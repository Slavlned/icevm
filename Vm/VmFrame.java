package com.slavlend.Vm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/*
Фрейм стека - дженерик, являющийся
хранилищем для ВМ
 */
@Getter
public class VmFrame<T> {
    // значения для хранения
    private final HashMap<String, T> values = new HashMap<>();
    /* рутовый фрейм, предназначен для поиска
       в случае отсутствия в текущем фрейме переменной.
       выглядит в виде иерархии:
       функци -> класс -> глобал
     */
    @Setter
    private VmFrame<T> root;

    /**
     * Ищет значение в фрейме
     * @param name - имя значения
     * @return возвращает значение
     */
    public T lookup(VmInAddr addr, String name) {
        VmFrame<T> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                IceVm.logger.error(addr,"not found: " + name);
            }
            current = current.root;
        }
        return current.getValues().getOrDefault(name, null);
    }

    /**
     * Устанавливает значение в фрейм
     * @param name - имя значения
     * @param val - значение
     */
    public void set(String name, T val) {
        VmFrame<T> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                break;
            }
            current = current.root;
        }
        if (current.getValues().containsKey(name)) {
            current.getValues().put(name, val);
        }
        else {
            getValues().put(name, val);
        }
    }

    /**
     * Возвращает бул показывающий
     * на то, найдено ли во фрейме
     * @return - найдено ли (бул)
     */
    public boolean has(String name) {
        VmFrame<T> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                return false;
            }
            current = current.root;
        }
        return current.getValues().containsKey(name);
    }
}
