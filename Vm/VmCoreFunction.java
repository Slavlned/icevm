package com.slavlend.Vm;

import com.slavlend.Vm.Instructions.VmInstrRet;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
Функция вшитая в вм
исполняющая джава код
 */
public interface VmCoreFunction{
    // вызов функции
    abstract Object exec(VmInAddr addr);
    abstract int argsAmount();
}
