import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class changeSchedule {
    public static class schedule {
        String station_name;
        String train_name;
        String arrive_time;
        String leave_time;
        String date;
        String en_able;
        int station_no;

        schedule(String station_name, String train_name, String arrive_time, String leave_time, String date, String en_able, String station_no) {
            this.station_name = station_name;
            this.train_name = train_name;
            this.arrive_time = arrive_time;
            this.leave_time = leave_time;
            this.date = date;
            this.en_able = en_able;
            this.station_no = Integer.parseInt(station_no);
        }
    }

    @FXML
    private TableView<Schedule> table;

    @FXML
    private TableColumn<Schedule, Schedule> date;

    @FXML
    private TableColumn<Schedule, Schedule> train_id;

    @FXML
    private TableColumn<Schedule, Schedule> start_station;

    @FXML
    private TableColumn<Schedule, Schedule> end_station;

    @FXML
    private TextField filePath;

    @FXML
    private TextField search_train;

    @FXML
    private TextField search_date;

    public static Stage dialogStage;

    private ArrayList<Schedule_detail> details = new ArrayList<>();

    private final ObservableList<Schedule> data = FXCollections.observableArrayList();

    public void back(){
        Main_adiminstrator.primaryStage.show();
        dialogStage.close();
    }

    public void setTable(ArrayList<Schedule> list) {
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        train_id.setCellValueFactory(new PropertyValueFactory<>("train_id"));
        start_station.setCellValueFactory(new PropertyValueFactory<>("start_station"));
        end_station.setCellValueFactory(new PropertyValueFactory<>("end_station"));
        data.addAll(list);
        table.setItems(data);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void addSchedule() {
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String filepath = filePath.getText();
        File infile = new File(filepath);
        String inString = "";
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(infile));
            while ((inString = reader.readLine()) != null) {
                buffer.append(inString);
                buffer.append("\n");
            }
            reader.close();
            inString = buffer.toString();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("文件有误！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("文件读取有误！");
            alert.showAndWait();
            e.printStackTrace();
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Schedule/create").ignoreContentType(true);
        con.data("text", inString);
        try {
            if (con.post().body().text().equals("true")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("恭喜");
                alert.setContentText("插入成功！");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("插入失败");
                alert.setContentText("文件格式有误！");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("报错");
            alert.setHeaderText("插入失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    public void handleChange() {
        Main_adiminstrator main = new Main_adiminstrator();
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("打开失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        if (table.getSelectionModel() != null) {
            main.showChangeScheduleDetail(details);
        }
    }

    public void handleSearch() {
        details.clear();
        data.clear();
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查询失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String search_name = search_train.getText();
        String search_time = search_date.getText();
        if (search_time.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查询失败");
            alert.setContentText("请先输入日期！");
            alert.showAndWait();
            return;
        }
        if (search_name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查询失败");
            alert.setContentText("请先输入车次！");
            alert.showAndWait();
            return;
        }
        Connection con = Jsoup.connect("http://localhost:8081/32106/Schedule/queryByName").ignoreContentType(true);
        con.data("train_name", search_name);
        try {
            Document doc = con.post();
            String[] in = doc.body().text().replaceAll("\\[\\{", "").replaceAll("}]", "").split("},\\{");
            schedule[] schedules = new schedule[in.length];
            int max = 0;
            int min = in.length;
            int max_index = 0;
            int min_index = 0;
            for (int i = 0; i < in.length; i++) {
                if(in[i].equals("[]"))
                    continue;
                int index = in[i].indexOf("name");
                int next_index = in[i].indexOf("\",\"no\"");
                String train_name = in[i].substring(index + 7, next_index);
                in[i] = in[i].replaceFirst("name", "");
                index = in[i].indexOf("name");
                next_index = in[i].indexOf("\",\"code\"");
                String station_name = in[i].substring(index + 7, next_index);
                index = in[i].indexOf("station_no");
                next_index = in[i].indexOf("arrive_time");
                String station_no = in[i].substring(index + 12, next_index - 2);
                index = in[i].indexOf("arrive_time");
                next_index = in[i].indexOf("leave_time");
                String arrive_time = in[i].substring(index + 14, next_index - 3);
                index = next_index;
                next_index = in[i].indexOf("date");
                String leave_time = in[i].substring(index + 13, next_index - 3);
                index = next_index;
                next_index = in[i].indexOf("en_able");
                String date = in[i].substring(index + 7, next_index - 3);
                index = next_index;
                next_index = in[i].length();
                String en_able = in[i].substring(index + 9, next_index);
                if(en_able.equals("false"))
                    continue;
                if (date.equals(search_time)) {
                    boolean flag = true;
                    for(int j = 0 ; j < details.size() ; j++){
                        if(station_name.equals(details.get(j).getStation_name()) && arrive_time.equals(details.get(j).getArrive_time())){
                            flag = false;
                        }
                    }
                    if(flag) {
                        details.add(new Schedule_detail(station_name, train_name, arrive_time, leave_time, date, en_able, station_no));
                        schedules[i] = new schedule(station_name, train_name, arrive_time, leave_time, date, en_able, station_no);
                        if (max < schedules[i].station_no) {
                            max = schedules[i].station_no;
                            max_index = i;
                        }
                        if (min > schedules[i].station_no) {
                            min = schedules[i].station_no;
                            min_index = i;
                        }
                    }
                }
            }
            ArrayList<Schedule> schedule = new ArrayList<>();
            if (schedules[min_index] == null || schedules[max_index] == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("查询失败");
                alert.setContentText("找不到您所需要的车次，请检查您的输入是否正确！");
                alert.showAndWait();
                return;
            }
            schedule.add(new Schedule(schedules[min_index].date, search_name, schedules[min_index].station_name, schedules[max_index].station_name));
            setTable(schedule);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("查询失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
    }
}
