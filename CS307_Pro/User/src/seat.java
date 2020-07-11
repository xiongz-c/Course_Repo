import javafx.collections.ObservableList;

import java.util.ArrayList;

public class seat {
    public void setSeat(ArrayList<String> in , ArrayList<String> data){
        for(int i = 0 ; i < in.size() ; i++){
            switch(in.get(i)){
                case "WZ":
                    data.add("无座");
                    break;
                case "F":
                    data.add("动卧");
                    break;
                case "M":
                    data.add("一等座");
                    break;
                case "0":
                    data.add("二等座");
                    break;
                case "3":
                    data.add("硬卧");
                    break;
                case "4":
                    data.add("软卧");
                    break;
                case "9":
                    data.add("商务座");
                    break;
            }
        }
    }

    public void setSeat(ArrayList<String> in , ObservableList<String> data){
        for(int i = 0 ; i < in.size() ; i++){
            switch(in.get(i)){
                case "WZ":
                    data.add("无座");
                    break;
                case "F":
                    data.add("动卧");
                    break;
                case "M":
                    data.add("一等座");
                    break;
                case "0":
                    data.add("二等座");
                    break;
                case "3":
                    data.add("硬卧");
                    break;
                case "4":
                    data.add("软卧");
                    break;
                case "9":
                    data.add("商务座");
                    break;
            }
        }
    }

    public void setPrice(ArrayList<String> in , ArrayList<String> price){

    }
}
