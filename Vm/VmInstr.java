package com.slavlend.Vm;

/*
Инструкция
 */
public interface VmInstr {
    void run(IceVm vm, VmFrame<String, Object> scope);
    void print();
}