# IMP

> Influence maximization Problem(IMP) is a problem of finding a fixed number of nodes in a social network graph so that the influence of these nodes on all nodes is as large as possible.

## ISE

Two model to solve IMP: independent cascade (IC) and linear threshold model (LT). These model is to help us estimate influence for seeds.

## TestCase

Two graph(different size) and seed for ISE.



### IMP implement

### Two Version

The implement of ISE with C++ is in the folder `ISE_SO`. And the `IMP_C` use the C++ version code.

And the `IMP_py` is python version of this project.

*`IMP_time` is not IMM and run times dependence on Graph size.*

### Usage

```bash
$ python3 IMP.py -i <graph file path> -k <seeds number> -m <IC or LT> -t <time limit> 
```

### Performance

The submit version is python version.

#### Input

* e=0.5
* l=1
* graph=NetHEPT
* seeds number=500
* model=LT

#### Output

* run time: 6.15s [fast enough]
* Result: 5296.8661[but result is not the best]

