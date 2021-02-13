#include <iostream>
#include <queue>

#pragma GCC optimize(2)

using namespace std;
const int MAXN = 4e5+10;
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}
struct Node{
    int out;
    vector<int> nxt;
}nodes[MAXN];
int t,m,n,u,v,siz;
int result[MAXN];
int main(){
    t = getint();
    while(t--)
    {
        siz = 0;
        n = getint();m = getint();
        for(int i = 0;i < m;i++)
        {
            u = getint();v = getint();
            nodes[v].nxt.push_back(u);
            nodes[u].out++;
        }
        priority_queue<int> head;
        for(int i = 1;i <= n;i++){
            if(!nodes[i].out)
            {
                head.push(i);
            }
        }
        while(!head.empty())
        {
            int tmp = head.top();
            head.pop();
            result[siz++] = tmp;
            for(int e : nodes[tmp].nxt)
            {
                if(!nodes[e].out)continue;
                nodes[e].out--;
                if(!nodes[e].out)head.push(e);
            }
        }

        while(siz--)printf("%d ",result[siz]);
        for(int i = 1;i <= n;i++)
        {
            nodes[i].nxt.clear();
        }
    }
    return 0;
}