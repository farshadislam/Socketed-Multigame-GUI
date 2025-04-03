module org.example.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.seng.gui to javafx.fxml;
    exports org.seng.gui;
}