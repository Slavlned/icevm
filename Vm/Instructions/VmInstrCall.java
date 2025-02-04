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
    // выключен ли пуш
    private final boolean shouldPushResult;

    // конструктор
    public VmInstrCall(VmInAddr addr, String name, VmVarContainer args, boolean hasPrevious, boolean shouldPushResult) {
        this.addr = addr;
        this.name = name; this.args = args; this.hasPrevious = hasPrevious;
        this.shouldPushResult = shouldPushResult;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        if (!hasPrevious) {
            callGlobalFunc(vm, frame);
        } else {
            Object last = vm.pop(addr);
            if (last instanceof VmObj vmObj) {
                callObjFunc(vm, frame, vmObj);
            } else if (last instanceof VmClass vmClass){
                callClassFunc(vm, frame, vmClass);
            } else {
                callReflectionFunc(vm, frame, last);
            }
        }
    }

    // Вызывает функцю объекта
    private void callObjFunc(IceVm vm, VmFrame<Object> frame, VmObj vmObj) {
        // аргументы
        int argsAmount = passArgs(vm, frame);
        VmFunction fn;
        if (vmObj.getClazz().getFunctions().has(name)) {
            fn = vmObj.getClazz().getFunctions().lookup(addr, name);
        } else {
            fn = (VmFunction)vmObj.getScope().lookup(addr, name);
        }
        checkArgs(fn.getArguments().size(), argsAmount);
        // вызов
        vmObj.call(addr, name, vm, shouldPushResult);
    }

    // Вызывает функцю класса
    private void callClassFunc(IceVm vm, VmFrame<Object> frame, VmClass vmClass) {
        // аргументы
        int argsAmount = passArgs(vm, frame);
        VmFunction fn;
        if (vmClass.getModFunctions().has(name)) {
            fn = vmClass.getModFunctions().lookup(addr, name);
        } else {
            fn = (VmFunction)vmClass.getModValues().lookup(addr, name);
        }
        checkArgs(fn.getArguments().size(), argsAmount);
        // вызов модульной функции
        vmClass.getModFunctions().lookup(addr, name).exec(vm, shouldPushResult);
    }

    // Вызывает рефлексийную функцию
    private void callReflectionFunc(IceVm vm, VmFrame<Object> frame, Object last) {
        // аргументы
        int argsAmount = passArgs(vm, frame);
        ArrayList<Object> callArgs = new ArrayList<>();
        for (int i = argsAmount-1; i >= 0; i--) {
            Object arg = vm.pop(addr);
            callArgs.add(0, arg);
        }
        // рефлексийный вызов
        Method[] methods = last.getClass().getMethods();
        Method func = null;
        for (Method m : methods) {
            // System.out.println("name: " + name + ":" + m.getParameterCount() + ", " + args.getVarContainer());
            if (m.getName().equals(name) &&
                    m.getParameterCount() == argsAmount) {
                func = m;
            }
        }
        if (func == null) {
            IceVm.logger.error(addr, "function not found: " + name + " in: " + last.getClass().getName());
        }
        else {
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

    // Вызов функции из глобального скоупа
    private void callGlobalFunc(IceVm vm, VmFrame<Object> frame) {
        if (frame.has(name)) {
            // аргументы
            int argsAmount = passArgs(vm, frame);
            VmFunction fn = ((VmFunction)frame.lookup(addr, name));
            checkArgs(fn.getArguments().size(), argsAmount);
            // вызов
            fn.exec(vm, shouldPushResult);
        } else {
            // аргументы
            int argsAmount = passArgs(vm, frame);
            // вызов
            if (!vm.isCoreFunc(name)) {
                if (vm.getFunctions().has(name)) {
                    checkArgs(vm.getFunctions().lookup(addr, name).getArguments().size(), argsAmount);
                }
                else {
                    checkArgs(((VmFunction)vm.getVariables().lookup(addr, name)).getArguments().size(), argsAmount);
                }
                vm.callGlobal(addr, name, shouldPushResult);
            } else {
                checkArgs(vm.getCoreFunctions().lookup(addr, name).argsAmount(), argsAmount);
                vm.callGlobal(addr, name, shouldPushResult);
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
    private int passArgs(IceVm vm, VmFrame<Object> frame) {
        int size = vm.stack().size();
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.stack().size()-size;
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
