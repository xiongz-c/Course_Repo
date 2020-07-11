import javafx.beans.property.SimpleStringProperty;
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

public class CreateStation {
    private final ObservableList<Station> data = FXCollections.observableArrayList();

    public class Station {
        private SimpleStringProperty name, city, code;

        Station(String name, String city, String code) {
            this.name = new SimpleStringProperty(name);
            this.city = new SimpleStringProperty(city);
            this.code = new SimpleStringProperty(code);
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setCity(String city) {
            this.city.set(city);
        }

        public void setCode(String code) {
            this.code.set(code);
        }

        public String getCity() {
            return city.get();
        }

        public String getName() {
            return name.get();
        }

        public String getCode() {
            return code.get();
        }
    }

    private Stage dialogStage;

    @FXML
    private TextField adder_name;

    @FXML
    private TextField adder_city;

    @FXML
    private TextField adder_code;

    @FXML
    private TextField search_name;

    @FXML
    private TableView<Station> table;

    @FXML
    private TableColumn<Station, Station> station_name;

    @FXML
    private TableColumn<Station, Station> station_code;

    @FXML
    private TableColumn<Station, Station> station_city;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void handleCreate() {
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String name = adder_name.getText();
        String city = adder_city.getText();
        String code = adder_code.getText();
        if (name.equals("") || city.equals("") || code.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先填完所有信息！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Station/create").ignoreContentType(true);
        con.data("station_name", name);
        con.data("code", code);
        con.data("city", city);
        try {
            Document doc = con.post();
            if (doc.body().text().equals("false")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("插入失败");
                alert.setContentText("车站名称或编码重复！");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入成功");
            alert.setContentText("插入成功！");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void handleSearch() {
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String name = search_name.getText();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Station/query/name").ignoreContentType(true);
        con.data("name", name);
        try {
            Document doc = con.post();
            String info = doc.body().text();
            if(info.length() == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("查询失败");
                alert.setContentText("未找到此站点！");
                alert.showAndWait();
                return;
            }
            System.out.println(doc.body().text());
            String output_name;
            String output_code;
            String output_city;
            int index = info.indexOf("\"name\":");
            int next_index = info.indexOf("\",\"code\":");
            output_name = info.substring(index + 8, next_index);
            index = next_index;
            next_index = info.indexOf("\",\"city\":");
            output_code = info.substring(index + 10, next_index);
            index = next_index;
            next_index = info.indexOf("\"}");
            output_city = info.substring(index + 10, next_index);
            Station station = new Station(output_name, output_city, output_code);
            data.clear();
            data.add(station);
            station_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            station_city.setCellValueFactory(new PropertyValueFactory<>("city"));
            station_code.setCellValueFactory(new PropertyValueFactory<>("code"));
            table.setItems(data);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("查找失败：");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }

    public void back(){
        Main_adiminstrator.primaryStage.show();
        dialogStage.close();
    }
}
