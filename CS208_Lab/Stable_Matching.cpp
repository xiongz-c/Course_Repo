#include <bits/stdc++.h>

const int name_len = 10;
const int MAXN = 2e6;
using namespace std;
int t;
struct Node{
    string name;
    int girl_num{ };
    int index = -1;
    struct Node* next = nullptr;
    struct Node* prev = nullptr;
};


char boys_list[1010][name_len];
char girls_list[1010][name_len];

Node boys_prefer[MAXN];
vector< int >girls_prefer[MAXN];
int boyfriend[MAXN];
int girlfriend[MAXN];
char boys_name[MAXN][name_len];
char girls_name[MAXN][name_len];

stack<int> free_boy;

int hash_name(char name[]){
    hash<string> hash_fn;
    size_t str_hash = hash_fn(name) % 1999957;
    return str_hash;
}

void find_girl(int boy_num){
    if(&boys_prefer[boy_num] == nullptr)return;
    int now_girl = boys_prefer[boy_num].next->girl_num;
    boys_prefer[boy_num] = *boys_prefer[boy_num].next;
    if(!boyfriend[now_girl]){
        boyfriend[now_girl] = boy_num;
        girlfriend[boy_num] = now_girl;
        free_boy.pop();
    } else{
        vector<int>::iterator iter;
        iter = find(girls_prefer[now_girl].begin(), girls_prefer[now_girl].end(), boyfriend[now_girl]);
        int now_bf = iter - girls_prefer[now_girl].begin();
        iter = find(girls_prefer[now_girl].begin(), girls_prefer[now_girl].end(), boy_num);
        int that_boy = iter - girls_prefer[now_girl].begin();
        if(now_bf < that_boy){
            return ;
        } else{
            free_boy.pop();
            free_boy.push(boyfriend[now_girl]);
            boyfriend[now_girl] = boy_num;
            girlfriend[boy_num] = now_girl;

        }
    }
}

int main(){

    ios::sync_with_stdio(false);
    cin.tie(0);
    cin >> t;
    for (int i = 1; i <= t; ++i) {
        cin >> boys_list[i];
        int boy_num = hash_name(boys_list[i]);
        for (int k = 0; k < name_len; ++k){
            boys_name[boy_num][k] = boys_list[i][k];
        }
        free_boy.push(boy_num);
        boys_prefer[boy_num].name = boys_list[i];
        boys_prefer[boy_num].index = 1;
        Node *tmp = &boys_prefer[boy_num];
        for (int j = 1; j <= t; ++j) {
            char tmp_name[name_len];
            cin >> tmp_name;
            Node *now = new Node();
            now->name = tmp_name;
            now->girl_num = hash_name(tmp_name);
            now->index = j;
            tmp->next = now;
            tmp = tmp->next;
        }
    }
    for (int i = 1; i <= t; ++i) {
        cin >> girls_list[i];
        int girl_num = hash_name(girls_list[i]);
        for (int k = 0; k < name_len; ++k){
            girls_name[girl_num][k] = girls_list[i][k];
        }
        for (int j = 1; j <= t; ++j) {
            char tmp_name[name_len];
            cin >> tmp_name;
            int boy_num = hash_name(tmp_name);
            girls_prefer[girl_num].push_back(boy_num);
        }
    }
    while(!free_boy.empty()){
        int now_boy = free_boy.top();
        find_girl(now_boy);
    }
    for (int i = 1;i <= t;i++) {
        cout << boys_list[i]<<" ";
        int theBoy = hash_name(boys_list[i]);
        cout << girls_name[girlfriend[theBoy]] << "\n";
    }
    return 0;
}