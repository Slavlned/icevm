package com.slavlend.Vm;

/*
Вывод ошибок
 */
public interface VmErrLogger {
    /**
     * Выводит ошибку
     * @param addr - аддресс из
     *             скомпилированного кода
     * @param message - сообщение
     */
    void error(VmInAddr addr, String message);
}
