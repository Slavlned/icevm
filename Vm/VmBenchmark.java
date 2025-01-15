package com.slavlend.Vm;

/*
Бенчмарки VM
 */
public class VmBenchmark {
    // первое зафиксированное время в мс
    private long startTime;

    /**
     * Запускает таймер
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Останавливает таймер и возвращает
     * секунды выполнения.
     * @return startTime - возвращает
     * время выполнения в секундах.
     */
    public double end() {
        return ((Long)(System.currentTimeMillis()-startTime)).doubleValue()/1000.0;
    }
}
