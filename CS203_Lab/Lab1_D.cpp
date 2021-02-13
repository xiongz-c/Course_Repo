#include <iostream>
using namespace std;
//#pragma GCC optimize(3,"Ofast","inline")
int main(){
//    ios::sync_with_stdio(false);
//    cin.tie(0);
    int len = 0;
    int target = 0;
    cin>>len>>target;
    int a[len];
    int z = 0;
    int f = 0;

    for(int i = 0;i<len;i++){
        cin>>a[i];
        if(a[i]>0){
            z++;
        } else if(a[i]<0){
            f++;
        }
    }
    int *p = a;
    int count = 0;
    if(target == 0){
        if(z+f != len){
            while(p <= &a[len-1]){
                if(*p == *(p-1)){
                    p++;
                    continue;
                }
                if(*p != 0){
                    count++;
                }
                p++;
            }
            if(len-z-f>1){
                count++;
            }
            cout<<count;
            return 0;
        }
    }else if(target<0){
        int *n = & a[f];
        while(*p<0 || n <= &a[len-1]){
            if(*p==*(p-1)){
                p++;
                continue;
            }
            if(*n==*(n-1)){
                n++;
                continue;
            }
            if(*p * *n ==target){
                count++;
                p++;
                n++;
            } else if(*p * *n < target){
                n++;
            }else if(*p * *n > target){
                p++;
            }
        }
    }else{
        if(f>1){
            int *n = &a[f-1];
            while(n>p){
                if(*p==*(p-1)){
                    p++;
                    continue;
                }
                if(*n==*(n+1)){
                    n--;
                    continue;
                }
                if(*p * *n ==target){
                    count++;
                    p++;
                    n--;
                } else if(*p * *n < target){
                    n--;
                }else if(*p * *n > target){
                    p++;
                }
            }
        }
        if(z>1){
            int *n = &a[len-1];
            p = &a[f];
            while(n>p){
                if(*p==*(p-1)){
                    p++;
                    continue;
                }
                if(*n==*(n+1)){
                    n--;
                    continue;
                }
                if(*p * *n == target){
                    count++;
                    p++;
                    n--;
                } else if(*p * *n > target){
                    n--;
                }else if(*p * *n < target){
                    p++;
                }
            }
        }
    }
    cout<<count;
    return 0;
}