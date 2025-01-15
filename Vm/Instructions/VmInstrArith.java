package com.slavlend.Vm.Instructions;

import com.slavlend.Parser.Operator;
import com.slavlend.Vm.*;
import lombok.Getter;

/*
Инструкция арифметической операции
 */
@SuppressWarnings({"SpellCheckingInspection", "RedundantCast", "ConstantValue"})
@Getter
public class VmInstrArith implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // оператор
    private final Operator operator;

    public VmInstrArith(VmInAddr addr, Operator operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        Object r = vm.pop();
        Object l = vm.pop();
        if (r == null) {
            r = "nil";
        }
        if (l == null) {
            l = "nil";
        }
        switch (operator.operator) {
            case "+" -> {
                if (l instanceof String || r instanceof String) {
                    vm.push(l.toString() + r.toString());
                    return;
                }
                vm.push((float)l + (float)r);
            }
            case "-" -> vm.push((float)l - (float)r);
            case "*" -> vm.push((float)l * (float)r);
            case "/" -> vm.push((float)l / (float)r);
            case "%" -> vm.push((float)l % (float)r);
            default -> IceVm.logger.error(addr, "operator not found: " + operator.operator);
        }
    }

    @Override
    public void print() {
        System.out.println("ARITH("+operator.operator+")");
    }

    public boolean equal(Object l, Object r) {
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
            return (l == r);
        }
        else if (l instanceof Boolean && r instanceof Boolean) {
            return (l == r);
        }
        else if (l instanceof Float && r instanceof Float) {
            return (l == r);
        }
        else if (l instanceof VmClass && r instanceof VmClass) {
            return (l == r);
        }
        else {
            IceVm.logger.error(addr, "invalid comparable types: " + l + ", " + r);
            return false;
        }
    }

    @Override
    public String toString() {
        return "ARITH(" + operator.operator + ")";
    }
}
