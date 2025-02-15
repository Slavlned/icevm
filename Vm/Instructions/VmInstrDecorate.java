package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Инструкция для декораторов
 */
@Getter
public class VmInstrDecorate implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // декоратор
    private VmVarContainer decorator;
    // функция
    private VmFunction fn;


    // конструктор
    public VmInstrDecorate(VmInAddr addr, VmVarContainer decorator, VmFunction fn) {
        this.addr = addr;
        this.decorator = decorator;
        this.fn = fn;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> frame) {
        // получаем декоратор
        VmFunction decoratorFn = (VmFunction) decorator.exec();
        // пушим функцию
        new VmInstrPush(addr, fn).run(vm, frame);
        // исполняем декоратор
        decoratorFn.exec(vm, false);
    }

    @Override
    public String toString() {
        return "DECORATE(" + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
