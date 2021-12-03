package sample;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import javafx.stage.Stage;
import java.io.IOException;

public class Background {

    public void runInBackground(Stage stage) throws IOException {
        stage.setTitle("BreakReminder");



        FXTrayIcon trayIcon = new FXTrayIcon(stage, getClass().getResource("icon.png"));
        trayIcon.show();
        trayIcon.addTitleItem(true);
        trayIcon.addExitItem(true);


    }

}