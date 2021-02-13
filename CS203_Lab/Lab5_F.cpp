#include <bits/stdc++.h>

using namespace std;
char arr[26];
int nxt[500010], len;
string str;

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
int KmpSearch(string s, string p) {
    int i = 0;int j = 0;int m = 0;
    int sLen = s.length();
    int pLen = p.length();
    while (i < sLen && j < pLen) {
        if (j == -1 || s[i] == p[j]) {
            i++;j++;
        } else {
            j = nxt[j];
        }
        m = j;
    }
    return m;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    for (int i = 0;i < 26;i++){
        char tmp;
        cin >> tmp;
        arr[tmp-97] = 'a' + i;
    }
    cin >> str;
    len = str.length();
    for (int i = 0; i < len / 2; i++) {
        str[i] = arr[str[i]-97];
    }
    string l = str.substr(0,len/2);
    string r = str.substr(len/2,len-len/2);
    GetNext(l,nxt);
    cout<<len-KmpSearch(r,l);
}