import com.sun.deploy.security.SelectableSecurityManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.omg.CORBA.TIMEOUT;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.security.krb5.internal.Ticket;

import javax.management.remote.JMXConnectionNotification;
import javax.print.Doc;
import javax.sound.sampled.Line;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.IntToDoubleFunction;


public class tickController {

    Main main = new Main();

    public class Information {
        private SimpleStringProperty start_time, start_station, end_time, end_station, train_no , date , end_date;

        Information(String start_time, String start_station, String end_time, String end_station, String train_no , String date , String end_date) {
            this.start_station = new SimpleStringProperty(start_station);
            this.start_time = new SimpleStringProperty(start_time);
            this.end_time = new SimpleStringProperty(end_time);
            this.end_station = new SimpleStringProperty(end_station);
            this.train_no = new SimpleStringProperty(train_no);
            this.date = new SimpleStringProperty(date);
            this.end_date = new SimpleStringProperty(end_date);
        }

        public void setEnd_date(String end_date) {
            this.end_date.set(end_date);
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public void setStart_station(String start_station) {
            this.start_station.set(start_station);
        }

        public void setStart_time(String start_time) {
            this.start_time.set(start_time);
        }

        public void setEnd_station(String end_station) {
            this.end_station.set(end_station);
        }

        public void setEnd_time(String end_time) {
            this.end_time.set(end_time);
        }

        public void setTrain_no(String train_no) {
            this.train_no.set(train_no);
        }

        public String getEnd_date() {
            return end_date.get();
        }

        public String getStart_time() {
            return start_time.get();
        }

        public String getDate() {
            return date.get();
        }

        public String getEnd_station() {
            return end_station.get();
        }

        public String getStart_station() {
            return start_station.get();
        }

        public String getEnd_time() {
            return end_time.get();
        }

        public String getTrain_no() {
            return train_no.get();
        }
    }

    @FXML
    private TextField user_name;

    @FXML
    private TextField user_id;

    @FXML
    private ChoiceBox<String> search_start_station;

    @FXML
    private ChoiceBox<String> search_end_station;

    @FXML
    private TextField start_city;

    @FXML
    private TextField end_city;

    @FXML
    private TextField date;

    @FXML
    private ChoiceBox<String> level;

    @FXML
    public static Stage dialogStage;

    @FXML
    private TableView<Information> table;

    @FXML
    private TableColumn<Information, Information> train_no;

    @FXML
    private TableColumn<Information, Information> start_time;

    @FXML
    private TableColumn<Information, Information> start_station;

    @FXML
    private TableColumn<Information, Information> end_station;

    @FXML
    private TableColumn<Information , Information> end_time;

    @FXML
    private TableColumn<Information , Information> end_date;

    private final ObservableList<String> seat_level = FXCollections.observableArrayList();

    private final ObservableList<Information> data = FXCollections.observableArrayList();


    public ArrayList<String> rest_ticket = new ArrayList<>();
    public ArrayList<String> a_seat_level = new ArrayList<>();
    public ArrayList<String> a_price = new ArrayList<>();
    public ArrayList<String> seat_id = new ArrayList<>();

