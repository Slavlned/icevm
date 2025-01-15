package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/*
Вызов функции в VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class VmInstrCall implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // аргументы
    private final VmVarContainer args;

    // конструктор
    public VmInstrCall(VmInAddr addr, String name, VmVarContainer args, boolean hasPrevious) {
        this.addr = addr;
        this.name = name; this.args = args; this.hasPrevious = hasPrevious;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        if (!hasPrevious) {
            passArgs(vm, frame);
            if (frame.has(name)) {
                ((VmFunction)frame.lookup(addr, name)).exec(vm);
            } else {
                vm.callGlobal(addr, name);
            }
        } else {
            Object last = vm.pop();
            if (last instanceof VmObj vmObj) {
                passArgs(vm, frame);
                vmObj.call(addr, name, vm);
            } else if (last instanceof VmClass vmClass){
                passArgs(vm, frame);
                vmClass.getModFunctions().lookup(addr, name).exec(vm);
            } else {
                // рефлексийный вызов
                Method[] methods = last.getClass().getMethods();
                Method func = null;
                for (Method m : methods) {
                    if (m.getName().equals(name) &&
                        m.getParameterCount() == args.getVarContainer().size()) {
                        func = m;
                    }
                }
                if (func == null) {
                    IceVm.logger.error(addr, "function not found: " + name + " in: " + last.getClass().getName());
                }
                else {
                    passArgs(vm, frame);
                    ArrayList<Object> callArgs = new ArrayList<>();
                    for (int i = args.getVarContainer().size()-1; i >= 0; i--) {
                        Object arg = vm.pop();
                        callArgs.add(arg);
                    }
                    checkArgs(func.getParameterCount(), callArgs.size());
                    try {
                        Object returned = func.invoke(last, callArgs.toArray());
                        // 👇 НЕ ВОЗВРАЩАЕТ NULL, ЕСЛИ ФУНКЦИЯ НИЧЕГО НЕ ВОЗВРАЩАЕТ
                        if (returned != null) {
                            vm.push(returned);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        IceVm.logger.error(addr, e.getCause().getMessage());
                    }
                }
            }
        }
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            IceVm.logger.error(addr,
                    "args and params not match: (expected:"+parameterAmount+",got:"+argsAmount +")");
        }
    }

    // помещает аргументы в стек
    private void passArgs(IceVm vm, VmFrame<Object> frame) {
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
    }

    @Override
    public String toString() {
        return "CALL[" + name  + "]" + "(" + args.getVarContainer().size() + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
