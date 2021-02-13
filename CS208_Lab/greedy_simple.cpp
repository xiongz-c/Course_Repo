#include <bits/stdc++.h>

using namespace std;

int getint() {
    char c;
    int flag = 1, num = 0;
    while ((c = getchar()) < '0' || c > '9')if (c == '-')flag = -1;
    while (c >= '0' && c <= '9') {
        num = num * 10 + c - 48;
        c = getchar();
    }
    return num *= flag;
}

const int MAXN = 1e6+10;
int T,n,tail,cnt;
struct Node{
    int start,end;
}nodes[MAXN];

bool cmp(struct Node a,struct Node b){
    if(a.end!=b.end){
        return a.end < b.end;
    }else{
        return a.start < b.start;
    }
}

int main(){
    T = getint();
    while (T--){
        n = getint();
        for(int i = 0;i < n;i++){
            nodes[i].start = getint();
            nodes[i].end = getint();
        }
        sort(nodes,nodes+n,cmp);
        tail = nodes[0].end,cnt = 1;
        for (int i = 0; i < n; ++i) {
            if(nodes[i].start > tail){
                cnt++;
                tail = nodes[i].end;
            }
        }
        printf("%d\n",cnt);
    }
    return 0;
}