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
    public void run(IceVm vm, VmFrame<Object> frame) {
        // конструктор
        passArgs(vm, frame);
        vm.push(new VmObj(vm, vm.getClasses().lookup(addr, className), addr));
    }

    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "INST(" + className + "," + args.getVarContainer().size() + ")";
    }

    private void passArgs(IceVm vm, VmFrame<Object> frame) {
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
    }
}
