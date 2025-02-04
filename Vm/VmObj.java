package com.slavlend.Vm;

import lombok.Getter;

/*
Инстанс класса ВМ
 */
@Getter
public class VmObj {
    // скоуп
    private final VmFrame<Object> scope = new VmFrame<>();
    // класс
    private final VmClass clazz;
    // адресс
    private final VmInAddr addr;

    // конструктор
    public VmObj(IceVm vm, VmClass clazz, VmInAddr addr) {
        this.clazz = clazz;
        this.addr = addr;
        for (int i = clazz.getConstructor().size()-1; i >= 0; i--) {
            Object arg = vm.pop(addr);
            scope.set(clazz.getConstructor().get(i), arg);
        }
        scope.setRoot(vm.getVariables());
        if (clazz.getFunctions().getValues().containsKey("init")) {
            call(addr, "init", vm, true);
        }
    }

    /**
     * Вызов функции объекта
     * @param name - имя функции
     * @param vm - ВМ
     */
    public void call(VmInAddr inAddr, String name, IceVm vm, boolean shouldPushResult) {
        // копируем и вызываем функцию
        VmFunction func;
        if (clazz.getFunctions().getValues().containsKey(name)) {
            func = clazz.getFunctions().getValues().get(name).copy();
        } else {
            func = ((VmFunction)scope.lookup(inAddr, name)).copy();
        }
        func.setDefinedFor(this);
        func.getScope().get().setRoot(scope);
        func.exec(vm, shouldPushResult);
    }
}
