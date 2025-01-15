package com.slavlend.Vm;

import com.slavlend.Compiler.Compiler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
Код поступающий на вход для виртуальной машины.
Данный класс предоставляет реализацию инструментов,
для работы с инструкциями ВМ.
 */
@Getter
public class VmCode {
    // инструкции для исполнения виртуальной машине
    private final List<VmInstr> instructions = new ArrayList<>();
    /* куда записывать инструкции, если не в основной
    поток инструкций. Интерфейс VmInstrContainer реализуют:
        @VmClass
        @VmFunction
        @VmIfInstr
        @VmLoopInstr
     */
    private final Stack<VmInstrContainer> writeTo = new Stack<>();


    // конструктор
    public VmCode() {

    }

    /**
     Дефайн функции в глобальном хранилище
     @param fn - функция для дефайна
     */
    public void defineFunction(VmInAddr addr, VmFunction fn) {
        if (writeTo.isEmpty()) {
            Compiler.iceVm.getFunctions().getValues().put(fn.getName(), fn);
        } else {
            if (writeTo.lastElement() instanceof VmClass vmClass) {
                if (!vmClass.isModuleFunctionsWriting()) {
                    vmClass.getFunctions().set(fn.getName(), fn);
                } else {
                    vmClass.getModFunctions().set(fn.getName(), fn);
                }
            }
            else {
                IceVm.logger.error(addr,"cannot define functions in block, except class");
            }
        }
    }

    /**
     Дефайн класса в глобальном хранилище
     @param clazz - класс для дефайна
     */
    public void defineClass(VmInAddr addr, VmClass clazz) {
        if (writeTo.isEmpty()) {
            Compiler.iceVm.getClasses().getValues().put(clazz.getName(), clazz);
        } else {
            IceVm.logger.error(addr,"cannot define classes in block, except class");
        }
    }
    /**
     Запись инструкции
     @param instr - инструкция для запииси
     */
    public void visitInstr(VmInstr instr) {
        if (writeTo.isEmpty()) {
            this.instructions.add(instr);
        } else {
            writeTo.lastElement().visitInstr(instr);
        }
    }

    /**
     * Выход из текущего блока
     * для записи
     */
    public void endWrite() {
        this.writeTo.pop();
    }

    /**
     * Вход в блок для записи,
     * например для записи в функцию
     * @param c - блок для записи
     */
    public void startWrite(VmInstrContainer c) {
        this.writeTo.push(c);
    }
}
