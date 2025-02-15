package com.slavlend.Vm.Instructions;

import lombok.Getter;
import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;

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
    public void run(IceVm vm, VmFrame<String, Object> frame) {
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
