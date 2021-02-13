#include <bits/stdc++.h>
#define ull unsigned long long
using namespace std;
string str1,str2;
int s1len,s2len,seed = 133;

ull djzhash(string str){//hash
    ull hash = 0;
    int len = str.length();
    for(int i = 0;i < len;i++){
        hash = hash * seed + (ull)str[i];
    }
    return hash;
}
unsigned long long  quick_pow(int a,int n)//快速幂
{
    long long sum = 1;
    for(int i = 0 ; i < n ; i++){
        sum = sum * a;
    }
    return sum;
}

bool sameStr(int len){//判断相等，on
    ull q = quick_pow(seed,len-1);
    ull s1 = djzhash(str1.substr(0,len));
    ull s2 = djzhash(str2.substr(0,len));
    unordered_map<ull,string> substr;
    substr[s1] = "s";
    for(int i = 1;i < s1len - len + 1;i++) {
        s1 = (s1 - str1[i - 1] * q)*seed + str1[len+i - 1];
        substr[s1] = "s";
    }
    for (int i = 1; i < s2len - len + 1; i++) {
        if(substr.find(s2) != substr.end())return true;
        s2 = (s2 - str2[i - 1]*q)*seed + str2[len+i-1];
    }
    return false;
}

int binarySearch(int l, int r){
    int mid ;
    while(l<r) {
        mid = l + (r - l)/2;
        if (mid==0 || sameStr(mid)) {
            l = mid+1;
        } else r = mid;
    }
    return l;
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin>>str1>>str2;
    s1len = str1.length();
    s2len = str2.length();
    if(s2len < s1len){
        string tmp;int tmp2;
        tmp = str1;
        str1 = str2;
        str2 = tmp;
        tmp2 = s1len;
        s1len = s2len;
        s2len = tmp2;
    }
    int result = binarySearch(0,s1len+1);
    cout<<result-1;
    return 0;
}