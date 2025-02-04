package com.slavlend.Vm;

import com.slavlend.Vm.Instructions.VmInstrRet;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
Функция вм
 */
@SuppressWarnings("UnnecessaryReturnStatement")
@Getter
public class VmFunction implements VmInstrContainer {
    // имя функции
    private final String name;
    // инструкции
    private List<VmInstr> instructions = new ArrayList<>();
    // аргументы
    private final ArrayList<String> arguments;
    // скоуп
    private final ThreadLocal<VmFrame<Object>> scope = new ThreadLocal<>();
    // объект чья функция
    @Setter
    private VmObj definedFor;
    // адресс
    private final VmInAddr addr;

    // конструкция
    public VmFunction(String name, ArrayList<String> arguments, VmInAddr addr) {
        this.name = name;
        this.arguments = arguments;
        this.addr = addr;
        this.scope.set(new VmFrame<>());
    }

    /**
     * Добавление инструкции в
     * функцию
     * @param instr - инструкция
     */
    public void add(VmInstr instr) {
        instructions.add(instr);
    }

    /**
     * Выполнение функции
     * @param vm - ВМ
     */
    public void exec(IceVm vm, boolean shouldPushResult) {
        scope.set(new VmFrame<>());
        if (definedFor == null) {
            getScope().get().setRoot(vm.getVariables());
        }
        loadArgs(vm);
        if (definedFor != null) {
            scope.get().set("this", definedFor);
        }
        try {
            // исполняем функцию
            for (VmInstr instr : instructions) {
                instr.run(vm, scope.get());
            }
        } catch (VmInstrRet e) {
            if (shouldPushResult) {
                e.pushResult(vm, scope.get());
            }
            return;
        }
    }

    /**
     * Загрузка аргументов в функции
     */
    private void loadArgs(IceVm vm) {
        // загружаем аргументы
        for (int i = arguments.size()-1; i >= 0; i--) {
            if (vm.getStack().get().isEmpty()) {
                IceVm.logger.error(addr,
                        "stack is empty. cannot invoke function: " + name);
            }
            Object arg = vm.pop(addr);
            scope.get().set(arguments.get(i), arg);
        }
    }

    /**
     Копия функции
     @return возвращает копию
     */
    public VmFunction copy() {
        VmFunction fn = new VmFunction(name, arguments, addr);
        fn.instructions = instructions;
        return fn;
    }

    /**
     * Добавляет инструкцию
     * @param instr - инструкция
     */
    @Override
    public void visitInstr(VmInstr instr) {
        this.instructions.add(instr);
    }

    /**
     * Выводит информацию о функции
     */
    public void print() {
        System.out.println("╭─────────function─────────╮");
        for (VmInstr instr : instructions) {
            instr.print();
        }
        System.out.println("╰──────────────────────────╯");
    }
}
