package com.slavlend.Vm;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Адресс скомпилированного кусочка
 */
@SuppressWarnings("ClassCanBeRecord")
@Getter
@AllArgsConstructor
public class VmInAddr {
    // линия
    private final int line;

    // в строку

    @Override
    public String toString() {
        return "VmInAddr{" +
                "line=" + line +
                '}';
    }
}
