package com.slavlend.Vm;

import lombok.Getter;

/*
Что либо выкидываемое
 */
@Getter
public class VmThrowable extends RuntimeException {
    // значение для выкидываемого
    private final Object throwableValue;

    /**
     * Конструктор
     * @param throwableValue
     * - объект для выкидывания
     */
    public VmThrowable(Object throwableValue) {
        this.throwableValue = throwableValue;
    }
}
