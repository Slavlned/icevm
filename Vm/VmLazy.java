package com.slavlend.Vm;

import com.slavlend.Vm.Instructions.VmInstrPush;

/*
Ленивое значение
 */
public class VmLazy {
    // контейнер для вычисления ленивого значения
    private final VmVarContainer lazyContainer;
    // значение
    private Object value = null;

    /**
     * Конструктор
     */
    public VmLazy(VmVarContainer lazyContainer) {
        this.lazyContainer = lazyContainer;
    }

    /**
     * Из объекта
     * @param addr - аддресс
     * @param value - значение
     * @return - ленивое значение
     */
    public static VmLazy of(VmInAddr addr, Object value) {
        VmVarContainer c = new VmVarContainer();
        c.visitInstr(new VmInstrPush(addr, value));
        return new VmLazy(c);
    }

    /**
     * Получение значения
     * @return вычисленное значение
     */
    public Object get() {
        if (value == null) {
            value = lazyContainer.exec();
        }
        return value;
    }

    /**
     * Вычисление значения
     */
    public void eval() {
        if (value == null) {
            value = lazyContainer.exec();
        }
    }
}
