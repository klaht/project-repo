package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSave { // singleton class that holds temporary data for transferring data between the controllers

    private Label savedLabel;
    private VBox savedLastParent;
    private boolean deleted;
    private String chosenDay;
    private int openWindowType;

    public int getOpenWindowType() {
        return openWindowType;
    }

    public void setOpenWindowType(int openWindowType) {
        this.openWindowType = openWindowType;
    }

    private final static DataSave INSTANCE = new DataSave();

    public Label getSavedLabel() {
        return savedLabel;
    }

    public static DataSave getInstance() {
        return INSTANCE;
    }

    public void setSavedLabel(Label savedLabel) {
        this.savedLabel = savedLabel;
    }


    public VBox getSavedLastParent() {
        return savedLastParent;
    }

    public void setSavedLastParent(VBox savedLastParent) {
        this.savedLastParent = savedLastParent;
    }

    public void setDeleted(boolean bool) {
        deleted = bool;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public String getChosenDay(){
        return chosenDay;

    }

    public void setChosenDay(String weekday){
        chosenDay = weekday;
    }
}
