package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;

public class PopupController {
    @FXML
    private TextField taskTextField, newTaskTextField;
    @FXML
    private Button newLabelButton, setTextButton, deleteButton, helpBackButton;
    @FXML private Stage stage;
    @FXML private ComboBox<String> daySelect;
    DataSave dataSave = DataSave.getInstance();

    private static final String BUTTON_STYLE = "-fx-background-color: rgb(148,148,148); -fx-background-radius: 5; -fx-border-color: black;-fx-border-radius: 5;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: rgb(169,169,169); -fx-background-radius: 5; -fx-border-color: black; -fx-border-radius: 5";

    public void initialize(){
        if (dataSave.getOpenWindowType()==0){ //  only initialize combobox if opened window is type 0, meaning that its new task window
            //add items to dropdown box
            daySelect.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
            //set default value to monday
            daySelect.getSelectionModel().select("Monday");
        }
    }


    public void backToMainWindow(){
        stage = (Stage) helpBackButton.getScene().getWindow();
        stage.close();
    }

    //add new task in main window
    public void setNewButton() {
        Label label = new Label();

        if (!newTaskTextField.getText().equals("")) {
            label.setText(newTaskTextField.getText());
        }else
            label.setText("New task");

        dataSave.setSavedLabel(label);

        stage = (Stage) newLabelButton.getScene().getWindow();
        dataSave.setChosenDay(daySelect.getSelectionModel().getSelectedItem());
        stage.close();

    }

    //set text of a task
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


    public void helpBackButtonEntered(){
        helpBackButton.setStyle(HOVERED_BUTTON_STYLE);
    }

    public void helpBackButtonExited(){
        helpBackButton.setStyle(BUTTON_STYLE);
    }

    public void newButtonEntered(){
        newLabelButton.setStyle(HOVERED_BUTTON_STYLE);
    }

    public void newButtonExited(){
        newLabelButton.setStyle(BUTTON_STYLE);
    }

    public void deleteButtonEntered(){
        deleteButton.setStyle(HOVERED_BUTTON_STYLE);
    }

    public void deleteButtonExited(){
        deleteButton.setStyle(BUTTON_STYLE);
    }

    public void saveButtonEntered(){
        setTextButton.setStyle(HOVERED_BUTTON_STYLE);
    }

    public void saveButtonExited(){
        setTextButton.setStyle(BUTTON_STYLE);
    }
}
