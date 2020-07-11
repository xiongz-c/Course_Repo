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
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class ChangeDetail {
    private Stage dialogStage;

    @FXML
    private TableView<Schedule_detail> table;

    @FXML
    private TableColumn<Schedule_detail, Schedule_detail> station_name;

    @FXML
    private TableColumn<Schedule_detail, Schedule_detail> arrive_time;

    @FXML
    private TableColumn<Schedule_detail, Schedule_detail> leave_time;

    @FXML
    private TableColumn<Schedule_detail, Schedule_detail> date;

    @FXML
    private TableColumn<Schedule_detail, Schedule_detail> train_id;

    @FXML
    private TextField adder_name;

    @FXML
    private TextField adder_arrive_time;

    @FXML
    private TextField adder_leave_time;

    @FXML
    private TextField adder_date;

    public Main_adiminstrator main = new Main_adiminstrator();

    final ObservableList<Schedule_detail> data = FXCollections.observableArrayList();


    public void back() {
        changeSchedule.dialogStage.show();
        this.dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage, ArrayList<Schedule_detail> schedule) {
        setArray(schedule);
        this.dialogStage = dialogStage;
    }

    public void setArray(ArrayList<Schedule_detail> schedule) {
        data.clear();
        data.addAll(schedule);
        data.sort(new Comparator<Schedule_detail>() {
            @Override
            public int compare(Schedule_detail o1, Schedule_detail o2) {
                if (o1.getEn_able().equals("false"))
                    return 1;
                if (o2.getEn_able().equals("false"))
                    return 1;
                return Integer.parseInt(o1.getStation_no()) - Integer.parseInt(o2.getStation_no());
            }
        });
        station_name.setCellValueFactory(new PropertyValueFactory<>("station_name"));
        arrive_time.setCellValueFactory(new PropertyValueFactory<>("arrive_time"));
        leave_time.setCellValueFactory(new PropertyValueFactory<>("leave_time"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        train_id.setCellValueFactory(new PropertyValueFactory<>("train_id"));
        table.setItems(data);
    }

    public void addStation() {
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        String name = adder_name.getText();
        String arrive_time = adder_arrive_time.getText();
        String leave_time = adder_leave_time.getText();
        String date = adder_date.getText();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Station/query/name").ignoreContentType(true);
        con.data("name", name);
        try {
            Document doc = con.post();
            if(doc.body().text().length() == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("插入失败");
                alert.setContentText("不存在此站点！");
                alert.showAndWait();
                return;
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("添加失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
            return;
        }
        if (arrive_time.length() != 5 || leave_time.length() != 5 || date.length() != 10) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先检查输入日期时间是否符合格式！");
            alert.showAndWait();
            return;
        }
        if (arrive_time.charAt(2) != ':' || leave_time.charAt(2) != ':' || date.charAt(4) != '-' || date.charAt(7) != '-') {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先检查输入日期时间是否符合格式！");
            alert.showAndWait();
            return;
        }
        if (!check(arrive_time, leave_time, date)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("插入失败");
            alert.setContentText("请先检查输入日期时间是否有误！");
            alert.showAndWait();
            return;
        }
        Schedule_detail new_one = new Schedule_detail(name, data.get(0).getTrain_id(), arrive_time, leave_time, date, "True", "-1");
        con = Jsoup.connect("http://localhost:8081/32106/Schedule/delete").ignoreContentType(true);
        con.data("train_name", data.get(0).getTrain_id());
        con.data("date", data.get(0).getDate());
        data.add(new_one);
        data.sort(new Comparator<Schedule_detail>() {
            @Override
            public int compare(Schedule_detail o1, Schedule_detail o2) {
                if (!o1.getDate().equals(o2.getDate())) {
                    if (!o1.getDate().substring(0, 4).equals(o2.getDate().substring(0, 4)))
                        return Integer.parseInt(o1.getDate().substring(0, 4)) - Integer.parseInt(o2.getDate().substring(0, 4));
                    if (!o1.getDate().substring(5, 7).equals(o2.getDate().substring(5, 7)))
                        return Integer.parseInt(o1.getDate().substring(5, 7)) - Integer.parseInt(o2.getDate().substring(5, 7));
                    return Integer.parseInt(o1.getDate().substring(8, 10)) - Integer.parseInt(o2.getDate().substring(8, 10));
                }
                if (!o1.getArrive_time().substring(0, 2).equals(o2.getArrive_time().substring(0, 2)))
                    return Integer.parseInt(o1.getArrive_time().substring(0, 2)) - Integer.parseInt(o2.getArrive_time().substring(0, 2));
                return Integer.parseInt(o1.getArrive_time().substring(3, 5)) - Integer.parseInt(o2.getArrive_time().substring(3, 5));
            }
        });
        try {
            Document doc = con.post();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < data.size(); i++) {
                buffer.append(data.get(i).getTrain_id() + "," + data.get(i).getStation_name() + "," + data.get(i).getArrive_time() + "," + data.get(i).getLeave_time() + "," + (i + 1) + "," + "True" + "," + data.get(i).getDate());
                buffer.append('\n');
            }
            System.out.println(buffer.toString());
            con = Jsoup.connect("http://localhost:8081/32106/Schedule/create").ignoreContentType(true);
            con.data("text", buffer.toString());
            if ((doc = con.post()).body().text().equals("false")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("添加失败");
                alert.setContentText("请检查您的信息是否有误！");
                alert.showAndWait();
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("添加成功");
                alert.setContentText("恭喜您，添加成功！");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("添加失败");
            alert.setContentText("请检查网络！");
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    public boolean check(String arrive_time, String leave_time, String date) {
        int h1, h2, m1, m2, y, m, d;
        try {
            h1 = Integer.parseInt(arrive_time.substring(0, 2));
            h2 = Integer.parseInt(leave_time.substring(0, 2));
            m1 = Integer.parseInt(arrive_time.substring(3, 5));
            m2 = Integer.parseInt(leave_time.substring(3, 5));
            y = Integer.parseInt(date.substring(0, 4));
            m = Integer.parseInt(date.substring(5, 7));
            d = Integer.parseInt(date.substring(8, 10));
        } catch (Exception e) {
            return false;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            Date a = format.parse(date);
        } catch (Exception ex) {
            System.out.println(1);
            return false;
        }
        if (h1 < 0 || h2 < 0 || m1 < 0 || m2 < 0 || y <= 0 || m <= 0 || d <= 0 || h1 > 23 || h2 > 23 || m1 > 59 || m2 > 59) {
            System.out.println(2);
            return false;
        }
        if (h1 != 23 && h2 != 0) {
            if (h1 > h2) {
                System.out.println(3);
                return false;
            }
            if (h1 == h2 && m1 > m2) {
                System.out.println(4);
                return false;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).getDate().equals(date))
                continue;
            if (data.get(i).getArrive_time().equals("----"))
                data.get(i).setArrive_time(data.get(i).getLeave_time());
            int ch1 = Integer.parseInt(data.get(i).getArrive_time().substring(0, 2));
            int ch2 = Integer.parseInt(data.get(i).getLeave_time().substring(0, 2));
            int cm1 = Integer.parseInt(data.get(i).getArrive_time().substring(3, 5));
            int cm2 = Integer.parseInt(data.get(i).getLeave_time().substring(3, 5));
            if (h1 == ch2 && m1 == cm2) {
                System.out.println(5);
                return false;
            }
            if (h2 == ch1 && m2 == cm1) {
                System.out.println(6);
                return false;
            }
            if (h1 == ch1 && m1 == cm1) {
                System.out.println(h1 + " " + ch1 + " " + m1 + " " + cm1);
                System.out.println(7);
                return false;
            }
            if (h2 == ch2 && m2 == cm2) {

                System.out.println(8);
                return false;
            }
            if (h1 < ch1 || (h1 == ch1 && m1 < cm1)) {
                if (h2 > ch1 || (h2 == ch1 && m2 > cm1)) {
                    System.out.println(9);
                    return false;
                }
            }
            if (h2 > ch2 || (h2 == ch2 && m2 > cm2)) {
                if (h1 < ch2 || (h1 == ch2 && m1 < cm2)) {

                    System.out.println(10);
                    return false;
                }
            }
            if (h1 > ch1 || (h1 == ch1 && m1 > cm1)) {
                if (h2 < ch2 || (h2 == ch2 && m2 < cm2))
                    return false;
            }
        }
        return true;
    }


    public void delete() {
        if (Main_adiminstrator.PhoneNumber == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("删除失败");
            alert.setContentText("请先登录！");
            alert.showAndWait();
            return;
        }
        Schedule_detail station = table.getSelectionModel().getSelectedItem();
        Connection con = Jsoup.connect("http://localhost:8081/32106/Schedule/setEnable").ignoreContentType(true);
        con.data("train", station.getTrain_id());
        con.data("station", station.getStation_name());
        con.data("date", station.getDate());
        con.data("en_able", "false");
        try {
            Document doc = con.post();
            if (doc.body().text().equals("false")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("删除失败");
                alert.setContentText("请检查信息！");
                alert.showAndWait();
            } else {
                data.remove(station);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("删除成功");
                alert.setContentText("删除成功！");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("删除失败");
            alert.setContentText("请查看网络！");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

}
