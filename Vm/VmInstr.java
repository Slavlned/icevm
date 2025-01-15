package com.slavlend.Vm;

/*
Инструкция
 */
public interface VmInstr {
    void run(IceVm vm, VmFrame<Object> scope);
    void print();
}