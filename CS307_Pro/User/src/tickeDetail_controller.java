import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class tickeDetail_controller {

    private Stage dialogstage;

    @FXML
    private Label price;

    @FXML
    private ChoiceBox<String> box;

    @FXML
    private TableView<Schedule> table;

    @FXML
    private TableColumn<Schedule, Schedule> train_id;

    @FXML
    private TableColumn<Schedule, Schedule> station_name;

    @FXML
    private TableColumn<Schedule, Schedule> arrive_time;

    @FXML
    private TableColumn<Schedule, Schedule> leave_time;

    @FXML
    private TableColumn<Schedule, Schedule> date;

    private final ObservableList<Schedule> data = FXCollections.observableArrayList();
    private final ObservableList<String> seat_level = FXCollections.observableArrayList();

    public void setDialogStage(Stage dialogstage, ArrayList<Schedule> schedule , ArrayList<String> seat_level , ArrayList<String> in_price) {
        seat seat = new seat();
        seat.setSeat(seat_level , this.seat_level);
        box.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ArrayList<String> change = new ArrayList<>();
                seat.setSeat(seat_level , change);
                String level = box.getValue();
                int i = 0;
                for(; i < change.size() ;i++){
                    if(change.get(i).equals(level))
                        break;
                }
                price.setText("价格：" + in_price.get(i));
            }
        });
        box.setItems(this.seat_level);
        data.addAll(schedule);
        train_id.setCellValueFactory(new PropertyValueFactory<>("train_id"));
        station_name.setCellValueFactory(new PropertyValueFactory<>("station_name"));
        arrive_time.setCellValueFactory(new PropertyValueFactory<>("arrive_time"));
        leave_time.setCellValueFactory(new PropertyValueFactory<>("leave_time"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(data);

        this.dialogstage = dialogstage;
    }

    public void back() {
        dialogstage.hide();
        tickController.dialogStage.show();
    }
}
