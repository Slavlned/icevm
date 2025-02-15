package com.slavlend.Vm.Instructions;

import lombok.Getter;
import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;

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
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        frame.getValues().put(name, vm.pop(addr));
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
