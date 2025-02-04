package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Создание рефлексийного объекта
 */
@SuppressWarnings({"ClassCanBeRecord", "deprecation"})
@Getter
public class VmInstrRefl implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя
    private final String name;

    // конструктор
    public VmInstrRefl(VmInAddr addr, String name) {
        this.addr = addr;
        this.name = name;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        // ищем класс
        try {
            Object o = VmJvmClasses.lookup(addr, name).newInstance();
            vm.push(o);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "REFLECT[" + name  + "]";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
