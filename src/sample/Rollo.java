package sample;


import javafx.util.Callback;

public interface Rollo {

    void Up();

    void Down();

    String getStatus();

    void setFinishedMovingCallback(Callback cb);

}
