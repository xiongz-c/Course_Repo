# -*- coding: UTF-8 -*-
import math
def find_narcissistic_number(start:int,end:int)->list:
    ans_list = []
    for x in range(start,end):
        nums_list = []
        nums = 0
        tmp = x
        while tmp != 0:
            # print(str(nums) + "," +str(tmp))
            nums_list.append(tmp%10)
            tmp //= 10
            nums += 1
        sum = 0
        for i in range(0,nums):

            sum += int(pow(nums_list[i],nums))
        if sum == x:
            ans_list.append(x)
    return ans_list