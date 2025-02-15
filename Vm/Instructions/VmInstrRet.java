package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
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
    private final VmVarContainer ret;
    private final VmInAddr addr;

    // конструктор
    public VmInstrRet(VmVarContainer ret, VmInAddr addr) {
        this.ret = ret;
        this.addr = addr;
    }

    public void pushResult(IceVm vm, VmFrame<String, Object> scope) {
        for (VmInstr i : ret.getVarContainer()) {
            i.run(vm, scope);
        }
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> scope) {
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
