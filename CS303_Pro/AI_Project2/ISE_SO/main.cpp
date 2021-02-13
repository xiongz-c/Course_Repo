#include <cstring>
#include <iostream>
#include <vector>
#include <queue>
using namespace std;

const int maxn = 1e6 + 5;
int node_number = 0;
int edge_number = 0;
struct Edge {
    int to;
    double w;
};
int acted[maxn];
double threshold[maxn];
double in_weight[maxn];
vector<int> seeds_set;
vector<Edge> graph[maxn];

int u, v;
double w;

int ise_ic() {
    queue<int> act_list;
    memset(acted, 0, sizeof(int)*(node_number+1));
    for (int e : seeds_set) {
        act_list.push(e);
        acted[e] = 1;
    }
    while (!act_list.empty()) {
        int cur_seed = act_list.front();
        for (Edge edge : graph[cur_seed]) {
            if (acted[edge.to] == 1)continue;
            double new_weight = rand() / double(RAND_MAX);
            if (new_weight <= edge.w) {
                act_list.push(edge.to);
                acted[edge.to] = 1;
            }
        }
        act_list.pop();
    }
    int count = 0;
    for (int i = 0; i <= node_number; ++i) {
        if (acted[i]){
            count++;
        }
    }
    return count;
}

int ise_lt() {
    queue<int> act_list;
    memset(in_weight, 0, sizeof(double) * (node_number+1));
    memset(acted, 0, sizeof(int)*(node_number+1));
    memset(threshold, -1, sizeof(double)*(node_number+1));
    for (int e : seeds_set) {
        act_list.push(e);
        acted[e] = 1;
    }
    while (!act_list.empty()) {
        int cur_seed = act_list.front();
        for (Edge edge : graph[cur_seed]) {
            if (acted[edge.to] == 0) {
                in_weight[edge.to] += edge.w;
                if(threshold[edge.to]==-1)threshold[edge.to]  = rand() / double(RAND_MAX);
                if (in_weight[edge.to] >= threshold[edge.to] || threshold[edge.to]==0) {
                    act_list.push(edge.to);
                    acted[edge.to] = 1;
                }
            }
        }
        act_list.pop();
    }
    int count = 0;
    for (int i = 0; i <= node_number; ++i) {
        if (acted[i])count++;
    }
    return count;
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
void set_single_int_seed(int num){
    while(!seeds_set.empty())seeds_set.pop_back();
    seeds_set.push_back(num);
}

void getGraph(char *network) {
    freopen(network, "r", stdin);
    scanf("%d %d", &node_number, &edge_number);
    for(int i =0;i <= node_number;i++ ){
        while(!graph[i].empty())graph[i].pop_back();
    }
    for (int i = 0; i < edge_number; ++i) {
        scanf("%d%d%lf", &u, &v, &w);
        graph[v].push_back(Edge{u,w});
    }
    fclose(stdin);
}


extern "C" {
int init(char *network) {
    getGraph(network);
    return 1;
}
void set_seeds(int seed){
    set_single_int_seed(seed);
}
string res;
char* cal( int seed, char *m){
    res = "";
    srand(seed);
    if (!strcmp(m,"IC")){
        ise_ic();
        for (int i = 1; i <= node_number; ++i)
        {
            if(acted[i]==1)res.append(to_string(i) + " ");
        }
        return const_cast<char *>(res.c_str());
    }else if(!strcmp(m,"LT")){
        ise_lt();
        for (int i = 1; i <= node_number; ++i)
        {
            if(acted[i]==1)res.append(to_string(i) + " ");
        }
        return const_cast<char *>(res.c_str());
    }
    return nullptr;
}
}
// int tmp;
//int main() {
//    time_t now = clock();
//    srand(time(nullptr));
//    char *network = "network.txt";
//
//    ifstream infile;
//    infile.open("./seeds.txt");
//    int tmp;
//    while (!infile.eof()) {
//        infile >> tmp;
//        seeds_set.push_back(tmp);
//    }
//    infile.close();
//    init(network, 0, network);
//    int ans = 0;
//    for (int i = 0; i < 1000000; ++i) {
//        ans += ise_ic();
//    }
//    cout << "ans=" << ans / 1000000.0 << endl;
//    cout << "time:" << clock() - now ;
//    return 0;
//}
