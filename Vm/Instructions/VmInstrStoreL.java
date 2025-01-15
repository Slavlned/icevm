package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Сохраняет значение в ТОЛЬКО ЛОКАЛЬНУЮ переменную в VM
 */
@Getter
public class VmInstrStoreL implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя переменной
    private final String name;

    // конструктор
    public VmInstrStoreL(VmInAddr addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        frame.getValues().put(name, vm.pop());
    }

    @Override
    public String toString() {
        return "STORE_L(" + name + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
