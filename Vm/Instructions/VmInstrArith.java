package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

/*
Инструкция арифметической операции
 */
@SuppressWarnings({"SpellCheckingInspection", "UnnecessaryToStringCall", "ClassCanBeRecord", "UnnecessaryReturnStatement"})
@Getter
public class VmInstrArith implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // оператор
    private final String operator;

    public VmInstrArith(VmInAddr addr, String operator) {
        this.addr = addr;
        this.operator = operator;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        Object r = vm.pop(addr);
        Object l = vm.pop(addr);
        if (r == null) {
            r = "nil";
        }
        if (l == null) {
            l = "nil";
        }
        checkForTypes(r, l);
        switch (operator) {
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
            default -> IceVm.logger.error(addr, "operator not found: " + operator);
        }
    }

    // проверка на типы
    private void checkForTypes(Object r, Object l) {
        boolean stringOrFloatRight = !(r instanceof String) && !(r instanceof Float);
        boolean stringOrFloatLeft = !(l instanceof String) && !(l instanceof Float);
        if (stringOrFloatRight || (r instanceof String && !operator.equals("+"))) {
            IceVm.logger.error(addr, "invalid value type for operator (" + operator + "): " + r);
            return;
        }
        if (stringOrFloatLeft || (l instanceof String && !operator.equals("+"))) {
            IceVm.logger.error(addr, "invalid value type for operator (" + operator + "): " + l);
            return;
        }
    }

    @Override
    public void print() {
        System.out.println("ARITH("+operator+")");
    }

    @Override
    public String toString() {
        return "ARITH(" + operator + ")";
    }
}
