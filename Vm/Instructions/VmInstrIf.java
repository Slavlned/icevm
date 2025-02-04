package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
Блок иф для VM
 */
@Getter
public class VmInstrIf implements VmInstr, VmInstrContainer {
    // адресс
    private final VmInAddr addr;
    // инструкции кондишенов
    private final List<VmInstr> condInstructions = new ArrayList<>();
    // инструкции
    private final List<VmInstr> instructions = new ArrayList<>();
    // блок иф для else
    private VmInstrIf elseCondition;

    // пишем ли мы кондишены
    @Setter
    private boolean writingConditions;

    // конструктор
    public VmInstrIf(VmInAddr addr) {
        this.addr = addr;
    }

    // установка else
    public void setElse(VmInstrIf _else) {
        this.elseCondition = _else;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        for (VmInstr instr : condInstructions) {
            instr.run(vm, frame);
        }
        Boolean bool = (Boolean) vm.pop(addr);
        if (bool) {
            for (VmInstr instr : instructions) {
                instr.run(vm, frame);
            }
        } else {
            if (elseCondition != null) {
                elseCondition.run(vm, frame);
            }
        }
    }

    @Override
    public void visitInstr(VmInstr instr) {
        if (isWritingConditions()) {
            this.condInstructions.add(instr);
        }
        else {
            this.instructions.add(instr);
        }
    }

    @Override
    public String toString() {
        return "IF()";
    }

    @Override
    public void print() {
        System.out.println("╭────if/elif/else block────╮");
        System.out.println("[conds]");
        for (VmInstr instr : condInstructions) {
            instr.print();
        }
        System.out.println("[endconds]");
        for (VmInstr instr : instructions) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
        if (elseCondition != null) {
            elseCondition.print();
        }
    }
}
