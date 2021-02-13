#include <bits/stdc++.h>
using namespace std;
int n,arr[20000010];
int main(){
    scanf("%d",&n);
    int l = 0,r = 0,num;
    char c[10];
    for(int i = 0;i < n;i++){
        scanf("%s",c);
        if(c[0]=='E'){
            scanf("%d",&num);arr[r++] = num;
        }else if(c[0]=='A'){
            if(l!=r)printf("%d\n",arr[l]);
        }else l++;
    }
    while(l < r)printf("%d ", arr[l++]);
    return 0;
}