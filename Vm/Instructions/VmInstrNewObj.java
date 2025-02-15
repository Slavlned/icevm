package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Помещение инстанса класса в VM
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
public class VmInstrNewObj implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя класса
    private final String className;
    // аргументы конструктора
    private final VmVarContainer args;

    // конструктор
    public VmInstrNewObj(VmInAddr addr, String className, VmVarContainer args) {
        this.addr = addr;
        this.className = className;
        this.args = args;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        // конструктор
        int amount = passArgs(vm, frame);
        VmClass clazz = vm.getClasses().lookup(addr, className);
        checkArgs(clazz.getConstructor().size(), amount);
        vm.push(new VmObj(vm, clazz, addr));
    }

    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "INST(" + className + "," + args.getVarContainer().size() + ")";
    }

    // передача аргументов
    private int passArgs(IceVm vm, VmFrame<String, Object> frame) {
        int size = vm.stack().size();
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.stack().size()-size;
    }

    // проверка на колличество параметров и аргументов
    private void checkArgs(int parameterAmount, int argsAmount) {
        if (parameterAmount != argsAmount) {
            IceVm.logger.error(addr,
                    "args and params not match: (expected:"+parameterAmount+",got:"+argsAmount +")");
        }
    }
}
