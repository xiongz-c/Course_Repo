#include <bits/stdc++.h>

#define ll unsigned long long
const ll Q = 998244353;
const int MAXN = 400010;
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

int num_len[MAXN], sum_len[11], n, len;
ll nums[MAXN],total_sum = 0,tmp_num = 0,step1 = 0,step2 = 0, tmp = 0,tail = 0;

ll get_MIX(ll number){
    ll sum = 0;
    for (int k = 1; k < 11; ++k) {
        tmp_num = number%10;
        sum = (sum + (ll)(pow(10,2*k-1)+pow(10,2*k-2)) % Q * tmp_num) % Q ;
        number /= 10;
        if(!number)break;
    }
    return sum % 998244353;
}

int main() {
    n = getint();
    for (int i = 1; i <= n; ++i) {
        nums[i] = tmp = getint();
        len = 1;
        while (tmp /= 10)len++;
        sum_len[len]++;
        num_len[i] = len;
    }
    for (int i = 1; i <= n; ++i) {
        step1 = step2 = 0;
        tmp = nums[i];
        ll sol = get_MIX(tmp);
        for (int j = num_len[i]; j < 11; ++j) step1 = (step1 + sol * sum_len[j] )%Q;

        for (int j = 1; j < num_len[i]; ++j) {
            tail = tmp % (ll)pow(10,j);
            step2 = (step2 + get_MIX(tail) * sum_len[j]) % Q;
            step2 = (step2 + (tmp - tail)*(ll)pow(10,j) * 2 % Q * sum_len[j] % Q) % Q;
        }
        total_sum = (total_sum+step1+step2) % Q;
    }
    printf("%lld",total_sum % Q);
    return 0;
}