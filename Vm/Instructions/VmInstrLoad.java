package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Помещение переменной по имени в стек VM
 */
@Getter
public class VmInstrLoad implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // имя переменной
    private final String name;
    // есть ли предыдущий аксесс
    private final boolean hasPrevious;

    // конструктор
    public VmInstrLoad(VmInAddr addr, String name, boolean hasPrevious) {
        this.addr = addr;
        this.name = name; this.hasPrevious = hasPrevious;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        if (!hasPrevious) {
            if (frame.has(name)) {
                vm.load(addr, frame, name);
            }
            else if (vm.getClasses().has(name)) {
                vm.push(vm.getClasses().lookup(addr, name));
            } else if (vm.getFunctions().has(name))  {
                vm.push(vm.getFunctions().lookup(addr, name));
            } else {
                IceVm.logger.error(addr, "not found: " + name);
            }
        } else {
            Object last = vm.pop();
            if (last instanceof VmObj vmObj) {
                if (vmObj.getScope().has(name)) {
                    vm.push(vmObj.getScope().lookup(addr, name));
                } else if (vmObj.getClazz().getFunctions().has(name)) {
                    vm.push(vmObj.getClazz().getFunctions().lookup(addr, name));
                } else {
                    IceVm.logger.error(addr, "not found: " + name);
                }
            } else {
                VmClass clazz = (VmClass) last;
                if (clazz.getModValues().has(name)) {
                    vm.push(clazz.getModValues().lookup(addr, name));
                } else if (clazz.getModFunctions().has(name)){
                    vm.push(clazz.getModFunctions().lookup(addr, name));
                }
            }
        }
    }

    @Override
    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "LOAD(" + name + ")";
    }
}
