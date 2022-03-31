package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Controller{

    @FXML private Button saveButton, loadButton, newTaskButton, clearButton, helpButton;
    @FXML private Pane deleteTask;
    @FXML private AnchorPane mainPane;
    @FXML private VBox monVBox, tuesVBox, wedVBox, thurVBox, friVBox, saturVBox, sunVBox;
    @FXML private final ArrayList<Label> allTasksList = new ArrayList<>();
    @FXML private Text savedText, deletedText;

    private Stage stage;
    private Parent root;

    private final int labelWidth = 150;
    private final int labelHeight = 150;

    DataSave dataSave = DataSave.getInstance();

    private boolean unSavedChanges;
    private static final String BUTTON_STYLE = "-fx-background-color: rgb(148,148,148); -fx-background-radius: 5; -fx-border-color: black;-fx-border-radius: 5;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: rgb(169,169,169); -fx-background-radius: 5; -fx-border-color: black; -fx-border-radius: 5";
    private static final String LABEL_STYLE = "-fx-background-color: rgb(148,148,148); -fx-font: 16 arial; -fx-background-radius: 2; -fx-border-color: black;-fx-border-radius: 2;";
    private static final String HOVERED_LABEL_STYLE = "-fx-background-color: rgb(169,169,169); -fx-font: 16 arial; -fx-background-radius: 2; -fx-border-color: black; -fx-border-radius: 2;";

    public void initialize() {
        load();
        buttonStyling();
    }

    //open the instructions window
    public void openHelp() throws IOException {
        dataSave.setOpenWindowType(2);
        root = FXMLLoader.load(getClass().getResource("helpWindow.fxml"));
        stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(monVBox.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
    }

    public VBox getChosenDay(String day){
        switch (day){
            case "Monday":
                return monVBox;
            case "Tuesday":
                return tuesVBox;
            case "Wednesday":
                return wedVBox;
            case "Thursday":
                return thurVBox;
            case "Friday":
                return friVBox;
            case "Saturday":
                return saturVBox;
            case "Sunday":
                return sunVBox;
            default:
                return null;
        }
    }

    //set button styling (button colour changes on mouse hover)
    public void buttonStyling() {
        newTaskButton.setOnMouseEntered(event -> newTaskButton.setStyle(HOVERED_BUTTON_STYLE));
        newTaskButton.setOnMouseExited(event -> newTaskButton.setStyle(BUTTON_STYLE));

        saveButton.setOnMouseEntered(event -> saveButton.setStyle(HOVERED_BUTTON_STYLE));
        saveButton.setOnMouseExited(event -> saveButton.setStyle(BUTTON_STYLE));

        loadButton.setOnMouseEntered(event -> loadButton.setStyle(HOVERED_BUTTON_STYLE));
        loadButton.setOnMouseExited(event -> loadButton.setStyle(BUTTON_STYLE));

        clearButton.setOnMouseEntered(event -> clearButton.setStyle(HOVERED_BUTTON_STYLE));
        clearButton.setOnMouseExited(event -> clearButton.setStyle(BUTTON_STYLE));

        helpButton.setOnMouseEntered(event -> helpButton.setStyle(HOVERED_BUTTON_STYLE));
        helpButton.setOnMouseExited(event -> helpButton.setStyle(BUTTON_STYLE));


    }

    // Opens the naming popup and creates a new task label
    public void newTask() {
        unSavedChanges = true;
        if (monVBox.getChildren().size() <= 17) {
            try {
                dataSave.setOpenWindowType(0);
                root = FXMLLoader.load(getClass().getResource("newTaskPopup.fxml"));
                stage = new Stage();                dataSave.setOpenWindowType(0);

                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(monVBox.getScene().getWindow());
                stage.setResizable(false);
                stage.showAndWait();
                Label label = dataSave.getSavedLabel();


                try {
                    //get day chosen by user with dropdown menu
                    getChosenDay(dataSave.getChosenDay()).getChildren().add(label);
                    allTasksList.add(label);
                    addLabelFunctionality(label);
                }catch (Exception e) {
                    System.out.println("Creating new label was cancelled");
                }
            } catch (IOException error) {
                System.out.println(error);
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Current day is full");
            alert.setContentText("Delete or remove tasks from this day to add new ones");
            alert.showAndWait();
        }
    }

    // make nodes draggable, clickable and snap to targets (weekdays)
    public void addLabelFunctionality(Label label){

        label.setPrefWidth(labelWidth);

        label.setStyle(LABEL_STYLE);
        label.setOnMouseEntered(event -> label.setStyle(HOVERED_LABEL_STYLE));
        label.setOnMouseExited(event -> label.setStyle(LABEL_STYLE));

        label.setMaxHeight(labelHeight);
        label.setWrapText(true);

        label.addEventHandler(MouseEvent.MOUSE_DRAGGED, e-> {
            label.relocate(label.getLayoutX() + e.getX() - label.getLayoutBounds().getWidth() / 2, label.getLayoutY() + e.getY() - label.getLayoutBounds().getHeight() / 2);
            label.setStyle(HOVERED_LABEL_STYLE);
            if ((e.getSceneX() >= deleteTask.getLayoutX() && e.getSceneX() <= deleteTask.getLayoutX() + deleteTask.getWidth()) &&
                    e.getSceneY() >= deleteTask.getLayoutY() && e.getSceneY() <= deleteTask.getLayoutY() + deleteTask.getHeight()){
                    deleteTask.setStyle("-fx-background-color: rgb(207,0,0); -fx-background-radius: 5; -fx-border-color: rgb(0,0,0); -fx-border-radius:5;");
                } else{
                    deleteTask.setStyle("-fx-background-color: GRAY; -fx-background-radius: 5; -fx-border-color: rgb(0,0,0); -fx-border-radius:5;");
            }
        });

        label.addEventHandler(MouseEvent.MOUSE_PRESSED, e-> {
            if (e.isPrimaryButtonDown()) {

                double currentX;
                double currentY;
                if (!mainPane.getChildren().contains(label)) {
                    label.setStyle(HOVERED_LABEL_STYLE);
                    dataSave.setSavedLastParent((VBox)label.getParent());
                    currentX = label.localToScene(label.getBoundsInLocal()).getMinX();
                    currentY = label.localToScene(label.getBoundsInLocal()).getMinY();
                    mainPane.getChildren().add(label);
                    label.relocate(currentX, currentY);

                }
            }else if (e.isSecondaryButtonDown()) {
                try {
                    dataSave.setSavedLabel(label);
                    dataSave.setSavedLastParent((VBox) label.getParent());
                    stage = new Stage();
                    dataSave.setOpenWindowType(1);
                    root = FXMLLoader.load(getClass().getResource("textPopup.fxml"));
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(label.getScene().getWindow());
                    stage.setResizable(false);
                    label.setWrapText(true);
                    stage.showAndWait();

                    if (dataSave.getDeleted()) { //if user deletes task with the delete button
                        deletedText.setVisible(true);
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), deletedText);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0);
                        fadeTransition.play();
                        Label currentLabel = dataSave.getSavedLabel();
                        allTasksList.remove(currentLabel);
                        dataSave.getSavedLastParent().getChildren().remove(currentLabel);
                        dataSave.setDeleted(false);

                    }
                    unSavedChanges = true;

                }catch (IOException error) {
                    System.out.println(error);
                }

            }
        });

        label.addEventHandler(MouseEvent.MOUSE_RELEASED, e-> {
            label.setStyle(LABEL_STYLE);
            double currentChildY;
            boolean lowest = true;

            Pane currentHover = currentlyHovered(e.getSceneX(), e.getSceneY());

            if (currentHover != null) {
                if (currentHover == deleteTask) {

                    allTasksList.remove(label);
                    currentHover.getChildren().remove(label);
                    deleteTask.setStyle("-fx-background-color: GRAY; -fx-background-radius: 5; -fx-border-color: rgb(0,0,0); -fx-border-radius:5;");
                    deletedText.setVisible(true);
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), deletedText);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0);
                    fadeTransition.play();
                }
                if (currentHover.getChildren().size() < 18) {


                    for (int i = 0; i < currentHover.getChildren().size(); i++) {
                        //the y value of child node (inside a parent) relative to scene
                        currentChildY = currentHover.getChildren().get(i).localToScene(currentHover.getChildren().get(i).getBoundsInLocal()).getMinY();
                        if (currentChildY > label.getLayoutY()) {
                            currentHover.getChildren().remove(label);
                            currentHover.getChildren().add(i, label);
                            lowest = false;
                            break;
                        }
                    }
                    if (lowest) { // if mouse pos is lowest of all nodes
                        currentHover.getChildren().add(label);
                    }
                }else {
                    dataSave.getSavedLastParent().getChildren().add(label);
                }
            }
            unSavedChanges = true;
        });
    }

    public Pane currentlyHovered(Double x, double y) { // checks which VBox (weekday) the coordinates x and y are in. Bad implementation, needs to be improved

        if (((x > deleteTask.getLayoutX() && x < deleteTask.getLayoutX() + deleteTask.getWidth())
                && y >= deleteTask.getLayoutY())) {
            return deleteTask;
        } else if (x > monVBox.getLayoutX() && x < monVBox.getLayoutX() + monVBox.getWidth()) {
            return monVBox;
        }else if (x > tuesVBox.getLayoutX() && x < tuesVBox.getLayoutX() + tuesVBox.getWidth()) {
            return tuesVBox;
        }else if (x > wedVBox.getLayoutX() && x < wedVBox.getLayoutX() + wedVBox.getWidth()){
            return wedVBox;
        }else if(x > thurVBox.getLayoutX() && x < thurVBox.getLayoutX() + thurVBox.getWidth()) {
            return thurVBox;
        } else if (x > friVBox.getLayoutX() && x < friVBox.getLayoutX() + friVBox.getWidth()) {
            return friVBox;
        }else if (x > saturVBox.getLayoutX() && x < saturVBox.getLayoutX() + saturVBox.getWidth()) {
            return saturVBox;
        }else if (x > sunVBox.getLayoutX() && x < sunVBox.getLayoutX() + sunVBox.getWidth()) {
            return sunVBox;
        }else{
            return monVBox;
        }

    }

    //save current tasks and their locations
    public void save () {
        File file = new File("TaskList.json");
        unSavedChanges = false;

        file.delete();


        TaskData taskData = new TaskData();
        try {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.beginArray();

            for (int i = 0; i < allTasksList.size(); i++) {
                    Label label = allTasksList.get(i);

                    taskData.setName(label.getText());
                    taskData.setVisibility(label.isVisible());
                    taskData.setLocX(label.getLayoutX());
                    taskData.setLocY(label.getLayoutY());
                    taskData.setParentID(label.getParent().getId());

                    //saving values to json file
                    writer.beginObject();
                    writer.name("name").value(taskData.getName());
                    writer.name("locX").value(taskData.getLocX());
                    writer.name("locY").value(taskData.getLocY());
                    writer.name("visibility").value(taskData.isVisibility());
                    writer.name("parentID").value(taskData.getParentID());
                    writer.endObject();
                }

            writer.endArray();
            writer.close();
            //animation to showcase that save was succesful
            savedText.setVisible(true);
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), savedText);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0);
            fadeTransition.play();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //clear all tasks
    public void clear(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("Clearing list confirmation");
        alert.setContentText("Are you sure you want load clear the list? All unsaved progress will be lost");

        if (alert.showAndWait().get() == ButtonType.YES){
            unSavedChanges = true;
            allTasksList.clear();
            monVBox.getChildren().clear();
            tuesVBox.getChildren().clear();
            wedVBox.getChildren().clear();
            thurVBox.getChildren().clear();
            friVBox.getChildren().clear();
            saturVBox.getChildren().clear();
            sunVBox.getChildren().clear();
        }

    }

    public void load() {
        //check if there are unsaved changes before loading
        if (unSavedChanges) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("unsaved changes!");

            alert.setContentText("Are you sure you want load? All unsaved progress will be lost");
            if (alert.showAndWait().get() == ButtonType.YES){
                unSavedChanges = false;
                load();
            }
        //no unsaved changes
        } else {
            int a = 0;

            allTasksList.clear();
            monVBox.getChildren().clear();
            tuesVBox.getChildren().clear();
            wedVBox.getChildren().clear();
            thurVBox.getChildren().clear();
            friVBox.getChildren().clear();
            saturVBox.getChildren().clear();
            sunVBox.getChildren().clear();

            File file = new File("TaskList.json");

            try {
                Reader reader = Files.newBufferedReader(Paths.get("TaskList.json"));
                List<TaskData> tasks = new Gson().fromJson(reader, new TypeToken<List<TaskData>>() {
                }.getType());

                for (int i = 0; i < tasks.size(); i++) {

                    Label label = new Label(tasks.get(a).getName());
                    label.setVisible(tasks.get(a).isVisibility());
                    label.setLayoutX(tasks.get(a).getLocX());
                    label.setLayoutY(tasks.get(a).getLocY());
                    allTasksList.add(label);
                    addLabelFunctionality(label);

                    mainPane.getChildren().remove(label);
                    mainPane.getChildren().add(label);

                    //find in which
                    switch (tasks.get(a).getParentID()) {
                        case "monVBox":
                            monVBox.getChildren().add(label);
                            break;
                        case "tuesVBox":
                            tuesVBox.getChildren().add(label);
                            break;
                        case "wedVBox":
                            wedVBox.getChildren().add(label);
                            break;
                        case "thurVBox":
                            thurVBox.getChildren().add(label);
                            break;
                        case "friVBox":
                            friVBox.getChildren().add(label);
                            break;
                        case "saturVBox":
                            saturVBox.getChildren().add(label);
                            break;
                        case "sunVBox":
                            sunVBox.getChildren().add(label);
                            break;
                        default:
                            break;
                    }
                    a++;
                }
                reader.close();
                unSavedChanges = false;

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
