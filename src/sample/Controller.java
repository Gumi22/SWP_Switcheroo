package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public Button upBtn;
    public Button downBtn;
    public Label status;

    private Rollo rp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rp = new RolloProxy();
        status.textProperty().bind(new SimpleStringProperty(rp.getStatus()));
        rp.setCallback( r -> {
            //Using platform.runlater to avoid java.lang.IllegalStateException “Not on FX application thread”:
            Platform.runLater(
                    () -> {
                        Rollo ro = (Rollo)r;
                        status.textProperty().unbind();
                        status.textProperty().bind(new SimpleStringProperty(ro.getStatus()));
                    }
            );
            return null;
        });
    }


    public void up() {
        rp.Up();
    }

    public void down() {
        rp.Down();
    }
}
