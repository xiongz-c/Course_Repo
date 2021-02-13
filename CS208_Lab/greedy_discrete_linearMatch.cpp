#include <bits/stdc++.h>
#define ll long long
#define MAXN 5010
#define INF 1e8 +100
using namespace std;

int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

struct Node{
    int s, t, v;
}nodes[MAXN];

bool cmp_s(const Node &x,const Node &y){
    if(x.s!=y.s)return x.s < y.s;
    else return x.t > y.t;
}
bool cmp_v(const Node &x,const Node &y){return x.v > y.v;}

int n,k,s_x,now_node,new_x;
ll ans;
bool dot[int(INF)];
int dot_v[int(INF)];

bool find(int i, int x){
    if(x > nodes[i].t)return false;
    if(dot[x] && !dot_v[x]){
        dot_v[x] = i;
        return true;
    }
    now_node = dot_v[x];
    new_x = x + 1;
    while(!dot[new_x])new_x++;
    if( nodes[now_node].t < nodes[i].t ){
        return find(i,new_x);
    }else{
        if(find(now_node,x+1)){
            dot_v[x] = i;
            return true;
        }else return false;
    }
}

int main(){
    n = getint();
    for(int i = 1;i <= n;++i){
        nodes[i].s = getint();
        nodes[i].t = getint();
        nodes[i].v = getint();
    }
    sort(nodes + 1,nodes + n + 1,cmp_s);
    k = 0;
    for(int i = 1;i <= n;++i){
        k = max(k+1,nodes[i].s);
        dot[k] = true;
//        printf("%d ", k);
    }
//    printf("\n");
    sort(nodes + 1,nodes + n + 1,cmp_v);
    for(int i = 1;i <= n;++i){
        s_x = nodes[i].s;
        while(!dot[s_x])s_x++;
//        printf("%d ", s_x);
        find(i,s_x);
    }
//    printf("\n");
    ans = 0;
    for(int i = 1;i <= INF;++i){
        if(dot[i] && dot_v[i]){
            ans += nodes[dot_v[i]].v;
//            printf("%d ", dot_v[i]);
        }
    }
//    printf("\n");
    printf("%lld", ans);
    return 0;
}