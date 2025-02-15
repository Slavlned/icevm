package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Выкидывает значение
 */
@Getter
public class VmInstrThrow implements VmInstr {
    // адресс
    private final VmInAddr addr;

    // конструктор
    public VmInstrThrow(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        Object o = vm.pop(addr);
        if (o instanceof VmException exception) {
            throw exception;
        }
        throw new VmThrowable(o);
    }

    @Override
    public String toString() {
        return "THROW()";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
