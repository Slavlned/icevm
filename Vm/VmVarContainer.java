package com.slavlend.Vm;


import com.slavlend.Compiler.Compiler;
import lombok.Getter;

import java.util.ArrayList;

/*
Временный контейнер переменных для вызова функции
 */
@Getter
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class VmVarContainer implements VmInstrContainer{
    // контейнер
    private final ArrayList<VmInstr> varContainer = new ArrayList<>();

    /**
     * Выполняет инструкцию
     * @param instr - инструкция
     */
    @Override
    public void visitInstr(VmInstr instr) {
        this.varContainer.add(instr);
    }

    public Object exec() {
        for (VmInstr instr : varContainer) {
            instr.run(Compiler.iceVm, Compiler.iceVm.getVariables());
        }
        return Compiler.iceVm.pop(new VmInAddr(-1));
    }
}
