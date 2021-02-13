//逆序拓扑+dp累加b
#include <iostream>
#include <vector>
#include <queue>
#pragma GCC optimize(2)
#define ll long long

using namespace std;

const int MAXN = 1e5+100;
const int mod = 1e9+7;
vector<int> in[MAXN];
ll a[MAXN],b[MAXN],out[MAXN],ans[MAXN],sum;
int t,n,m,u,v;

int getint(){
    char c;int flag = 1,num = 0;
    while((c=getchar())<'0'||c>'9')if(c=='-')flag=-1;
    while(c>='0'&&c<='9'){num=num*10+c-48;c=getchar();}
    return num*=flag;
}

int main()
{
    t = getint();
    while (t--)
    {
        n = getint();m = getint();
        for(int i = 1;i <= n;i++)ans[i] = 0,in[i].clear();;
        for(int i = 1;i <= n;i++)a[i] = getint(),b[i] = getint();
        for(int i = 1;i <= m;i++)
        {
            u = getint();v = getint();
            in[v].push_back(u);//入度+1
            out[u]++;//记录出度
        }
        queue<int>q;
        for(int i = 1;i<=n;i++) if(out[i]==0)q.push(i);
        while(!q.empty())
        {
            int tmp = q.front();q.pop();
            for(int ii : in[tmp])
            {
                ans[ii] = (ans[ii]  + (ans[tmp]  + b[tmp] % mod) % mod) % mod;
                out[ii]--;
                if(out[ii]==0)q.push(ii);
            }
        }
        sum = 0;
        for(int i = 1;i<=n;i++) sum = (sum + ans[i] * a[i] % mod )%mod;
        printf("%lld\n",sum);
    }
    return 0;
}