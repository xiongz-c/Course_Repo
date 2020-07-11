import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;


public class LoginController {
    Main main = new Main();
    @FXML
    public static Stage dialogStage;

    @FXML
    private TextField Phone;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label failed;

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private String phoneNumber;

    @FXML
    private void handleLogin() {
        Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/login").ignoreContentType(true);
        con.data("phone", Phone.getText());
        con.data("password" , passwordField.getText());
        String get = "";
        try {
            Document doc = con.post();
            get = doc.body().text();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }

        if (get.equals("true")) {
            con = Jsoup.connect("http://localhost:8081/32106/Passenger/query/phone").ignoreContentType(true);
            con.data("phone" , Phone.getText());
            try {
                Document doc = con.post();
                System.out.println(doc.body().text());
                Main.man_id = doc.body().text().substring(doc.body().text().indexOf("\"id\":") + 5, doc.body().text().length() - 1);
                Main.PhoneNumber = Phone.getText();
                Main.passWord = passwordField.getText();
                Main.Name = doc.body().text().split(":")[1].split(",")[0];
                Main.Name = Main.Name.substring(1 , Main.Name.length() - 1);
                Main.id = doc.body().text().split(",")[2].split(":")[1];
                Main.id = Main.id.substring(1 , Main.id.length() - 1);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("报错");
                alert.setHeaderText("登陆失败：");
                alert.setContentText("请检查网络！");
                alert.showAndWait();
                e.printStackTrace();
                return;
            }
            failed.setVisible(false);
            dialogStage.close();
            Main.primaryStage.show();
        } else {
            failed.setVisible(true);
        }
    }

    @FXML
    private void handleCreate() {
        main.showCreate();
    }
}
