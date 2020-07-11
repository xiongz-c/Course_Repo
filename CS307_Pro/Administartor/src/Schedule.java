import javafx.beans.property.SimpleStringProperty;

public class Schedule {
    private SimpleStringProperty date , train_id , start_station , end_station;

    Schedule(String date , String train_id , String Start_station , String end_station){
        this.date = new SimpleStringProperty(date);
        this.train_id = new SimpleStringProperty(train_id);
        this.start_station = new SimpleStringProperty(Start_station);
        this.end_station = new SimpleStringProperty(end_station);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setStart_station(String start_station) {
        this.start_station.set(start_station);
    }

    public void setEnd_station(String end_station) {
        this.end_station.set(end_station);
    }

    public void setTrain_id(String train_id) {
        this.train_id.set(train_id);
    }

    public String getDate() {
        return date.get();
    }

    public String getStart_station() {
        return start_station.get();
    }

    public String getTrain_id() {
        return train_id.get();
    }

    public String getEnd_station() {
        return end_station.get();
    }
}
