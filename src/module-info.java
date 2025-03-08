module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;


    opens gui to javafx.fxml;
    exports gui;
}