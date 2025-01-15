package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;
import lombok.Getter;

/*
Дублирует значение в стек VM
 */
@Getter
public class VmInstrDup implements VmInstr {
    // адресс
    private final VmInAddr addr;

    // конструктор
    public VmInstrDup(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        Object o = vm.pop();
        vm.push(o);
        vm.push(o);
    }

    @Override
    public String toString() {
        return "DUP(" + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
