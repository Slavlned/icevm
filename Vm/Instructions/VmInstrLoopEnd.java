package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;
import lombok.Getter;

/*
Ломает текущее исполнение цикла или переходит на следующую итерацию
 */
@SuppressWarnings("UnnecessaryToStringCall")
@Getter
public class VmInstrLoopEnd extends RuntimeException implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // только текущую итерацию
    private final boolean onlyCurrentIteration;

    public VmInstrLoopEnd(VmInAddr addr, boolean onlyCurrentIteration) {
        this.addr = addr;
        this.onlyCurrentIteration = onlyCurrentIteration;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> scope) {
        throw this;
    }

    @Override
    public String toString() {
        return "END(CI:"+ onlyCurrentIteration +")";
    }

    @Override
    public void print() {
        System.out.println(toString());
    }
}
