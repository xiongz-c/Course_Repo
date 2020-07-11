import javafx.beans.property.SimpleStringProperty;

public class User {

    private final SimpleStringProperty ID;

    private final SimpleStringProperty name;

    private final SimpleStringProperty phoneNumber;

    private final SimpleStringProperty password;



    public User(String ID, String name, String phoneNumber , String password) {
        this.ID = new SimpleStringProperty(ID);
        this.name = new SimpleStringProperty(name);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.password = new SimpleStringProperty(password);
    }

    public String getID() {
        return ID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getPassword(){
        return password.get();
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setPassword(String password){
        this.password.set(password);
    }
}