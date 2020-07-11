import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class UserStage {
    public Main main = new Main();


    @FXML
    public void Login(){
        main.showLogin();
    }

    @FXML
    public void exit(){
        Main.PhoneNumber = null;
    }

    @FXML
    public void order(){
        if(Main.PhoneNumber == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
        }
        else{
            main.showOrder();
        }
    }

    @FXML
    public void ticket(){
        if(Main.PhoneNumber == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
        }
        else{
            main.showTicket();
        }
    }

    @FXML
    public void info(){
        if(Main.PhoneNumber == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("打开失败：");
            alert.setContentText("请先登录！");
            alert.showAndWait();
        }
        else{
            main.showInfo();
        }
    }
}
