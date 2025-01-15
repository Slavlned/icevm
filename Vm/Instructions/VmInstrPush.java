package com.slavlend.Vm.Instructions;

import com.slavlend.Vm.IceVm;
import com.slavlend.Vm.VmFrame;
import com.slavlend.Vm.VmInAddr;
import com.slavlend.Vm.VmInstr;
import lombok.Getter;

/*
Помещение значения в стек VM
 */
@Getter
public class VmInstrPush implements VmInstr {
    // адресс
    private final VmInAddr addr;
    // данные для пуша
    private final Object data;

    // конструктор
    public VmInstrPush(VmInAddr addr, Object data) {
        this.addr = addr;
        this.data = data;
    }

    @Override
    public void run(IceVm vm, VmFrame<Object> frame) {
        vm.push(data);
    }

    @Override
    public String toString() {
        return "PUSH(" + data + ")";
    }

    @Override
    public void print() {
        System.out.println(this);
    }
}
