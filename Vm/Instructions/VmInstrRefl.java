package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/*
Создание рефлексийного объекта
 */
@SuppressWarnings({ "rawtypes"})
@Getter
public class VmInstrRefl implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя
    private final String name;
    // аргументы конструктора
    private final VmVarContainer args;

    // конструктор
    public VmInstrRefl(VmInAddr addr, String name, VmVarContainer args) {
        this.addr = addr;
        this.name = name;
        this.args = args;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        // ищем класс
        try {
            // класс
            Class<?> clazz = VmJvmClasses.lookup(addr, name);
            // колличество аргументов
            int argsAmount = passArgs(vm, frame);
            // аргументы
            ArrayList<Object> callArgs = new ArrayList<>();
            for (int i = argsAmount-1; i >= 0; i--) {
                Object arg = vm.pop(addr);
                callArgs.add(0, arg);
            }
            // ищем конструктор
            Constructor constructor = null;
            for (Constructor c : clazz.getConstructors()) {
                if (c.getParameterCount() == argsAmount) {
                    constructor = c; 
                }
            }
            if (constructor == null) {
                throw new VmException(
                        addr, 
                        "constructor with args amount: "
                                + args + " not found for class: " 
                                + clazz.getSimpleName()
                );
            }
            Object o = constructor.newInstance(callArgs.toArray());
            vm.push(o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new VmException(addr, e.getMessage());
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

    // передача аргументов
    private int passArgs(IceVm vm, VmFrame<String, Object> frame) {
        int size = vm.stack().size();
        for (VmInstr instr : args.getVarContainer()) {
            instr.run(vm, frame);
        }
        return vm.stack().size()-size;
    }
}
