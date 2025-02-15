package com.slavlend.Vm;

import com.slavlend.Vm.Instructions.VmInstrDecorate;
import com.slavlend.Vm.Instructions.VmInstrMakeClosure;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/*
Класс VM
 */
@Getter
public class VmClass implements VmInstrContainer {
    // имя класса
    private final String name;
    // полное имя
    private final String fullName;
    // функции
    private final VmFrame<String, VmFunction> functions = new VmFrame<>();
    // модульные значения
    private final VmFrame<String, VmLazy> modValues = new VmFrame<>();
    // конструктор для класса
    private final ArrayList<String> constructor;
    // модульные функции
    private final VmFrame<String, VmFunction> modFunctions = new VmFrame<>();
    // адресс
    private final VmInAddr addr;
    // пишется ли в модульные функции
    @Setter
    private boolean isModuleFunctionsWriting = false;

    // конструктор
    public VmClass(String name, String fullName, ArrayList<String> constructor, VmInAddr addr) {
        this.name = name;
        this.fullName = fullName;
        this.constructor = constructor;
        this.addr = addr;
    }

    /**
     * Записывает инструкцию
     * @param instr - инструкция, в данном
     *              случае - функция
     */
    @Override
    public void visitInstr(VmInstr instr) {
        // пропускаем инструкции декорирования,
        // их компиляция целиком лежит на руках
        // человека, который использует ВМ.
        if (instr instanceof VmInstrDecorate) {
            return;
        }
        IceVm.logger.error(addr, "cannot visit instr: " + instr + " with class!");
    }

    /**
     * Выводит информацию о классе в консоль
     */
    public void print() {
        System.out.println("╭──────────class───────────╮");
        for (VmFunction instr : functions.getValues().values()) {
            instr.print();
        }
        System.out.println("╭───────────mod────────────╮");
        for (VmFunction instr : modFunctions.getValues().values()) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
        System.out.println("╰──────────────────────────╯");
    }

    // в строку

    @Override
    public String toString() {
        return "VmClass{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", addr=" + addr +
                ", funcs=" + getModFunctions().getValues().getValues().toString() +
                ", values=" + getModValues().getValues().getValues().toString() +
                '}';
    }
}
