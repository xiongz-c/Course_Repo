#include <bits/stdc++.h>

using namespace std;
struct Node{
    int c;
    int num;
    Node* fa;
    bool operator < (const Node &x)const{
        return x.num < num;
    }
}num[100];


int t, str_len, fa_index;
int val[100];
long long ans;
string str;

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);
    cin >> t;
    for (int i = 1; i <= 99; ++i) num[i].c = i + 96;
    while(t--){
        cin>>str;
        fa_index = 27;
        str_len = str.size();
        for (int i = 0; i < str_len; ++i) {
            num[str[i] - 96].num++;
        }
        priority_queue<Node> q;
        for(int i = 1;i <= 26;i++){
            if(num[i].num)q.push(num[i]);
        }
        if(q.size()==1){
            ans = q.top().num;
            cout<<ans<<"\n";
            ans = 0;
            while(!q.empty())q.pop();
            for (int i = 1; i <= 99; ++i) {
                num[i].num = 0,val[i] = 0,num[i].fa = nullptr;
            }
            continue;
        }
        while(!q.empty()){
            int tmp1 = q.top().c - 96;q.pop();
            if(q.empty())break;
            int tmp2 = q.top().c - 96;q.pop();
            num[fa_index].num = num[tmp1].num + num[tmp2].num;
            num[tmp1].fa = &num[fa_index];
            num[tmp2].fa = &num[fa_index];
            q.push(num[fa_index]);
            fa_index++;
        }
        for (int i = 1; i <= 26; ++i) {
            val[i] = -1;
        }
        for(int i = 1;i <= 26;i++){
            Node* pr = &num[i];
            while(pr != nullptr ){
                val[i]++;
                pr = pr->fa;
            }
        }
        for (int i = 1; i <= 26; ++i) {
            ans += num[i].num * val[i];
        }
        cout<<ans<<"\n";
        ans = 0;
        while(!q.empty())q.pop();
        for (int i = 1; i <= 99; ++i) {
            num[i].num = 0,val[i] = 0,num[i].fa = nullptr;
        }
    }

    return 0;
}