import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class StageController {
    private Main_adiminstrator main = new Main_adiminstrator();




    @FXML
    public void handleUser(){
        main.showChangeUser();
    }

    @FXML
    public void handleLogin(){
        main.showLogin();
    }


    @FXML
    public void handleChangeSchedule(){
        main.showChangeSchedule();
    }

    @FXML
    public void handleLogout(){
        main.PhoneNumber = null;
    }

    @FXML
    public void handleCreateStation(){
        main.showChangeStation();
    }
}
