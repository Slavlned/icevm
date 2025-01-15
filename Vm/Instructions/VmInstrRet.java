package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;
import lombok.Getter;

/*
Ломает текущее выполнение, перед
вызовом этой инструкции в стек помещается значение
для возврата, которое в дальнейшем ловится
инструкцией Store
 */
@Getter
public class VmInstrRet extends RuntimeException implements VmInstr {
    // адресс
    private final VmInAddr addr;

    // конструктор
    public VmInstrRet(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> scope) {
        throw this;
    }

    @Override
    public String toString() {
        return "RET()";
    }

    @Override
    public void print() {
        System.out.println(toString());
    }
}
