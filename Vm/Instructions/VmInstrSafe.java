package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
Блок сэйф для VM
 */
@Getter
public class VmInstrSafe implements VmInstr, VmInstrContainer {
    // адресс
    private final VmInAddr addr;
    // инструкции
    private final List<VmInstr> instructions = new ArrayList<>();
    // инструкции хэндлера
    private final List<VmInstr> handleInstructions = new ArrayList<>();

    // пишем ли мы хэндл
    @Setter
    private boolean writingHandlePart;

    // конструктор
    public VmInstrSafe(VmInAddr addr) {
        this.addr = addr;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        try {
            for (VmInstr instr : instructions) {
                instr.run(vm, frame);
            }
        } catch (VmThrowable throwable) {
            vm.push(throwable.getThrowableValue());
            handle(vm, frame);
        } catch (VmException exception) {
            vm.push(exception.getMessage());
            handle(vm, frame);
        }
    }

    private void handle(IceVm vm, VmFrame<Object> frame) {
        for (VmInstr instr : handleInstructions) {
            instr.run(vm, frame);
        }
    }

    @Override
    public void visitInstr(VmInstr instr) {
        if (isWritingHandlePart()) {
            this.handleInstructions.add(instr);
        }
        else {
            this.instructions.add(instr);
        }
    }

    @Override
    public String toString() {
        return "SAFE()";
    }

    @Override
    public void print() {
        System.out.println("╭────────safe block────────╮");
        for (VmInstr instr : instructions) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
        System.out.println("╭───────handler block──────╮");
        for (VmInstr instr : handleInstructions) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
    }
}
