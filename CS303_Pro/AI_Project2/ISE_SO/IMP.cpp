#include <cstring>
#include <iostream>
#include <vector>
#include <queue>
#include <set>
#include <ctime>
#include <map>
#include <algorithm>

using namespace std;

const int maxn = 1e6 + 5;
int node_number = 0;
int edge_number = 0;
struct Edge {
    int to;
    double w;
};
double threshold[maxn];
double in_weight[maxn];
vector<int> seeds_set;
vector<Edge> graph[maxn];

set<int> acted;
int u, v;
double w;

vector<int> ise_ic() {
    queue<int> act_list;
    vector<int> rr;
    acted.clear();
    int seed = (rand() % (node_number - 1 + 1)) + 1;
    act_list.push(seed);
    rr.push_back(seed);
    acted.insert(seed);
    while (!act_list.empty()) {
        int cur_seed = act_list.front();
        for (Edge edge : graph[cur_seed]) {
            if (acted.count(edge.to))continue;
            double new_weight = rand() / double(RAND_MAX);
            if (new_weight <= edge.w) {
                act_list.push(edge.to);
                rr.push_back(edge.to);
                acted.insert(edge.to);
            }
        }
        act_list.pop();
    }
    return rr;
}

vector<int> ise_lt() {
    queue<int> act_list;
    vector<int> rr;
    acted.clear();
    memset(in_weight, 0, sizeof(double) * (node_number + 1));
    memset(threshold, -1, sizeof(double) * (node_number + 1));
    int seed = (rand() % (node_number - 1 + 1)) + 1;
    act_list.push(seed);
    acted.insert(seed);
    while (!act_list.empty()) {
        int cur_seed = act_list.front();
        rr.push_back(cur_seed);
        for (Edge edge : graph[cur_seed]) {
            if (!acted.count(edge.to)) {
                in_weight[edge.to] += edge.w;
                if (threshold[edge.to] == -1)threshold[edge.to] = rand() / double(RAND_MAX);
                if (in_weight[edge.to] >= threshold[edge.to] || threshold[edge.to] == 0) {
                    act_list.push(edge.to);
                    acted.insert(edge.to);
                }
            }
        }
        act_list.pop();
    }
    return rr;
}

//void generate_seeds(char *str){
//    while(!seeds_set.empty())seeds_set.pop_back();
//    char* tmp_str;
//    const string delim = "\n";
//    char* p = strtok_r(str, delim.c_str(), &tmp_str);
//    do{
//        seeds_set.push_back(atoi(p));
//    }while((p = strtok_r(nullptr, delim.c_str(), &tmp_str)) != nullptr);
//}
void set_single_int_seed(int num) {
    while (!seeds_set.empty())seeds_set.pop_back();
    seeds_set.push_back(num);
}

void getGraph(char *network) {
    freopen(network, "r", stdin);
    scanf("%d %d", &node_number, &edge_number);
    for (int i = 0; i <= node_number; i++) {
        while (!graph[i].empty())graph[i].pop_back();
    }
    for (int i = 0; i < edge_number; ++i) {
        scanf("%d%d%lf", &u, &v, &w);
        graph[v].push_back(Edge{u, w});
    }
    fclose(stdin);
}

struct Node {
    int len;
    int key;
    int idx;

    bool operator<(const Node &a) const {
        return len < a.len; //大顶堆
    }
};
long long run_limit;
vector<vector<int>> ans_R;
extern "C" {
int c_imp(char *path, int k, char *model, int time_limit) {
    // time_t begin_time = time(0);
    // cout << "begin"<<endl;
    srand(time(0));
    getGraph(path);
    ans_R.clear();
    int run_cnt = 0;
    // cout << "init finish"<<endl;
    if(node_number > 75000){
        run_limit = 200000;
    }else if(node_number >35000){
        run_limit = 400000;
    }else{
        run_limit = 1500000;
    }
    while (run_cnt < run_limit) {
        if (!strcmp(model, "IC")) {
            ans_R.push_back(ise_ic());
        } else {
            ans_R.push_back(ise_lt());
        }
        run_cnt++;
        // cout << "run count:"<< run_cnt <<endl;
    }
    // cout << "ise finish"<<endl;
    // time_t select_time = time(0);
    priority_queue<Node> q;
    vector<int> ans;
    map<int, set<int>> rr_dict;
    int R_len = ans_R.size();
    for (int i = 0; i < R_len; i++) {
        vector<int> rr = ans_R[i];
        for (int u : rr) {
            if (!rr_dict.count(u)) {
                set<int> temp;
                rr_dict.emplace(u, temp);
            }
            rr_dict[u].insert(i);
        }
    }
    for (auto &it : rr_dict) {
        q.push(Node{static_cast<int>((it.second).size()), it.first, 0});
    }
    int curr_idx = 0;
    set<int> covered_seeds;
    while (curr_idx < k) {
        Node now = q.top();
        q.pop();
        set<int> diff;
        set_difference(rr_dict[now.key].begin(), rr_dict[now.key].end(), covered_seeds.begin(), covered_seeds.end(),
                       inserter(diff, diff.begin()));
        rr_dict[now.key] = diff;
        if (curr_idx == now.idx) {
            ans.push_back(now.key);
            curr_idx++;
            set<int> temp;
            set_union(rr_dict[now.key].begin(), rr_dict[now.key].end(), covered_seeds.begin(), covered_seeds.end(),
                      inserter(temp, temp.begin()));
            covered_seeds = temp;
        } else {
            now.len = rr_dict[now.key].size();
            now.idx = curr_idx;
            q.push(now);
        }
    }
    // cout << "node selection finish:" << time(0) - select_time<<endl;
    for (int ans_v : ans) {
        cout << ans_v <<endl;
    }
    // cout << "time use: "<<time(0) - begin_time <<endl;
    return 0;
}
}
