module org.example.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.seng.gui to javafx.fxml;
    //exports org.seng.gui;
    exports org.seng.authentication;
}