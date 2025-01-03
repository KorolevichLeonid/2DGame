module com.example.firstgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.firstgame to javafx.fxml;
    exports com.example.firstgame;
}