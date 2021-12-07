package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class Controller{

    @FXML private Button saveButton, loadButton, newTaskButton;

    @FXML private Pane deleteTask;

    @FXML private AnchorPane mainPane;


    @FXML private VBox monVBox, tuesVBox, wedVBox, thurVBox, friVBox, saturVBox, sunVBox;

    @FXML private final ArrayList<Label> allTasksList = new ArrayList<>();

    private Stage stage;
    private Parent root;

    private final int labelWidth = 150;
    private final int labelHeight = 150;

    DataSave dataSave = DataSave.getInstance();

    private boolean unSavedChanges;
    private static final String BUTTON_STYLE = "-fx-background-color: lightgreen; -fx-background-radius: 5;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: rgb(144, 255, 144);";

    public void initialize() {
        load();
        buttonStyling();
    }

    public void buttonStyling() {
        newTaskButton.setStyle(BUTTON_STYLE);
        newTaskButton.setOnMouseEntered(event -> newTaskButton.setStyle(HOVERED_BUTTON_STYLE));
        newTaskButton.setOnMouseExited(event -> newTaskButton.setStyle(BUTTON_STYLE));

        saveButton.setStyle(BUTTON_STYLE);
        saveButton.setOnMouseEntered(event -> saveButton.setStyle(HOVERED_BUTTON_STYLE));
        saveButton.setOnMouseExited(event -> saveButton.setStyle(BUTTON_STYLE));

        loadButton.setStyle(BUTTON_STYLE);
        loadButton.setOnMouseEntered(event -> loadButton.setStyle(HOVERED_BUTTON_STYLE));
        loadButton.setOnMouseExited(event -> loadButton.setStyle(BUTTON_STYLE));

    }






    public void newTask() { // Opens the naming popup and creates a new task label
        unSavedChanges = true;
        if (monVBox.getChildren().size() <= 17) {
            try {
                root = FXMLLoader.load(getClass().getResource("newTaskPopup.fxml"));
                stage = new Stage();

                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(monVBox.getScene().getWindow());
                stage.showAndWait();
                Label label = dataSave.getSavedLabel();

                try {
                    monVBox.getChildren().add(label);
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




    public void addLabelFunctionality(Label label){ // makes nodes draggable, clickable and makes them snap to targets
        label.setPrefWidth(labelWidth);
        //label.setPrefHeight(labelHeight);
        label.setStyle("-fx-background-color:cyan; -fx-font: 16 arial; -fx-border-color: black;");
        label.setMaxHeight(labelHeight);
        label.setWrapText(true);




        label.addEventHandler(MouseEvent.MOUSE_DRAGGED, e-> label.relocate(label.getLayoutX() + e.getX() - label.getLayoutBounds().getWidth()/2,
                label.getLayoutY() + e.getY()- label.getLayoutBounds().getHeight()/2));

        label.addEventHandler(MouseEvent.MOUSE_PRESSED, e-> {
            if (e.isPrimaryButtonDown()) {
                double currentX;
                double currentY;
                if (!mainPane.getChildren().contains(label)) {
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
                    root = FXMLLoader.load(getClass().getResource("textPopup.fxml"));
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner(label.getScene().getWindow());
                    label.setWrapText(true);
                    stage.showAndWait();

                    if (dataSave.getDeleted()) { //if user deletes task with the delete button
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
            double currentChildY;
            boolean lowest = true;
            Pane currentHover = currentlyHovered(label);


            if (currentHover != null) {
                if (currentHover == deleteTask) {
                    if (deleteTask.getChildren().size() > 10) {
                        deleteTask.getChildren().clear();
                    }
                    allTasksList.remove(label);
                    currentHover.getChildren().remove(label);
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

    public Pane currentlyHovered(Node node) { // checks which VBox (weekday) parameter node is in

        double centerX = node.getLayoutX() + (labelWidth / 2);
        double centerY = node.getLayoutY() + (labelHeight/2);

        if (((centerX > deleteTask.getLayoutX() && centerX < deleteTask.getLayoutX() + deleteTask.getWidth())
                && centerY >= deleteTask.getLayoutY() && centerY <= deleteTask.getLayoutY() + deleteTask.getHeight())) {
            return deleteTask;
        } else if (centerX > monVBox.getLayoutX() && centerX < monVBox.getLayoutX() + monVBox.getWidth()) {
            return monVBox;
        }else if (centerX > tuesVBox.getLayoutX() && centerX < tuesVBox.getLayoutX() + tuesVBox.getWidth()) {
            return tuesVBox;
        }else if (centerX > wedVBox.getLayoutX() && centerX < wedVBox.getLayoutX() + wedVBox.getWidth()){
            return wedVBox;
        }else if(centerX > thurVBox.getLayoutX() && centerX < thurVBox.getLayoutX() + thurVBox.getWidth()) {
            return thurVBox;
        } else if (centerX > friVBox.getLayoutX() && centerX < friVBox.getLayoutX() + friVBox.getWidth()) {
            return friVBox;
        }else if (centerX > saturVBox.getLayoutX() && centerX < saturVBox.getLayoutX() + saturVBox.getWidth()) {
            return saturVBox;
        }else if (centerX > sunVBox.getLayoutX() && centerX < sunVBox.getLayoutX() + sunVBox.getWidth()) {
            return sunVBox;
        }else{
            return monVBox;
        }

    }





    public void save () {
        File file = new File("TaskList.json");
        unSavedChanges = false;

        file.delete();


        if (file != null) {
            TaskData taskData = new TaskData();
            try {
                JsonWriter writer = new JsonWriter(new FileWriter(file));

                writer.beginArray();
                for (int i = 0; i < allTasksList.size(); i++) {

                            if (deleteTask.getChildren().contains(allTasksList.get(i))) {
                                //task is deleted, dont save it
                            }else {


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



                }
                writer.endArray();
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }

    }

    public void load() {
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

            if (file != null) {
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




}
