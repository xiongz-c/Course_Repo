import time
import argparse
import ctypes

IMP = ctypes.cdll.LoadLibrary('./ISE_SO/IMP.so')

if __name__ == '__main__':
    begin_time = time.time()
    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--file_name', type=str, default='./testcase/10w.txt')
    parser.add_argument('-k', '--seed', type=int, default=5)
    parser.add_argument('-m', '--model', type=str, default='IC')
    parser.add_argument('-t', '--time_limit', type=int, default=20)

    args = parser.parse_args()
    file_name = args.file_name
    seed_size = args.seed
    model = args.model
    time_limit = args.time_limit
    IMP.c_imp(ctypes.c_char_p(file_name.encode()), ctypes.c_int(seed_size), ctypes.c_char_p(model.encode()),
              ctypes.c_int(time_limit))
