# icevm
Ice vm 🧊
Stack-based virtual machine written in java.

# documentation
Every IceVm instruction has address (class: VmInAddr) - address with line
from code of your programming language script.

IceVm instr list 🥶
| Name  | Args                                                           | Action                                                 |
|-------|----------------------------------------------------------------|--------------------------------------------------------|
| Push  | Init address (class: VmInAddr), object to push (class: Object) | Pushing object to stack                                |
| Pop   | Init address (class: VmInAddr)                                 | Deleting last object from stack                        |
| Load  | Init address (class: VmInAddr), variable name (class: String)  | Loads variable from current frame                      |
| Arith | Init address (class: VmInAddr), operator (class: String)       | Doing arith operation of last  two objects from stack  |
| Store | Init address (class: VmInAddr), name (class: String)           | Stores variable with last stack value to current frame |
|       |                                                                |                                                        |
|       |                                                                |                                                        |
