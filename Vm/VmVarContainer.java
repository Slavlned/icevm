package com.slavlend.Vm;


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
}
