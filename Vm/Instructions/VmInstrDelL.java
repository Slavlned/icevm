package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Удаляет локальную переменную в ВМ
 */
@Getter
public class VmInstrDelL implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя переменной
    private final String name;

    public VmInstrDelL(VmInAddr addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        frame.getValues().remove(name);
    }

    @Override
    public String toString() {
        return "DEL_L(" + name + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
