import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class changeUserController {
    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User , User> name;

    @FXML
    private TableColumn<User , User> id;

    @FXML
    private TableColumn<User , User> phone;

    @FXML
    private TableColumn<User , User> password;

    @FXML
    private Stage dialogStage;

    @FXML
    private TextField get_phone;

    final ObservableList<User> data = FXCollections.observableArrayList();

    @FXML
    public void setDialogStage(Stage dialogStage) {

        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        phone.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));

        table.setItems(data);

        this.dialogStage = dialogStage;
    }

    @FXML
    public void quit(){
        Main_adiminstrator.primaryStage.show();
        this.dialogStage.close();
    }

    @FXML
    public void search(){
        if(Main_adiminstrator.PhoneNumber == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("修改失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String phone = get_phone.getText();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/query/phone").ignoreContentType(true);
        con.data("phone" , phone);
        try {
            Document doc = con.post();
            String info = doc.body().text();
            System.out.println(doc.body().text());
            if(info.equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("不存在该手机号注册的用户！");
                alert.showAndWait();
                return;
            }
            int index = info.indexOf(":");
            int next_index = info.indexOf("\",\"");
            String name = info.substring(index + 2 , next_index);
            index = info.indexOf(",\"idc\":\"");
            next_index = info.indexOf("\",\"phone");
            String idc = info.substring(index + 8 , next_index);
            index = next_index;
            next_index = info.indexOf("\",\"password\":\"");
            String phone_num = info.substring(index + 11 , next_index);
            index = next_index;
            next_index = info.indexOf("\",\"id\":");
            String password = info.substring(index + 14 , next_index);
            User user = new User(idc , name , phone_num , password);
            data.add(user);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查找失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }
}
