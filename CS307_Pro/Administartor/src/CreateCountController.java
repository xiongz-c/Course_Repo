import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CreateCountController {
    private String createNumber;
    private String passWord;
    private String repeatPassword;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField pass1;
    @FXML
    private PasswordField pass2;
    @FXML
    private RadioButton read;

    @FXML
    private Stage dialogStage;

    @FXML
    private Label wrongPhone;

    @FXML
    private Label repeatAgain;

    @FXML
    private Label wrongPassword;

    @FXML
    private Label Readtheyaoqiu;

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    @FXML
    private void create(){
        createNumber = phone.getText();
        passWord = pass1.getText();
        repeatPassword = pass2.getText();
        if(createNumber.length() < 6){
            wrongPhone.setVisible(true);
        }
        else{
            char[] chars = createNumber.toCharArray();
            for(int i = 0 ; i < chars.length ; i++){
                if(!Character.isDigit(chars[i])){
                    wrongPhone.setVisible(true);
                    return;
                }
            }
            wrongPhone.setVisible(false);
            if(passWord.length() < 8){
                wrongPassword.setVisible(true);
            }
            else{
                wrongPassword.setVisible(false);
                if(!passWord.equals(repeatPassword)){
                    repeatAgain.setVisible(true);
                }
                else{
                    repeatAgain.setVisible(false);
                    if(!read.isSelected()){
                        Readtheyaoqiu.setVisible(true);

                    }
                    else{
                        Readtheyaoqiu.setVisible(false);
                        Connection con = Jsoup.connect("http://localhost:8081/32106/Admin/register").ignoreContentType(true);
                        con.data("id" , phone.getText());
                        con.data("password" , repeatPassword);
                        try {
                            Document doc = con.post();
                            if(doc.body().text().equals("false")){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("报错");
                                alert.setHeaderText("创建失败：");
                                alert.setContentText("该账号已被注册！");
                                alert.showAndWait();
                                return;
                            }
                        } catch (IOException e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("报错");
                            alert.setHeaderText("创建失败：");
                            alert.setContentText("请检查网络！");
                            alert.showAndWait();
                            e.printStackTrace();
                            return;
                        }
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("信息");
                        alert.setHeaderText("恭喜你：");
                        alert.setContentText("创建成功！");
                        alert.showAndWait();
                        LoginController.dialogStage.show();
                        dialogStage.close();
                    }
                }
            }
        }
    }

    @FXML
    private void cancel(){
        dialogStage.close();
        LoginController.dialogStage.show();
    }
}