    public void setDialogStage(Stage dialogStage) {
        start_city.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_start_station.getItems().clear();
                Connection con = Jsoup.connect("http://localhost:8081/32106/Station/query/city").ignoreContentType(true);
                con.data("city", start_city.getText());
                try {
                    Document doc = con.post();
                    if (doc.body().text().length() != 2) {
                        String info[] = doc.body().text().split("\\},\\{");
                        for (int i = 0; i < info.length; i++) {
                            int index = info[i].indexOf(",\"name\":");
                            int next_index = info[i].indexOf("\",\"code\"");
                            search_start_station.getItems().add(info[i].substring(index + 9, next_index));
                        }
                        search_start_station.getItems().add("无站");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        end_city.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                search_end_station.getItems().clear();
                Connection con = Jsoup.connect("http://localhost:8081/32106/Station/query/city").ignoreContentType(true);
                con.data("city", end_city.getText());
                try {
                    Document doc = con.post();
                    if (doc.body().text().length() != 2) {
                        String info[] = doc.body().text().split("\\},\\{");
                        for (int i = 0; i < info.length; i++) {
                            int index = info[i].indexOf(",\"name\":");
                            int next_index = info[i].indexOf("\",\"code\"");
                            search_end_station.getItems().add(info[i].substring(index + 9, next_index));
                        }
                        search_end_station.getItems().add("无站");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1){
                seat_level.clear();
                a_price.clear();
                a_seat_level.clear();
                rest_ticket.clear();
                seat_id.clear();
                if (Main.PhoneNumber == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("报错");
                    alert.setHeaderText("打开失败：");
                    alert.setContentText("请先登录！");
                    alert.showAndWait();
                    return;
                }
                Information ticket = table.getSelectionModel().getSelectedItem();
                Connection con = Jsoup.connect("http://localhost:8081/32106/Seat/query/leftTicket").ignoreContentType(true);
                con.data("start_station" , ticket.getStart_station());
                con.data("dest_station" , ticket.getEnd_station());
                con.data("train_name" , ticket.getTrain_no());
                con.data("date" , ticket.getDate());
                try {
                    Document doc = con.post();
                    if(doc.body().text().equals("[]")){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("报错");
                        alert.setContentText("此车没有安排座位！");
                        alert.showAndWait();
                        return;
                    }
                    String info[] = doc.body().text().split("],\\[");
                    for(int i = 0 ; i < info.length ; i++){
                        info[i] = info[i].replaceAll("\\[" , "").replaceAll("]" , "");
                        int index = 1;
                        int next_index = info[i].indexOf(",");
                        String id = info[i].substring(index , next_index - 1);
                        index = info[i].indexOf(",");
                        info[i] = info[i].replaceFirst("," , "");
                        next_index = info[i].indexOf(",");
                        String level = info[i].substring(index + 1 , next_index - 1);
                        info[i] = info[i].replaceFirst("," , "");
                        index = next_index;
                        next_index = info[i].indexOf(",");
                        String rest = info[i].substring(index + 1 , next_index - 1);
                        info[i] = info[i].replaceFirst("," , "");
                        index = next_index;
                        next_index = info[i].length();
                        String price = info[i].substring(index + 1 , next_index - 1);
                        a_price.add(price);
                        rest_ticket.add(rest);
                        a_seat_level.add(level);
                        seat_id.add(id);
                    }
                    seat seat = new seat();
                    seat.setSeat(a_seat_level , seat_level);
                    level.setItems(seat_level);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("查找失败");
                    alert.setHeaderText("查找失败：");
                    alert.setContentText("请检查网络！");
                    alert.showAndWait();
                    ex.printStackTrace();
                    return;
                }
            }
        });
        this.dialogStage = dialogStage;
    }

    public void search() {
        data.clear();
        if (Main.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String start_station = search_start_station.getValue();
        String end_station = search_end_station.getValue();
        String start_city = this.start_city.getText();
        String end_city = this.end_city.getText();
        String date = this.date.getText();
        if (start_city.length() * end_city.length() * date.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请先输入全部信息！");
            alert.showAndWait();
            return;
        }
        if(start_station == null || end_station == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请先选择站点！");
            alert.showAndWait();
            return;
        }
        if (date.length() != 10) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查日期格式是否正确！");
            alert.showAndWait();
            return;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date a = format.parse(date);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查日期格式是否正确！");
            alert.showAndWait();
            return;
        }
        Connection con;
        if(start_station.equals(end_station) && start_station.equals("无站")){
            con = Jsoup.connect("http://localhost:8081/32106/Schedule/query/bycity").ignoreContentType(true);
            con.data("city1" , start_city);
            con.data("city2" , end_city);
            con.data("date" , date);
        }else if(start_station.equals("无站") || end_station.equals("无站")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请选择正确的站点！");
            alert.showAndWait();
            return;
        }
        else {
            con = Jsoup.connect("http://localhost:8081/32106/Schedule/query/bystation").ignoreContentType(true);
            con.data("start_station", start_station);
            con.data("dest_station", end_station);
            con.data("date", date);
        }
        try {
            Document doc = con.post();
            if (doc.body().text().equals("[]")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("查找失败");
                alert.setHeaderText("查找失败：");
                alert.setContentText("当天无车辆从" + start_station + "到" + end_station + "!");
                alert.showAndWait();
                return;
            }
            String info[] = doc.body().text().split("],\\[");
            for (int i = 0; i < info.length; i++) {
                info[i] = info[i].replaceAll("\\[", "").replaceAll("]", "");
                int index = 1;
                int next_index = info[i].indexOf(",");
                String train_name = info[i].substring(index , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String start_station0 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String end_station0 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String start_time_1 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String end_time_1 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String start_time_2 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String end_time_2 = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].length();
                String train_date = info[i].substring(index + 1 , next_index - 1);
                int h = Integer.parseInt(start_time_2.substring(0 , 2));
                h = h / 24;
                if(h != 0) {
                    int h1 = Integer.parseInt(start_time_2.substring(0 , 2)) - h * 24;
                    if(h1 >= 10)
                        start_time_2 = (Integer.parseInt(start_time_2.substring(0 , 2)) - h * 24) + start_time_2.substring(2 , 5);
                    else
                        start_time_2 = "0" + (Integer.parseInt(start_time_2.substring(0 , 2)) - h * 24) + start_time_2.substring(2 , 5);
                    Information in;
                    if(h > 1)
                        in = new Information(end_time_1, start_station0, start_time_2, end_station0, train_name, date, "出发后" + h + "日");
                    else
                        in = new Information(end_time_1, start_station0, start_time_2, end_station0, train_name, date, "次日到达");
                        data.add(in);
                }else{
                    Information in = new Information(end_time_1, start_station0, start_time_2, end_station0, train_name, date, "当天可达");
                    data.add(in);
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        train_no.setCellValueFactory(new PropertyValueFactory<>("train_no"));
        start_time.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        end_time.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        end_date.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        this.start_station.setCellValueFactory(new PropertyValueFactory<>("start_station"));
        this.end_station.setCellValueFactory(new PropertyValueFactory<>("end_station"));
        table.setItems(data);
    }

    public void better() {
        if (Main.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String station_station = this.search_start_station.getValue();
        String end_station = this.search_end_station.getValue();
        if(station_station == null || end_station == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请先选择站点！");
            alert.showAndWait();
            return;
        }
        String date = this.date.getText();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date a = format.parse(date);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查日期格式是否正确！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Schedule/query/bystation/recommend").ignoreContentType(true).timeout(500000000);
        con.data("start_station", station_station);
        con.data("dest_station", end_station);
        con.data("date", date);
        try {
            Document doc = con.post();
            if(doc.body().text().equals("[]")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("查找失败");
                alert.setHeaderText("查找失败：");
                alert.setContentText("无中转车辆可到达！");
                alert.showAndWait();
                return;
            }
            String info[] = doc.body().text().split("],\\[");
            ArrayList<Transfer> list = new ArrayList<>();
            for (int i = 0; i < info.length; i++) {
                info[i] = info[i].replaceAll("\\[" , "").replaceAll("]" , "");
                int index = 1;
                int next_index = info[i].indexOf(",");
                String first_station = info[i].substring(index , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String second_station = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String final_station = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String first_train = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String second_train = info[i].substring(index + 1, next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String first_start_time = info[i].substring(index  , next_index );
                first_start_time = first_start_time.substring(0 , first_start_time.length() / 2) + ":" + first_start_time.substring(first_start_time.length() / 2 , first_start_time.length());
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String first_arrive_time = info[i].substring(index , next_index );
                first_arrive_time = first_arrive_time.substring(0 , first_arrive_time.length() / 2) + ":" + first_arrive_time.substring(first_arrive_time.length() / 2 , first_arrive_time.length());
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String second_start_time = info[i].substring(index , next_index );
                second_start_time = second_start_time.substring(0 , second_start_time.length() / 2) + ":" + second_start_time.substring(second_start_time.length() / 2 , second_start_time.length());
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].length();
                String second_arrive_time = info[i].substring(index, next_index );
                second_arrive_time = second_arrive_time.substring(0 , second_arrive_time.length() / 2) + ":" + second_arrive_time.substring(second_arrive_time.length() / 2 , second_arrive_time.length());
                if(first_arrive_time.length() == 5 && Integer.parseInt(first_arrive_time.substring(0 , first_arrive_time.length() / 2 )) < 24){
                    Transfer transfer = new Transfer(first_train , second_train , first_station , second_station , final_station , first_arrive_time , second_arrive_time , first_start_time , second_start_time , date);
                    list.add(transfer);
                }
            }
            dialogStage.hide();
            main.showBetter(list);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void getDetail(){
        if (Main.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        ArrayList<Schedule> schedules = new ArrayList<>();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Schedule/distinct").ignoreContentType(true);
        String train_name = table.getSelectionModel().getSelectedItem().getTrain_no();
        String current_data = table.getSelectionModel().getSelectedItem().getDate();
        con.data("train_name" , train_name);
        con.data("date" , current_data);
        try {
            Document doc = con.post();
            String info[] = doc.body().text().split("],\\[");
            for(int i = 0 ; i < info.length ; i++){
                info[i] = info[i].replaceAll("\\[" , "").replaceAll("]" , "");
                int index = 1;
                int next_index = info[i].indexOf(",");
                String city_name = info[i].substring(index , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String arrive_time = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                info[i] = info[i].replaceFirst("," , "");
                next_index = info[i].indexOf(",");
                String leave_time = info[i].substring(index + 1 , next_index - 1);
                info[i] = info[i].replaceFirst("," , "");
                index = next_index;
                next_index = info[i].indexOf(",");
                String date = info[i].substring(index + 1 , next_index - 1);
                index = next_index;
                next_index = info[i].length();
                String no = info[i].substring(index + 1 , next_index);
                int h1 = Integer.parseInt(arrive_time.substring(0 , 2));
                h1 = h1 / 24;
                int h2 = Integer.parseInt(leave_time.substring(0 , 2));
                h2 = h2 / 24;
                if(h1 > 0)
                    date = "出发时间后" + h1 + "天";
                h1 = Integer.parseInt(arrive_time.substring(0 , 2)) - 24 * h1;
                if(h1 < 10)
                    arrive_time = "0" + h1 + arrive_time.substring(2 , 5);
                else
                    arrive_time = h1 + arrive_time.substring(2 , 5);
                h2 = Integer.parseInt(leave_time.substring(0 , 2)) - 24 * h2;
                if(h2 < 10)
                    leave_time = "0" + h2 + leave_time.substring(2 , 5);
                else
                    leave_time = h2 + leave_time.substring(2 , 5);
                Schedule schedule = new Schedule(city_name , train_name , arrive_time , leave_time , date);
                schedules.add(schedule);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        main.showTicketDetail(a_seat_level , a_price , schedules);
    }

    public void back() {
        dialogStage.close();
        Main.primaryStage.show();
    }

    public void buy(){
        if (Main.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String name = user_name.getText();
        String id = user_id.getText();
        if(name.equals("") || id.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("购买失败：");
            alert.setContentText("请先补全信息！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/query/IDC").ignoreContentType(true);
        con.data("IDC" , id);
        try {
            Document doc = con.post();
            String info = doc.body().text();
            if(info.length() == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("该用户不存在！");
                alert.showAndWait();
                return;
            }
            int indexn = info.indexOf("\"name\":");
            int next_indexn = info.indexOf("\",\"age\"");
            String person_name = info.substring(indexn + 8 , next_indexn);
            int index = info.indexOf("\"id\":");
            String person_id = info.substring(index + 5 , info.length() - 1);
            String seat_level = level.getValue();
            if(!person_name.equals(name)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("姓名错误！");
                alert.showAndWait();
                return;
            }
            if(seat_level == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("请选择座位！");
                alert.showAndWait();
                return;
            }
            String ind = "0";
            int rest = 0;
            for(int i = 0 ; i < this.seat_level.size() ; i++){
                if(this.seat_level.get(i).equals(seat_level)){
                    rest = Integer.parseInt(rest_ticket.get(i));
                    ind = (seat_id.get(i));
                }
            }
            Connection con2 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
            con2.data("passenger" , person_id);
            con2.data("seat" , ind);
            Document doc2  = con2.post();
            if(!doc2.body().text().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("该乘客已购买该时段的列车！");
                alert.showAndWait();
                return;
            }
            if(rest == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("已无余票！");
                alert.showAndWait();
                return;
            }else{
                con = Jsoup.connect("http://localhost:8081/32106/Ticket/create").ignoreContentType(true);
                con.data("passenger" , person_id);
                con.data("seat" , ind);
                doc = con.post();
                if(doc.body().text().equals("true")){
                    Connection con1 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
                    con1.data("passenger" , person_id);
                    con1.data("seat" , ind);
                    Document doc1 = con1.post();
                    String ticket_id = doc1.body().text();
                    Connection con3 = Jsoup.connect("http://localhost:8081/32106/Order/create").ignoreContentType(true);
                    con3.data("user" , Main.man_id);
                    con3.data("ticket" , ticket_id);
                    doc1 = con3.post();
                    if(doc1.body().text().equals("true")){
                        Connection con4 = Jsoup.connect("http://localhost:8081/32106/Order/query").ignoreContentType(true);
                        con4.data("user" , Main.man_id);
                        con4.data("ticket" , ticket_id);
                        Document doc4 = con4.post();
                        String infom = doc4.body().text();
                        System.out.println(infom);
                        String entrance = infom.substring(infom.indexOf("\"ticket_entrance\"") + 19 , infom.indexOf("\",\"seatNumber\"") );
                        String ids = infom.substring(7 , infom.indexOf(","));
                        int index1 = infom.indexOf("\"seatNumber\":");
                        infom = infom.substring(index1);
                        index1 = infom.indexOf(":");
                        int next_index = infom.indexOf("}");
                        String seat_Number = infom.substring(index1 + 2 , next_index - 1);
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("购买成功");
                        alert1.setContentText("订单生成成功！\n订单信息：\n使用人："+ name +"\n始发站：" + table.getSelectionModel().getSelectedItem().getStart_station()
                                +"\n终点站："+ table.getSelectionModel().getSelectedItem().getEnd_station() +"\n出发时间：" + table.getSelectionModel().getSelectedItem().getStart_time()
                                +"\n日期：" + table.getSelectionModel().getSelectedItem().getDate() +"\n车次：" + table.getSelectionModel().getSelectedItem().getTrain_no() + "\n座位号：" + seat_Number
                                + "\n检票口：" + entrance
                                +"\n点击确认付款\n");
                        Optional<ButtonType> result = alert1.showAndWait();
                        if(result.isPresent() && result.get() == ButtonType.OK) {
                            Connection con5 = Jsoup.connect("http://localhost:8081/32106/Order/pay").ignoreContentType(true);
                            con5.data("id", ids);
                            Document doc5 = con5.post();
                            if (doc5.body().text().equals("Pay successful! Please check your order status again!")) {
                                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                alert2.setTitle("购买成功");
                                alert2.setContentText("支付成功！");
                                alert2.showAndWait();
                                dialogStage.close();
                                Main.primaryStage.show();
                            } else {
                                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                alert2.setTitle("购买失败");
                                alert2.setContentText("支付超时，订单已被取消！");
                                alert2.showAndWait();
                            }
                        }
                    }else{
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("购买失败");
                        alert1.setHeaderText("购买失败：");
                        alert1.setContentText("由于未知原因，订单生成失败！");
                        alert1.showAndWait();
                    }
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("购买失败");
                    alert.setHeaderText("购买失败：");
                    alert.setContentText("由于未知原因，车票生成失败！");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("购买失败");
            alert.setHeaderText("购买失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }
}
