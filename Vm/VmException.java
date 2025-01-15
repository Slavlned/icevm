package com.slavlend.Vm;

import lombok.Getter;

/*
Выкидываемый эксцепшен
 */
@Getter
public class VmException extends RuntimeException {
    // адресс
    private final VmInAddr addr;
    // сообщение
    private final String message;

    /**
     * Конструктор
     * @param addr - адресс
     * @param message - сообщение
     */
    public VmException(VmInAddr addr, String message) {
        this.addr = addr;
        this.message = message;
    }
}
