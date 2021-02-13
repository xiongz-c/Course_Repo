#include <bits/stdc++.h>
using namespace std;
int t, cnt = 0, nxt[10010];
string str1,str2;
void GetNext(string p,int next[])
{
    int pLen = p.length();
    next[0] = -1;int k = -1;int j = 0;
    while (j < pLen - 1)
    {
        if (k == -1 || p[j] == p[k]) ++k,++j,next[j] =k;
        else k = next[k];
    }
}
bool KmpSearch(string s, string p) {
    int i = 0;int j = 0;int m = 0;
    int sLen = s.length();
    int pLen = p.length();
    while (i < sLen && j < pLen) {
        if (j == -1 || s[i] == p[j]) {
            i++;j++;
        } else {
            j = nxt[j];
        }
        m = max(j,m);
        if(m >= pLen/3)return true;
    }
    return false;
}
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>t;
    while(t--){
        cin>>str1>>str2;
        GetNext(str1,nxt);
        if(KmpSearch(str2,str1))cnt++;
    }
    cout<<cnt;
    return 0;
}