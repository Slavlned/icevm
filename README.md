# icevm
Ice vm 🧊
Stack-based virtual machine written in java. 
Written for dynamic-typed OOP languages.
Supports functions, classes, static variables,
static function. Access modifiers not supported.

# documentation
Every IceVm instruction has address (class: VmInAddr) - address with line
from code of your programming language script.
Every value is a java Object, here is not any wrappers.

Class structure 🦩:
work in progress

Func structure 🏅:
work in progress

Basic instr list 🥶:
| Name         | Args                                                           | Action                                                                                                                                                                      |
|--------------|----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Push         | Init address (class: VmInAddr), object to push (class: Object) | Pushing object to stack                                                                                                                                                     |
| Pop          | Init address (class: VmInAddr)                                 | Deleting last object from stack                                                                                                                                             |
| Load         | Init address (class: VmInAddr), variable name (class: String)  | Loads variable/function/class from current frame and root frames  to stack                                                                                                  |
| Arith        | Init address (class: VmInAddr), operator (class: String)       | Doing arith operation of last  two objects from stack                                                                                                                       |
| Store        | Init address (class: VmInAddr), name (class: String)           | Stores variable with last stack value to current frame                                                                                                                      |
| Store_L      | Init address (class: VmInAddr), name (class: String)           | Stores local variable to current frame with last stack value to current frame. Ignores global variable overrides. Usefull for temp variables, like in for or foreach loops. |
| Dup          | Init address (class: VmInAddr)                                 | Duplicates last stack value.                                                                                                                                                |
| Del_L        | Init address (class: VmInAddr), name (class: String)           | Removes local variable from current frame. Ignores global variables                                                                                                         |
| CondOp       | Init address (class: VmInAddr), operator (class: String)       | Logicall operation on last two values from stack. Pushing boolean to stack.                                                                                                 |
| ComputeConds | Init address (class: VmInAddr), operator (class: String)       | Checking equallity between last two values from stack. Pushing boolean to stack.                                                                                            |
| Call         | Init address (class: VmInAddr), name (class: String)           | Calling function from functions storage \|\| calling function from  VmObj or VmClass in stack.                                                                              |
