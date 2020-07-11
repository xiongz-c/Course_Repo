import javafx.beans.property.SimpleStringProperty;

public class Order {
    private SimpleStringProperty id, train_id, end_station, start_station, user, price, statue , ticket_id , user_id;

    public Order(String id, String train_id, String end_station, String start_station, String user, String price, String statue , String ticket_id , String user_id) {
        this.id = new SimpleStringProperty(id);
        this.train_id = new SimpleStringProperty(train_id);
        this.end_station = new SimpleStringProperty(end_station);
        this.start_station = new SimpleStringProperty(start_station);
        this.user = new SimpleStringProperty(user);
        this.price = new SimpleStringProperty(price);
        this.statue = new SimpleStringProperty(statue);
        this.ticket_id = new SimpleStringProperty(ticket_id);
        this.user_id = new SimpleStringProperty(user_id);
    }

    public void setUser_id(String user_id) {
        this.user_id.set(user_id);
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id.set(ticket_id);
    }

    public void setStatue(String statue) {
        this.statue.set(statue);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setTrain_id(String train_id) {
        this.train_id.set(train_id);
    }

    public void setEnd_station(String end_station) {
        this.end_station.set(end_station);
    }

    public void setStart_station(String start_station) {
        this.start_station.set(start_station);
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public String getTrain_id() {
        return train_id.get();
    }

    public String getId() {
        return id.get();
    }

    public String getEnd_station() {
        return end_station.get();
    }

    public String getStart_station() {
        return start_station.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getStatue() {
        return statue.get();
    }

    public String getUser() {
        return user.get();
    }

    public String getTicket_id() {
        return ticket_id.get();
    }

    public String getUser_id() {
        return user_id.get();
    }
}
