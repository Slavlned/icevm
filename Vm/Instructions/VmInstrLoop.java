package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Цикл в VM
 */
@SuppressWarnings("UnnecessaryContinue")
@Getter
public class VmInstrLoop implements VmInstr, VmInstrContainer {
    // адресс
    private final VmInAddr addr;
    // инструкции
    private final List<VmInstr> instructions = new ArrayList<>();

    // конструктор
    public VmInstrLoop(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        while (true) {
            try {
                for (VmInstr instr : instructions) {
                    instr.run(vm, frame);
                }
            } catch (VmInstrLoopEnd e) {
                if (e.isOnlyCurrentIteration()) {
                    continue;
                }
                else {
                    break;
                }
            }
        }
    }

    @Override
    public void visitInstr(VmInstr instr) {
        this.instructions.add(instr);
    }

    @Override
    public String toString() {
        return "LOOP()";
    }

    @Override
    public void print() {
        System.out.println("╭────────loop block────────╮");
        for (VmInstr instr : instructions) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
    }
}
