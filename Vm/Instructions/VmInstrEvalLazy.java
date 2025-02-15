package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Вычисление ленивых статичных
/модульных переменных для класса.
 */
@Getter
public class VmInstrEvalLazy implements VmInstr {
    // класс
    private final VmClass vmClass;

    // конструктор
    public VmInstrEvalLazy(VmClass vmClass) {
        this.vmClass = vmClass;
    }

    @Override
    public void run(IceVm vm, VmFrame<String, Object> scope) {
        for (VmLazy lazy : vmClass.getModValues().getValues().getValues()) {
            lazy.eval();
        }
    }

    @Override
    public String toString() {
        return "EVAL_LAZY("+vmClass.getFullName()+")";
    }

    @Override
    public void print() {
        System.out.println("EVAL_LAZY("+vmClass.getFullName()+")");
    }
}
