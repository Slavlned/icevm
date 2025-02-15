package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Сохраняет значение в переменную в VM
 */
@Getter
public class VmInstrStore implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;
    // для присваивания
    private final VmVarContainer args;

    // конструктор
    public VmInstrStore(VmInAddr addr, String name, boolean hasPrevious, VmVarContainer args) {
        this.addr = addr;
        this.name = name;
        this.hasPrevious = hasPrevious;
        this.args = args;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        if (!hasPrevious) {
            passArgs(vm, frame);
            frame.set(name, vm.pop(addr));
        } else {
            Object last = vm.pop(addr);
            if (last instanceof VmObj vmObj) {
                passArgs(vm, frame);
                vmObj.getScope().set(name, vm.pop(addr));
            } else {
                passArgs(vm, frame);
                ((VmClass) last).getModValues().set(name, VmLazy.of(addr, vm.pop(addr)));
            }
        }
    }

    // помещает аргументы в стек
    private void passArgs(IceVm vm, VmFrame<String, Object> frame) {
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
    }

    @Override
    public String toString() {
        return "STORE(" + name + ")";
    }

    @Override
    public void print() {
        for (VmInstr i : args.getVarContainer()) {
            i.print();
        }
        System.out.println(this);
    }
}
