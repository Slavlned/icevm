package com.slavlend.Vm;

import com.slavlend.Colors;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/*
Виртуальная машина ICE
 */
@SuppressWarnings("unused")
@Getter
public class IceVm {
    // стек объектов
    private final ThreadLocal<Stack<Object>> stack = new ThreadLocal<>();
    // хранилище
    private final VmFrame<Object> variables = new VmFrame<>();
    private final VmFrame<VmFunction> functions = new VmFrame<>();
    private final VmFrame<VmClass> classes = new VmFrame<>();
    private final VmFrame<VmCoreFunction> coreFunctions = new VmFrame<>();
    // рэйс ошибок
    @Setter
    public static VmErrRaiser raiser = new VmErrRaiser();
    // логгер
    @Setter
    public static VmErrLogger logger;
    // адресс возврата
    private final ThreadLocal<Object> returnAddress = new ThreadLocal<>();

    /**
     * Помещение значения в адресс возврата
     */
    public void ret(Object o) {
        returnAddress.set(o);
    }

    /**
     *
     * @param code - код для запуска
     *             кода в виртуальной машине
     */
    public void run(VmCode code, boolean isDebugMode) {
        // запуск
        try {
            // выводим байткод (инструкции вм)
            if (isDebugMode) {
                printByteCode(code);
            }
            // запускаем бенчмарк
            VmBenchmark benchmark = new VmBenchmark();
            benchmark.start();
            // инициализация стека
            initStackForThread();
            // исполняем код
            for (VmInstr instr : code.getInstructions()) {
                instr.run(this, variables);
            }
            // останавливаем бенчмарк и
            // выводим время исполнения
            System.out.println();
            System.out.println(
                    Colors.ANSI_BLUE + "🧊 Exec time: " + benchmark.end() + ", stack size: "
                            + stack.get().size() + "(" + stack.get().toString() + ")" + Colors.ANSI_RESET
            );
        } catch (VmException exception) {
            logger.error(exception.getAddr(), exception.getMessage());
        }
    }

    /**
     * Инициализация стека под поток
     */
    public void initStackForThread() {
        stack.set(new Stack<>());
    }

    /**
     *
     * @param code - код для вывода
     *             в консоль
     */
    private void printByteCode(VmCode code) {
        System.out.println(Colors.ANSI_BLUE + "🛞 Bytecode:" + Colors.ANSI_RESET);
        printCode(code);
        System.out.println();
    }

    /**
     * Выводит инструкции
     * @param code - код для вывода
     *             в консоль
     */
    private void printCode(VmCode code) {
        // выводим классы
        for (VmClass clazz : classes.getValues().values()) {
            clazz.print();
        }
        // выводим функции
        for (VmFunction function : functions.getValues().values()) {
            function.print();
        }
        // выводим другие инструкции
        for (VmInstr instr : code.getInstructions()) {
            instr.print();
        }
    }

    /**
     * Пушит в стек объект
     * @param val - объект, для помещения в стек
     */
    public void push(Object val) {
        stack().push(val);
    }

    /**
     * Удаляет объект с верхушки стека, возвращая его
     * @return - отдаёт объект с верхушки стека
     */
    public Object pop(VmInAddr addr) {
        // if (stack().empty()) {
            // raiser.error(addr, "stack is empty here (did you forgot back statement?)");
        // }
        return stack().pop();
    }

    /**
     * Загружает переменную в стек
     * @param frame - фрейм, для поиска переменной
     * @param name - имя переменной для поиска
     */
    public void load(VmInAddr addr, VmFrame<Object> frame, String name) {
        stack().push(frame.lookup(addr, name));
    }

    /**
     * Получение следующей инструкции
     *
     */

    /**
     * Получение стека текущего потока
     * @return - стэк
     */
    public Stack<Object> stack() {
        return getStack().get();
    }

    /**
     * Вызывает глобальную функцию
     * @param name - имя для вызова
     */
    public void callGlobal(VmInAddr addr, String name, boolean shouldPushResult) {
        if (functions.getValues().containsKey(name)) {
            functions.lookup(addr, name).exec(this, shouldPushResult);
        } else if (variables.getValues().containsKey(name)){
            ((VmFunction)variables.lookup(addr, name)).exec(this, shouldPushResult);
        } else {
            Object res = coreFunctions.lookup(addr, name).exec(addr);
            if (res != null) {
                push(res);
            }
        }
    }

    /**
     * Функция ли это - из ядра
     * @param name - имя функции
     * @return да или нет
     */
    public boolean isCoreFunc(String name) {
        return coreFunctions.has(name);
    }
}
