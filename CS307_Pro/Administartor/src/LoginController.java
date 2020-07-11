import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class LoginController {
    Main_adiminstrator main = new Main_adiminstrator();
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

    @FXML
    private void handleLogin() {
        if(Main_adiminstrator.PhoneNumber != null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("已有登陆账号！请先退出再登陆！");
            alert.showAndWait();
            dialogStage.close();
            Main_adiminstrator.primaryStage.show();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Admin/login").ignoreContentType(true);
        con.data("id", Phone.getText());
        con.data("password" , passwordField.getText());
        String get = "";
        try {
            Document doc = con.post();
            if(doc.body().text().equals("false")){
                failed.setVisible(true);
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }

        Main_adiminstrator.PhoneNumber = Phone.getText();
        failed.setVisible(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("登陆成功");
        alert.setContentText("登陆成功！");
        alert.showAndWait();
        Main_adiminstrator.primaryStage.show();
        dialogStage.close();
    }

    @FXML
    private void handleCreate() {
        main.showCreate();
    }
}
