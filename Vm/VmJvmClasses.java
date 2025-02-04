package com.slavlend.Vm;

import com.slavlend.Parser.Address;
import com.slavlend.PolarLogger;

import java.util.ArrayList;

/*
Jvm-классы для получения
в ВМ
 */
public class VmJvmClasses {
    // список классов
    private static final ArrayList<Class<?>> defined = new ArrayList<>();

    /**
     * Получения класса. Если класс
     * не найден в дефайнутых юзером,
     * ищем в стандартном класс лоадере
     * @param address - адресс
     * @param name - имя
     * @return класс
     */
    public static Class<?> lookup(VmInAddr address, String name) {
        for (Class<?> clazz : defined) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new VmException(address, "jvm class is not defined: " + name);
        }
    }

    /**
     * дефайн класса
     * @param clazz - класс для дефайна
     */
    public static void define(Class<?> clazz) {
        defined.add(clazz);
    }
}
