package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Инструкция создания замыкания
 */
@Getter
public class VmInstrMakeClosure implements VmInstr {
    // аддресс
    private final VmInAddr addr;
    // имя функции
    private final String name;

    // конструктор
    public VmInstrMakeClosure(VmInAddr addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    // конструктор
    public VmInstrMakeClosure(VmInAddr addr) {
        this.addr = addr;
        this.name = null;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> scope) {
        VmFunction fn;
        if (name == null) {
            fn = (VmFunction) vm.pop(addr);
        }
        else {
            fn = (VmFunction) scope.lookup(addr, name);
            }
        fn.setClosure(scope.copy());
    }

    @Override
    public void print() {
        System.out.println("MAKE_CLOSURE(" + name + ")");
    }
}
