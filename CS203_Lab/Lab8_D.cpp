#include <bits/stdc++.h>
#pragma GCC optimize(2)

using namespace std;

int t, n, m, a, b, sum = 0;
struct Node{
    bool val = false;
    bool run = false;
    vector<int> tail;
}nodes[(int)2e5 + 10];

void bfs(int node)
{
    queue<int> q;
    q.push(node);
    nodes[node].run = true;
    while(!q.empty())
    {
        for (int nums : nodes[q.front()].tail)
        {
            if(nodes[nums].run)continue;
            nodes[nums].val = !nodes[q.front()].val;
            q.push(nums);
            nodes[nums].run = true;
        }
        if(nodes[q.front()].val)
        {
            sum++;
        }
        q.pop();
    }
}
int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

inline void write(int x)
{
    if(x<0){
        putchar('-');
        x=-x;
    }
    if(x>9)
        write(x/10);
    putchar(x%10+'0');
}

int main(){
    t = getint();
    while(t--)
    {
        n = getint();m = getint();
        for(int i = 0;i < m;i++)
        {
            a = getint();b = getint();
            nodes[a].tail.push_back(b);
            nodes[b].tail.push_back(a);
        }
        bfs(1);
        if(sum*2 > n)
        {
            sum = n - sum;
            write(sum);printf("\n");
            for(int i = 1; i <= n;i++)
            {
                if(!nodes[i].val)write(i),printf(" ");
            }
        }else{
            write(sum);printf("\n");
            for(int i = 1; i <= n;i++)
            {
                if(nodes[i].val)write(i),printf(" ");
            }
        }
        sum = 0;
        n++;
        while(n--)
        {
            nodes[n].val = false;
            nodes[n].run = false;
            nodes[n].tail.clear();
        }
        printf("\n");
    }
}