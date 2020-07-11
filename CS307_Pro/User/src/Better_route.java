import com.sun.xml.internal.fastinfoset.tools.TransformInputOutput;
import javafx.beans.property.Property;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Better_route {
    public static Stage dialogStage;

    @FXML
    private TableView<Transfer> table;

    @FXML
    private TableColumn<Transfer, Transfer> train_no_1;

    @FXML
    private TableColumn<Transfer, Transfer> train_no_2;

    @FXML
    private TableColumn<Transfer, Transfer> first_Station;

    @FXML
    private TableColumn<Transfer, Transfer> second_Station;

    @FXML
    private TableColumn<Transfer, Transfer> third_Station;

    @FXML
    private TableColumn<Transfer, Transfer> first_arrive_time;

    @FXML
    private TableColumn<Transfer, Transfer> second_arrive_time;

    @FXML
    private TableColumn<Transfer, Transfer> first_start_time;

    @FXML
    private TableColumn<Transfer, Transfer> second_start_time;

    @FXML
    private TableColumn<Transfer, Transfer> date;


    @FXML
    private ChoiceBox<String> train1;

    @FXML
    private ChoiceBox<String> train2;

    @FXML
    private Label price1;

    @FXML
    private Label price2;

    @FXML
    private Label rest_t1;

    @FXML
    private Label rest_t2;

    @FXML
    private TextField user_name;

    @FXML
    private TextField user_id;

    public ArrayList<String> rest_ticket1 = new ArrayList<>();
    public ArrayList<String> a_seat_level1 = new ArrayList<>();
    public ArrayList<String> a_price1 = new ArrayList<>();
    public ArrayList<String> seat_id1 = new ArrayList<>();

    public ArrayList<String> rest_ticket2 = new ArrayList<>();
    public ArrayList<String> a_seat_level2 = new ArrayList<>();
    public ArrayList<String> a_price2 = new ArrayList<>();
    public ArrayList<String> seat_id2 = new ArrayList<>();

    private final ObservableList<Transfer> data = FXCollections.observableArrayList();
    private final ObservableList<String> seat_level1 = FXCollections.observableArrayList();
    private final ObservableList<String> seat_level2 = FXCollections.observableArrayList();

    public void setDialogStage(Stage dialogStage, ArrayList<Transfer> list) {
        train1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Main.PhoneNumber == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("报错");
                    alert.setHeaderText("打开失败：");
                    alert.setContentText("请先登录！");
                    alert.showAndWait();
                    return;
                }
                String level = train1.getValue();
                int i = 0;
                for (; i < seat_level1.size(); i++) {
                    if (seat_level1.get(i).equals(level))
                        break;
                }
                price1.setText("价格：" + a_price1.get(i));
                rest_t1.setText("余票：" + rest_ticket1.get(i));
            }
        });

        train2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (Main.PhoneNumber == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("报错");
                    alert.setHeaderText("打开失败：");
                    alert.setContentText("请先登录！");
                    alert.showAndWait();
                    return;
                }
                String level = train2.getValue();
                int i = 0;
                for (; i < seat_level2.size(); i++) {
                    if (seat_level2.get(i).equals(level))
                        break;
                }
                price2.setText("价格：" + a_price2.get(i));
                rest_t2.setText("余票：" + rest_ticket2.get(i));
            }
        });

        table.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            rest_t1.setText("余票：");
            rest_t2.setText("余票：");
            seat_level1.clear();

            seat_level2.clear();
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 1) {
                a_price1.clear();
                a_seat_level1.clear();
                rest_ticket1.clear();
                seat_id1.clear();
                if (Main.PhoneNumber == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("报错");
                    alert.setHeaderText("打开失败：");
                    alert.setContentText("请先登录！");
                    alert.showAndWait();
                    return;
                }
                Transfer transfer = table.getSelectionModel().getSelectedItem();
                Connection con = Jsoup.connect("http://localhost:8081/32106/Seat/query/leftTicket").ignoreContentType(true);
                con.data("start_station", transfer.getFirst_Station());
                con.data("dest_station", transfer.getSecond_Station());
                con.data("train_name", transfer.getTrain_no_1());
                con.data("date", transfer.getDate());
                try {
                    Document doc = con.post();
                    String info[] = doc.body().text().split("],\\[");
                    for (int i = 0; i < info.length; i++) {
                        info[i] = info[i].replaceAll("\\[", "").replaceAll("]", "");
                        int index = 1;
                        int next_index = info[i].indexOf(",");
                        String id = info[i].substring(index, next_index - 1);
                        index = info[i].indexOf(",");
                        info[i] = info[i].replaceFirst(",", "");
                        next_index = info[i].indexOf(",");
                        String level = info[i].substring(index + 1, next_index - 1);
                        info[i] = info[i].replaceFirst(",", "");
                        index = next_index;
                        next_index = info[i].indexOf(",");
                        String rest = info[i].substring(index + 1, next_index - 1);
                        info[i] = info[i].replaceFirst(",", "");
                        index = next_index;
                        next_index = info[i].length();
                        String price = info[i].substring(index + 1, next_index - 1);
                        boolean flag = true;
                        for (int k = 0; k < a_seat_level1.size(); k++) {
                            if (a_seat_level1.get(k).equals(level)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            a_price1.add(price);
                            rest_ticket1.add(rest);
                            a_seat_level1.add(level);
                            seat_id1.add(id);
                        }
                    }
                    seat seat = new seat();
                    seat.setSeat(a_seat_level1, seat_level1);
                    train1.setItems(seat_level1);

                    a_price2.clear();
                    a_seat_level2.clear();
                    rest_ticket2.clear();
                    seat_id2.clear();
                    Connection con1 = Jsoup.connect("http://localhost:8081/32106/Seat/query/leftTicket").ignoreContentType(true);
                    con1.data("start_station", transfer.getSecond_Station());
                    con1.data("dest_station", transfer.getThird_Station());
                    con1.data("train_name", transfer.getTrain_no_2());
                    con1.data("date", transfer.getDate());
                    doc = con1.post();
                    info = doc.body().text().split("],\\[");
                    for (int i = 0; i < info.length; i++) {
                        info[i] = info[i].replaceAll("\\[", "").replaceAll("]", "");
                        int index = 1;
                        int next_index = info[i].indexOf(",");
                        String id = info[i].substring(index, next_index - 1);
                        index = info[i].indexOf(",");
                        info[i] = info[i].replaceFirst(",", "");
                        next_index = info[i].indexOf(",");
                        String level = info[i].substring(index + 1, next_index - 1);
                        info[i] = info[i].replaceFirst(",", "");
                        index = next_index;
                        next_index = info[i].indexOf(",");
                        String rest = info[i].substring(index + 1, next_index - 1);
                        info[i] = info[i].replaceFirst(",", "");
                        index = next_index;
                        next_index = info[i].length();
                        String price = info[i].substring(index + 1, next_index - 1);
                        boolean flag = true;
                        for (int k = 0; k < a_seat_level2.size(); k++) {
                            if (a_seat_level2.get(k).equals(level)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            a_price2.add(price);
                            rest_ticket2.add(rest);
                            a_seat_level2.add(level);
                            seat_id2.add(id);
                        }
                    }
                    seat.setSeat(a_seat_level2, seat_level2);
                    train2.setItems(seat_level2);
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

        train_no_1.setCellValueFactory(new PropertyValueFactory<>("train_no_1"));
        train_no_2.setCellValueFactory(new PropertyValueFactory<>("train_no_2"));
        first_Station.setCellValueFactory(new PropertyValueFactory<>("first_Station"));
        second_Station.setCellValueFactory(new PropertyValueFactory<>("second_Station"));
        third_Station.setCellValueFactory(new PropertyValueFactory<>("third_Station"));
        first_arrive_time.setCellValueFactory(new PropertyValueFactory<>("first_arrive_time_change"));
        second_arrive_time.setCellValueFactory(new PropertyValueFactory<>("second_arrive_time_change"));
        first_start_time.setCellValueFactory(new PropertyValueFactory<>("first_start_time_change"));
        second_start_time.setCellValueFactory(new PropertyValueFactory<>("second_start_time_change"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        data.addAll(list);
        table.setItems(data);
        this.dialogStage = dialogStage;
    }

    public void buy() {
        if (Main.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("购买失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String name = user_name.getText();
        String id = user_id.getText();
        if (name.equals("") || id.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("购买失败：");
            alert.setContentText("请先补全信息！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/query/IDC").ignoreContentType(true);
        con.data("IDC", id);
        try {
            Document doc = con.post();
            String info = doc.body().text();
            if (info.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("该用户不存在！");
                alert.showAndWait();
                return;
            }
            int indexn = info.indexOf("\"name\":");
            int next_indexn = info.indexOf("\",\"age\"");
            String person_name = info.substring(indexn + 8, next_indexn);
            int index = info.indexOf("\"id\":");
            String person_id = info.substring(index + 5, info.length() - 1);
            String seat_level1 = train1.getValue();
            String seat_level2 = train2.getValue();
            if (!person_name.equals(name)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("姓名错误！");
                alert.showAndWait();
                return;
            }
            if (seat_level1 == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("请选择座位！");
                alert.showAndWait();
                return;
            }
            if (seat_level2 == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("请选择座位！");
                alert.showAndWait();
                return;
            }
            String ind1 = "0";
            int rest1 = 0;
            for (int i = 0; i < this.seat_level1.size(); i++) {
                if (this.seat_level1.get(i).equals(seat_level1)) {
                    rest1 = Integer.parseInt(rest_ticket1.get(i));
                    ind1 = (seat_id1.get(i));
                }
            }
            String ind2 = "0";
            int rest2 = 0;
            for (int i = 0; i < this.seat_level2.size(); i++) {
                if (this.seat_level2.get(i).equals(seat_level2)) {
                    rest2 = Integer.parseInt(rest_ticket2.get(i));
                    ind2 = (seat_id2.get(i));
                }
            }
            if (rest1 == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("已无余票！");
                alert.showAndWait();
                return;
            } else if (rest2 == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("已无余票！");
                alert.showAndWait();
                return;
            }
            Connection con1 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
            con1.data("passenger", person_id);
            con1.data("seat", ind1);
            if (!con1.post().body().text().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("该乘客已购买该时段的列车！");
                alert.showAndWait();
                return;
            }
            Connection con2 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
            con2.data("passenger", person_id);
            con2.data("seat", ind2);
            if (!con2.post().body().text().equals("")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("该乘客已购买该时段的列车！");
                alert.showAndWait();
                return;
            }
            con = Jsoup.connect("http://localhost:8081/32106/Ticket/create").ignoreContentType(true);
            con.data("passenger", person_id);
            con.data("seat", ind1);
            doc = con.post();
            Connection con3 = Jsoup.connect("http://localhost:8081/32106/Ticket/create").ignoreContentType(true);
            con3.data("passenger", person_id);
            con3.data("seat", ind2);
            Document doc3 = con3.post();
            if (doc.body().text().equals("true") && doc3.body().text().equals("true")) {
                Connection con4 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
                con4.data("passenger" , person_id);
                con4.data("seat" , ind1);
                String ticketid1 = con4.post().body().text();
                Connection con5 = Jsoup.connect("http://localhost:8081/32106/Ticket/queryId").ignoreContentType(true);
                con5.data("passenger" , person_id);
                con5.data("seat" , ind2);
                String ticketid2 = con5.post().body().text();
                Connection con6 = Jsoup.connect("http://localhost:8081/32106/Order/create").ignoreContentType(true);
                con6.data("user" , Main.man_id);
                con6.data("ticket" , ticketid1);
                String a = con6.post().body().text();
                Connection con7 = Jsoup.connect("http://localhost:8081/32106/Order/create").ignoreContentType(true);
                con7.data("user" , Main.man_id);
                con7.data("ticket" , ticketid2);
                String b = con7.post().body().text();
                if(a.equals(b) && a.equals("true")){
                    Connection con8 = Jsoup.connect("http://localhost:8081/32106/Order/query").ignoreContentType(true);
                    con8.data("user" , Main.man_id);
                    con8.data("ticket" , ticketid1);
                    Document doc8 = con8.post();
                    String infom1 = doc8.body().text();
                    String entrance1 = infom1.substring(infom1.indexOf("\"ticket_entrance\"") + 19 , infom1.indexOf("\",\"seatNumber\"") );
                    String ids1 = infom1.substring(7 , infom1.indexOf(","));
                    int index1 = infom1.indexOf("\"seatNumber\":");
                    infom1 = infom1.substring(index1);
                    index1 = infom1.indexOf(":");
                    int next_index = infom1.indexOf("}");
                    String seat_Number1 = infom1.substring(index1 + 2 , next_index - 1);
                    Connection con9 = Jsoup.connect("http://localhost:8081/32106/Order/query").ignoreContentType(true);
                    con9.data("user" , Main.man_id);
                    con9.data("ticket" , ticketid2);
                    Document doc9 = con9.post();
                    String infom2 = doc9.body().text();
                    String entrance2 = infom2.substring(infom2.indexOf("\"ticket_entrance\"") + 19 , infom2.indexOf("\",\"seatNumber\"") );
                    String ids2 = infom2.substring(7 , infom2.indexOf(","));
                    int index2 = infom2.indexOf("\"seatNumber\":");
                    infom2 = infom2.substring(index2);
                    index2 = infom2.indexOf(":");
                    int next_index2 = infom2.indexOf("}");
                    String seat_Number2 = infom2.substring(index2 + 2 , next_index2 - 1);
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setHeaderText("订单生成成功：");
                    alert1.setContentText("\n订单信息：\n使用人：" + person_name + "\n始发站：" + table.getSelectionModel().getSelectedItem().getFirst_Station()
                            +"\n中转站：" + table.getSelectionModel().getSelectedItem().getSecond_Station()  +"\n终点站：" + table.getSelectionModel().getSelectedItem() .getThird_Station()
                            +"\n出发时间：" + table.getSelectionModel().getSelectedItem().getFirst_start_time() + "\n中转站到达时间：" + table.getSelectionModel().getSelectedItem().getFirst_arrive_time() +
                            "\n中转站出发时间：" + table.getSelectionModel().getSelectedItem().getSecond_start_time() + "\n终点站到达时间：" + table.getSelectionModel().getSelectedItem().getSecond_arrive_time()
                            + "\n第一辆车车次：" + table.getSelectionModel().getSelectedItem().getTrain_no_1() + "\n第一个座位号：" + seat_Number1 + "\n第一个检票口：" + entrance1
                            + "\n第二辆车车次：" + table.getSelectionModel().getSelectedItem().getTrain_no_2() + "\n第二个座位号：" + seat_Number2 + "\n第二个检票口：" + entrance2
                            + "\n点击确认付款！\n");
                    Optional<ButtonType> result = alert1.showAndWait();
                    if(result.isPresent() && result.get() == ButtonType.OK) {
                        Connection con10 = Jsoup.connect("http://localhost:8081/32106/Order/pay").ignoreContentType(true);
                        con10.data("id", ids1);
                        Document doc5 = con10.post();
                        Connection con11 = Jsoup.connect("http://localhost:8081/32106/Order/pay").ignoreContentType(true);
                        con11.data("id", ids2);
                        Document doc6 = con11.post();
                        if (doc5.body().text().equals("Pay successful! Please check your order status again!") && doc6.body().text().equals("Pay successful! Please check your order status again!")) {
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
                }
                else{
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("购买失败");
                    alert1.setHeaderText("购买失败：");
                    alert1.setContentText("由于未知原因，订单生成失败！");
                    alert1.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("购买失败");
                alert.setHeaderText("购买失败：");
                alert.setContentText("由于未知原因，购买失败！");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("购买失败：");
            alert.setContentText("请检查网络设置！");
            alert.showAndWait();
            return;
        }
    }

    public void back() {
        dialogStage.close();
        tickController.dialogStage.show();
    }

}
