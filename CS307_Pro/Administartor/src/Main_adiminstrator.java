import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main_adiminstrator extends Application {

    public static Stage primaryStage;
    private BorderPane rootLayout;

    public static String PhoneNumber = null;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("32106");

        initRootLayout();
        showStage();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("administratorStage.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangeUser() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            if(PhoneNumber == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("打开失败");
                alert.setContentText("请先登录！");
                alert.showAndWait();
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("changeUser.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("修改用户");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            changeUserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            primaryStage.hide();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showLogin() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("Login.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("登陆");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            primaryStage.hide();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showCreate() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("CreateCount.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("注册用户");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CreateCountController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            LoginController.dialogStage.hide();
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangeSchedule(){
        try {
            if(PhoneNumber == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("打开失败");
                alert.setContentText("请先登录！");
                alert.showAndWait();
                return;
            }
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("changeSchedule.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("修改车程表");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            changeSchedule controller = loader.getController();
            controller.setDialogStage(dialogStage);
            primaryStage.hide();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangeStation(){
        try {
            if(PhoneNumber == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("打开失败");
                alert.setContentText("请先登录！");
                alert.showAndWait();
                return;
            }
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("CreateStation.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("修改车程表");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            CreateStation controller = loader.getController();
            controller.setDialogStage(dialogStage);
            primaryStage.hide();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChangeScheduleDetail(ArrayList<Schedule_detail> array) {
        try {
            if (PhoneNumber == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("打开失败");
                alert.setContentText("请先登录！");
                alert.showAndWait();
                return;
            }
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main_adiminstrator.class.getResource("changeDetail.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("修改车程表");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            ChangeDetail controller = loader.getController();
            controller.setDialogStage(dialogStage, array);
            dialogStage.show();
            changeSchedule.dialogStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}