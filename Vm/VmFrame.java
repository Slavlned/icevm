package com.slavlend.Vm;

import com.slavlend.Vm.Optimization.FasterMap;
import lombok.Getter;

import java.util.HashMap;

/*
Фрейм стека - дженерик, являющийся
хранилищем для ВМ
 */
@Getter
public class VmFrame<K, V> {
    // значения для хранения
    private FasterMap<K, V> values = new FasterMap<>();
    /* рутовый фрейм, предназначен для поиска
       в случае отсутствия в текущем фрейме переменной.
       выглядит в виде иерархии:
       функци -> класс -> глобал
     */
    private VmFrame<K, V> root;

    /**
     * Ищет значение в фрейме
     * @param name - имя значения
     * @return возвращает значение
     */
    public V lookup(VmInAddr addr, K name) {
        VmFrame<K, V> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                IceVm.logger.error(addr,"not found: " + name);
            }
            current = current.root;
        }
        return current.getValues().get(name);
    }

    /**
     * Устанавливает значение в фрейм
     * @param name - имя значения
     * @param val - значение
     */
    public void set(K name, V val) {
        VmFrame<K, V> current = this;
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
    public boolean has(K name) {
        VmFrame<K, V> current = this;
        while (!current.getValues().containsKey(name)) {
            if (current.root == null) {
                return false;
            }
            current = current.root;
        }
        return current.getValues().containsKey(name);
    }

    /**
     * Установка рут фрейма, если у
     * этого фрейма уже есть рут,
     * то ставиться рут для рут... и тд.
     * @param rootFrame - фрейм
     */
    public void setRoot(VmFrame<K, V> rootFrame) {
        VmFrame<K, V> current = this;
        if (this.root == rootFrame) { return; }
        while (current.getRoot() != null) {
            if (current.getRoot() == rootFrame) { return; }
            current = current.getRoot();
        }
        current.root = rootFrame;
    }

    // в строку
    @Override
    public String toString() {
        return "VmFrame{" +
                "values=" + values +
                ", root=" + root +
                '}';
    }

    /*
    Копирование
     */
    public VmFrame<K, V> copy() {
        VmFrame<K, V> copied = new VmFrame<K, V>();
        copied.values = new FasterMap<K, V>(getValues());
        return copied;
    }
}
