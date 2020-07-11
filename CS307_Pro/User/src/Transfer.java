import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Transfer {
    private SimpleStringProperty train_no_1, train_no_2, first_Station, second_Station, third_Station, first_arrive_time, second_arrive_time, first_start_time, second_start_time, date;
    private SimpleStringProperty first_arrive_time_change, second_arrive_time_change, first_start_time_change, second_start_time_change;

    Transfer(String train_no_1, String train_no_2, String first_Station, String second_Station, String third_Station, String first_arrive_time, String second_arrive_time, String first_start_time, String second_start_time, String date) {
        this.train_no_1 = new SimpleStringProperty(train_no_1);
        this.train_no_2 = new SimpleStringProperty(train_no_2);
        this.first_Station = new SimpleStringProperty(first_Station);
        this.second_Station = new SimpleStringProperty(second_Station);
        this.third_Station = new SimpleStringProperty(third_Station);
        this.first_arrive_time = new SimpleStringProperty(first_arrive_time);
        this.second_arrive_time = new SimpleStringProperty(second_arrive_time);
        this.first_start_time = new SimpleStringProperty(first_start_time);
        this.second_start_time = new SimpleStringProperty(second_start_time);
        this.date = new SimpleStringProperty(date);
        first_start_time_change = setChange(first_start_time);
        first_arrive_time_change = setChange(first_arrive_time);
        second_arrive_time_change = setChange(second_arrive_time);
        second_start_time_change = setChange(second_start_time);
    }

    public SimpleStringProperty setChange(String first_arrive_time){
        int h1 = Integer.parseInt(first_arrive_time.substring(0, first_arrive_time.indexOf(":")));
        int h11 = h1 % 24;
        h1 = h1 / 24;
        if (h1 > 1)
            return new SimpleStringProperty("出发后第" + h1 + "天" + h11 + first_arrive_time.substring(first_arrive_time.indexOf(":"), first_arrive_time.length()));
        else if (h1 == 1)
            return new SimpleStringProperty("次日" + h11 + first_arrive_time.substring(first_arrive_time.indexOf(":"), first_arrive_time.length()));
        else
            return new SimpleStringProperty("当天" + h11 + first_arrive_time.substring(first_arrive_time.indexOf(":"), first_arrive_time.length()));
    }

    public void setFirst_arrive_time_change(String first_arrive_time_change) {
        this.first_arrive_time_change.set(first_arrive_time_change);
    }

    public void setFirst_start_time_change(String first_start_time_change) {
        this.first_start_time_change.set(first_start_time_change);
    }

    public void setSecond_arrive_time_change(String second_arrive_time_change) {
        this.second_arrive_time_change.set(second_arrive_time_change);
    }

    public void setSecond_start_time_change(String second_start_time_change) {
        this.second_start_time_change.set(second_start_time_change);
    }

    public void setFirst_Station(String first_Station) {
        this.first_Station.set(first_Station);
    }

    public void setTrain_no_1(String train_no_1) {
        this.train_no_1.set(train_no_1);
    }

    public void setTrain_no_2(String train_no_2) {
        this.train_no_2.set(train_no_2);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setFirst_arrive_time(String first_arrive_time) {
        this.first_arrive_time.set(first_arrive_time);
    }

    public void setFirst_start_time(String first_start_time) {
        this.first_start_time.set(first_start_time);
    }

    public void setSecond_arrive_time(String second_arrive_time) {
        this.second_arrive_time.set(second_arrive_time);
    }

    public void setSecond_start_time(String second_start_time) {
        this.second_start_time.set(second_start_time);
    }

    public void setSecond_Station(String second_Station) {
        this.second_Station.set(second_Station);
    }

    public void setThird_Station(String third_Station) {
        this.third_Station.set(third_Station);
    }

    public String getFirst_Station() {
        return first_Station.get();
    }

    public String getTrain_no_1() {
        return train_no_1.get();
    }

    public String getTrain_no_2() {
        return train_no_2.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getFirst_arrive_time() {
        return first_arrive_time.get();
    }

    public String getFirst_start_time() {
        return first_start_time.get();
    }

    public String getSecond_arrive_time() {
        return second_arrive_time.get();
    }

    public String getSecond_start_time() {
        return second_start_time.get();
    }

    public String getSecond_Station() {
        return second_Station.get();
    }

    public String getThird_Station() {
        return third_Station.get();
    }

    public String getFirst_arrive_time_change() {
        return first_arrive_time_change.get();
    }

    public String getFirst_start_time_change() {
        return first_start_time_change.get();
    }

    public String getSecond_arrive_time_change() {
        return second_arrive_time_change.get();
    }

    public String getSecond_start_time_change() {
        return second_start_time_change.get();
    }
}
