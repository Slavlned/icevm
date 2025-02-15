package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;

/*
Удаление верхнего значения из стека VM
 */
public class VmInstrPop implements VmInstr {
    // адресс
    private final VmInAddr addr;

    // конструктор
    public VmInstrPop(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        vm.pop(addr);
    }

    @Override
    public String toString() {
        return "POP()";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
