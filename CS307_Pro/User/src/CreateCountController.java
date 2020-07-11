import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateCountController {

    @FXML
    private TextField name;

    @FXML
    private TextField sid;

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
    private void create() {
        String createNumber;
        String passWord;
        String repeatPassword;
        String Name, Sid;
        String age;
        Name = name.getText();
        Sid = sid.getText();
        createNumber = phone.getText();
        passWord = pass1.getText();
        repeatPassword = pass2.getText();
        int year = 0;
        if (Sid.length() != 18) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("注册失败：");
            alert.setContentText("身份证格式错误！");
            alert.showAndWait();
            return;
        }
        for (int i = 6; i < 10; i++) {
            if (Sid.charAt(i) < '0' || Sid.charAt(i) > '9') {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("报错");
                alert.setHeaderText("注册失败：");
                alert.setContentText("身份证格式错误！");
                alert.showAndWait();
                return;
            } else {
                year = year * 10 + Sid.charAt(i) - '0';
            }
        }
        Date current = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(current);
        int today = 0;
        for (int i = 0; i < 4; i++) {
            today = dateString.charAt(i) - '0' + today * 10;
        }
        age = Integer.toString(today - year);
        try {
            Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/check/idc").ignoreContentType(true);
            con.data("IDC", Sid);
            Document doc = con.post();
            if (doc.body().text().equals("false")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("报错");
                alert.setHeaderText("注册失败：");
                alert.setContentText("该身份证已被注册！");
                alert.showAndWait();
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

        try {
            Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/check/phone").ignoreContentType(true);
            con.data("phone", createNumber);
            Document doc = con.post();
            if (doc.body().text().equals("false")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("报错");
                alert.setHeaderText("注册失败：");
                alert.setContentText("该手机号已被注册！");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("注册失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }


        if (createNumber.length() != 11) {
            wrongPhone.setVisible(true);
        } else {
            char[] chars = createNumber.toCharArray();
            for (int i = 0; i < 11; i++) {
                if (!Character.isDigit(chars[i])) {
                    wrongPhone.setVisible(true);
                    return;
                }
            }
            wrongPhone.setVisible(false);
            if (passWord.length() < 8) {
                wrongPassword.setVisible(true);
            } else {
                wrongPassword.setVisible(false);
                if (!passWord.equals(repeatPassword)) {
                    repeatAgain.setVisible(true);
                } else {
                    repeatAgain.setVisible(false);
                    if (!read.isSelected()) {
                        Readtheyaoqiu.setVisible(true);

                    } else {
                        Readtheyaoqiu.setVisible(false);

                        try {
                            Connection con = Jsoup.connect("http://localhost:8081/32106/Passenger/register").ignoreContentType(true);
                            con.data("phone", createNumber);
                            con.data("age", age);
                            con.data("id_card", Sid);
                            con.data("name", Name);
                            con.data("password", passWord);
                            Document doc = con.post();
                            if(doc.body().text() == null){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("报错");
                                alert.setHeaderText("注册失败：");
                                alert.setContentText("遇到了未知错误，请重试！");
                                alert.showAndWait();
                                return;
                            }
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("报错");
                            alert.setHeaderText("注册失败：");
                            alert.setContentText("请检查网络！");
                            alert.showAndWait();
                            e.printStackTrace();
                            return;
                        }
                        Main.Name = Name;
                        Main.passWord = passWord;
                        Main.PhoneNumber = createNumber;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("信息");
                        alert.setHeaderText("恭喜" + Main.Name + "：");
                        alert.setContentText("创建成功！");
                        alert.showAndWait();
                        dialogStage.close();
                        LoginController.dialogStage.show();
                    }
                }
            }
        }
    }

    @FXML
    private void cancel() {
        LoginController.dialogStage.show();
        dialogStage.close();
    }
}