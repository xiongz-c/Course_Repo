import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class OrderListController {
    private Stage dialogStage;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order , Order> id;

    @FXML
    private TableColumn<Order , Order>train_id;

    @FXML
    private TableColumn<Order , Order>start_station;

    @FXML
    private TableColumn<Order , Order>end_station;

    @FXML
    private TableColumn<Order , Order>price;

    @FXML
    private  TableColumn<Order , Order>user;

    @FXML
    private TableColumn<Order , Order>statue;

    private final ObservableList<Order> data = FXCollections.observableArrayList();

    public void setDialogStage(Stage dialogStage){
        Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/query/phone").ignoreContentType(true);
        con.data("phone" , Main.PhoneNumber);
        try {
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            train_id.setCellValueFactory(new PropertyValueFactory<>("train_id"));
            start_station.setCellValueFactory(new PropertyValueFactory<>("start_station"));
            end_station.setCellValueFactory(new PropertyValueFactory<>("end_station"));
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            user.setCellValueFactory(new PropertyValueFactory<>("user"));
            statue.setCellValueFactory(new PropertyValueFactory<>("statue"));
            this.dialogStage = dialogStage;
            String info1 = con.post().body().text();
            String id = info1.substring(info1.indexOf("\"id\":") + 5 , info1.length() - 1);
            Connection con1 = Jsoup.connect("http://localhost:8081/32106/Order/query/byId").ignoreContentType(true);
            con1.data("user" , id);
            Document doc = con1.post();
            if(doc.body().text().equals("[]")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("您暂时没有订单！");
                alert.showAndWait();
                return;
            }
            String info[] = doc.body().text().split("},\\{");
            for(int i = 0 ; i < info.length ; i++){
                System.out.println(info[i]);
                info[i] = info[i].replaceAll("]" , "").replaceAll("\\[" , "").replaceAll("\\{" , "").replaceAll("}" , "");
                String order_id , train_id , start_station , end_station , price , user , statue , ticket_id , user_id;
                ticket_id = info[i].substring(info[i].indexOf("\"ticket\"") + 14 , info[i].indexOf(",\"seat\""));
                int index = info[i].indexOf(":");
                int next_index = info[i].indexOf(",");
                order_id = info[i].substring(index + 1 , next_index);
                index = info[i].indexOf("\"name\":");
                next_index = info[i].indexOf("\",\"age\":");
                info[i] = info[i].substring(next_index + 7);
                index = info[i].indexOf("\"passenger\":\"name\":");
                next_index = info[i].indexOf("\",\"age\":");
                user = info[i].substring(index + 20 , next_index);
                System.out.println(info[i] + "\n" + user);
                user_id = info[i].substring(info[i].indexOf("\"id\"") + 5 , info[i].indexOf(",\"ticket\""));
                index = info[i].indexOf("\"train\"");
                info[i] = info[i].substring(index);
                index = info[i].indexOf("\"name\":");
                next_index = info[i].indexOf("\",\"no\"");
                train_id = info[i].substring(index + 8 , next_index);
                info[i] = info[i].substring(next_index);
                index = info[i].indexOf("\"name\"");
                next_index = info[i].indexOf("\",\"code\"");
                start_station = info[i].substring(index + 8, next_index);
                index = info[i].indexOf("\"to_station\"");
                info[i] = info[i].substring(index);
                index = info[i].indexOf("\"name\"");
                next_index = info[i].indexOf("\",\"code\"");
                end_station = info[i].substring(index + 8 , next_index);
                index = info[i].indexOf("\"price\":");
                next_index = info[i].indexOf(",\"passenger\":");
                price = info[i].substring(index + 8 , next_index);
                index = info[i].indexOf("\"status\"");
                next_index = info[i].length();
                statue = info[i].substring(index + 9 , next_index);
                switch (statue){
                    case "-1":
                        statue = "已取消";
                        break;
                    case "0":
                        statue = "下单，未付款";
                        break;
                    case "1":
                        statue = "已付款,未发车";
                        break;
                    case "2":
                        statue = "已付款，已发车";
                }
                Order order = new Order(order_id , train_id , end_station , start_station , user , price , statue , ticket_id , user_id);
                data.add(order);
            }
            table.setItems(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel(){
        Order order = table.getSelectionModel().getSelectedItem();
        if(order.getStatue().equals("已取消")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("该订单已取消！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Order/cancel").ignoreContentType(true);
        con.data("id" , order.getId());
        try {
            Document doc = con.post();
            if(doc.body().text().equals("true")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("删除成功！");
                alert.show();
                Order order1 = table.getSelectionModel().getSelectedItem();
                for(int i = 0 ; i < data.size() ; i++){
                    if(order1.getId().equals(data.get(i).getId())){
                        data.get(i).setStatue("已取消");
                    }
                }
                table.refresh();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("报错");
                alert.setHeaderText("删除失败2！");
                alert.showAndWait();
                return;
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("删除失败1！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void pay(){
        Order order = table.getSelectionModel().getSelectedItem();
        if(!order.getStatue().equals("下单，未付款")){
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setContentText("该订单已付款或已取消！");
            alert2.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Order/query").ignoreContentType(true);
        con.data("user" , order.getUser_id());
        con.data("ticket" , order.getTicket_id());
        try {
            Document doc = con.post();
            String infom = doc.body().text();
            String entrance = infom.substring(infom.indexOf("\"ticket_entrance\"") + 19 , infom.indexOf("\",\"seatNumber\"") );
            String ids = infom.substring(7 , infom.indexOf(","));
            int index1 = infom.indexOf("\"seatNumber\":");
            infom = infom.substring(index1);
            index1 = infom.indexOf(":");
            int next_index = infom.indexOf("}");
            String seat_Number = infom.substring(index1 + 2 , next_index - 1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("详细信息");
            alert.setHeaderText("详细信息：");
            alert.setHeaderText("\n座位号："+ seat_Number +"\n检票口："+ entrance + "\n点击确定付款！");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                Connection con1 = Jsoup.connect("http://localhost:8081/32106/Order/pay").ignoreContentType(true);
                con1.data("id" , ids);
                Document doc1 = con1.post();
                if (doc1.body().text().equals("Pay successful! Please check your order status again!")) {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("购买成功");
                    alert2.setContentText("支付成功！");
                    alert2.showAndWait();
                    table.getSelectionModel().getSelectedItem().setStatue("已付款，未发车");
                    table.refresh();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("购买失败");
                    alert2.setContentText("支付超时，订单已被取消！");
                    alert2.showAndWait();
                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("网络错误！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void detail(){
        Order order = table.getSelectionModel().getSelectedItem();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Order/query").ignoreContentType(true);
        con.data("user" , order.getUser_id());
        con.data("ticket" , order.getTicket_id());
        try {
            Document doc = con.post();
            String infom = doc.body().text();
            System.out.println(infom);
            String entrance = infom.substring(infom.indexOf("\"ticket_entrance\"") + 19 , infom.indexOf("\",\"seatNumber\"") );
            String ids = infom.substring(7 , infom.indexOf(","));
            int index1 = infom.indexOf("\"seatNumber\":");
            infom = infom.substring(index1);
            index1 = infom.indexOf(":");
            int next_index = infom.indexOf("}");
            String seat_Number = infom.substring(index1 + 2 , next_index - 1);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("详细信息");
            alert.setHeaderText("详细信息：");
            alert.setHeaderText("\n座位号："+ seat_Number +"\n检票口："+ entrance );
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("网络错误！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void back(){
        dialogStage.close();
        Main.primaryStage.show();
    }

}
