package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupController {
    @FXML
    private TextField taskTextField, newTaskTextField;
    @FXML
    private Button newLabelButton;
    private Stage stage;
    DataSave dataSave = DataSave.getInstance();
    public void setNewButton() {
        Label label = new Label();


        if (!newTaskTextField.getText().equals("")) {
            label.setText(newTaskTextField.getText());
        }else
            label.setText("New task");


        dataSave.setSavedLabel(label);

        stage = (Stage) newLabelButton.getScene().getWindow();
        stage.close();

    }

    public void setButtonText (){
        Label currentNode = dataSave.getSavedLabel();
        if (!taskTextField.getText().equals("")) {
            currentNode.setText(taskTextField.getText());
        }

        stage = (Stage) taskTextField.getScene().getWindow();
        stage.close();
    }

    public void popupDeleteTask() {
        dataSave.setDeleted(true);

        stage = (Stage) taskTextField.getScene().getWindow();
        stage.close();

    }
}
