package sample;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DataSave {

    private Label savedLabel;
    private VBox savedLastParent;
    private boolean deleted;


    public Label getSavedLabel() {
        return savedLabel;
    }

    private final static DataSave INSTANCE = new DataSave();

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
