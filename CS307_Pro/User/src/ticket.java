import javafx.beans.property.SimpleStringProperty;

public class ticket {
    private SimpleStringProperty start_time , start_station , end_station , ticket1 , ticket2 , ticket3 , price;

    public ticket(String start_time , String start_station , String end_station , String ticket1 , String ticket2 , String ticket3 , String price){
        this.start_station = new SimpleStringProperty(start_station);
        this.end_station = new SimpleStringProperty(end_station);
        this.start_time = new SimpleStringProperty(start_time);
        this.ticket1 = new SimpleStringProperty(ticket1);
        this.ticket2 = new SimpleStringProperty(ticket2);
        this.ticket3 = new SimpleStringProperty(ticket3);
        this.price = new SimpleStringProperty(price);
    }

    public void setStart_time(String start_time) {
        this.start_time.set(start_time);
    }

    public void setStart_station(String start_station) {
        this.start_station.set(start_station);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setEnd_station(String end_station) {
        this.end_station.set(end_station);
    }

    public void setTicket1(String ticket1) {
        this.ticket1.set(ticket1);
    }

    public void setTicket2(String ticket2) {
        this.ticket2.set(ticket2);
    }

    public void setTicket3(String ticket3) {
        this.ticket3.set(ticket3);
    }

    public String getStart_time() {
        return start_time.get();
    }

    public String getStart_station() {
        return start_station.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getEnd_station() {
        return end_station.get();
    }

    public String getTicket1() {
        return ticket1.get();
    }

    public String getTicket2() {
        return ticket2.get();
    }

    public String getTicket3() {
        return ticket3.get();
    }
}
