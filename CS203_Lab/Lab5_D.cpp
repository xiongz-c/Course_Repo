#include <bits/stdc++.h>
using namespace std;
const int MAXN = 2e5+100;
int t, nxt[MAXN], pLen;
string str;
int GetNext(string p,int next[]){
    pLen = p.length();
    next[0] = -1;int k = -1;int j = 0;
    while (j < pLen - 1)
    {
        if (k == -1 || p[j] == p[k]) ++k,++j,next[j] =k;
        else k = next[k];
    }
    return next[pLen-1];
}
int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>str;
        GetNext(str+'*',nxt);
        int len ;
        len = pLen - nxt[pLen - 1]-1;
        if(nxt[pLen - 1] % len != 0  )cout<< len - nxt[pLen - 1] % len <<endl;
        else {
            if(len <= nxt[pLen - 1])cout<< 0 <<endl;
            else cout<< len <<endl;
        }
    }
    return 0;
}