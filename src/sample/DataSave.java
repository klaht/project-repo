package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSave { // singleton class that holds temporary data

    private Label savedLabel;
    private VBox savedLastParent;
    private boolean deleted;

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
}
