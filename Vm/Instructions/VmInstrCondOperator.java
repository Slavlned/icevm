package com.slavlend.Vm.Instructions;

import com.slavlend.Parser.Operator;
import com.slavlend.Vm.*;
import lombok.Getter;

/*
Кодишен VM
 */
@SuppressWarnings("ConstantValue")
@Getter
public class VmInstrCondOperator implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // оператор
    private final Operator operator;

    public VmInstrCondOperator(VmInAddr addr, Operator operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        switch (operator.operator) {
            case "==" -> vm.push(equal(l, r));
            case "!=" -> vm.push(!equal(l, r));
            case "<" -> vm.push((float)l < (float)r);
            case ">" -> vm.push((float)l > (float)r);
            case "<=" -> vm.push((float)l <= (float)r);
            case ">=" -> vm.push((float)l >= (float)r);
            default -> throw new IllegalStateException("unexpected value: " + operator.operator);
        }
    }

    // сравнивает два объекта
    public static boolean equal(Object l, Object r) {
        if (l instanceof String && r instanceof String) {
            return ((String)l).equals(((String)r));
        }
        else if (l == null && r != null) {
            return false;
        }
        else if (l != null && r == null) {
            return false;
        }
        else if (l == null && r == null) {
            return true;
        }
        else if (l instanceof VmObj && r instanceof VmObj) {
            return (((VmObj)l) == ((VmObj)r));
        }
        else if (l instanceof Boolean && r instanceof Boolean) {
            return (((boolean)l) == ((boolean)r));
        }
        else if (l instanceof Float && r instanceof Float) {
            return (((float) l) == ((float) r));
        }
        else if (l instanceof VmClass && r instanceof VmClass) {
            return (((VmClass)l) == ((VmClass)r));
        }
        else {
            throw new RuntimeException("invalid comparable types: " + l + ", " + r);
        }
    }

    @Override
    public String toString() {
        return "COP(" + operator.operator + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
