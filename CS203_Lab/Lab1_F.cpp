#include <iostream>

using namespace std;

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    int times = 0;
    cin>>times;
    while(times-->0){
        long long n = 0;
        cin>>n;
        long long result = 0;
        long long tmp = n/5;
        while(tmp != 0){
            result += tmp;
            tmp /= 5;
        }
        cout<<result<<"\n";
    }
}