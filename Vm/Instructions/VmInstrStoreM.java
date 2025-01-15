package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Сохраняет значение в МОДУЛЬНУЮ ПЕРЕМЕННУЮ класса в ВМ
 */
@Getter
public class VmInstrStoreM implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // класс
    private final VmClass vmClass;
    // имя переменной
    private final String name;

    // конструктор
    public VmInstrStoreM(VmInAddr addr, VmClass vmClass, String name) {
        this.addr = addr;
        this.vmClass = vmClass;
        this.name = name;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        vmClass.getModValues().set(name, vm.pop());
    }

    @Override
    public String toString() {
        return "STORE_M(" + name + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
