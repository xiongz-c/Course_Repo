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

public class SelfInfoController {
    private Stage dialogStage;

    @FXML
    private javafx.scene.control.Label check;

    @FXML
    private Label id;

    @FXML
    private Label name;

    @FXML
    private TextField phone;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    public void setDialogStage(Stage dialogStage){
        phone.setText(Main.PhoneNumber);
        id.setText(Main.id);
        name.setText(Main.Name);
        password1.setText(Main.passWord);
        this.dialogStage = dialogStage;
    }

    @FXML
    private void changeInfo(){
        if(Main.PhoneNumber == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
        }
        if(!password1.getText().equals(password2.getText())){
            check.setVisible(true);
            System.out.println(password1.getText());
            System.out.println(password2.getText());
            return;
        }
        if(phone.getText().length() != 11){
            check.setVisible(true);
            System.out.println(phone.getText());
            return;
        }

        try{
            check.setVisible(false);
            Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/update").ignoreContentType(true);
            con.data("phone" , phone.getText());
            con.data("password" , password1.getText());
            con.data("idc" , Main.id);
            Document doc = con.post();
            if(doc.body().text().equals("true")){
                Main.passWord = password1.getText();
                Main.PhoneNumber = phone.getText();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("恭喜");
                alert.setContentText("修改成功！");
                alert.showAndWait();
            }
            else{
                check.setVisible(true);
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(){
        dialogStage.close();
        Main.primaryStage.show();
    }

}
