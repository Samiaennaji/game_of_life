module org.example.game_of_life {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.game_of_life to javafx.fxml;
    exports org.example.game_of_life;
}