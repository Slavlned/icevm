package com.slavlend.Vm.Instructions;

import com.slavlend.Parser.Operator;
import com.slavlend.Vm.*;
import lombok.Getter;

/*
Логика VM
 */
@SuppressWarnings("ConstantValue")
@Getter
public class VmInstrLgcl implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // оператор
    private final Operator operator;

    public VmInstrLgcl(VmInAddr addr, Operator operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        Object r = vm.pop(addr);
        Object l = vm.pop(addr);
        switch (operator.operator) {
            case "&&" -> {
                vm.push((boolean) r && (boolean) l);
            }
            case "||" -> {
                vm.push((boolean) r || (boolean) l);
            }
        }
    }

    @Override
    public String toString() {
        return "LGCL(" + operator.operator + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
