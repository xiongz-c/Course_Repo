#include <iostream>
using namespace std;
int main() {
    int times = 0;
    cin>>times;
    while(times-->0){
        for(int i = 0 ; i < 9;i++){
            char str;
            cin >> str;
            if(i==4){
                cout<<str<<endl;
                break;
            }
        }
    }
    return 0;
}