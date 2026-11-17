module CinemaUQ {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    opens ViewController to javafx.fxml;

    opens model to javafx.base;
    opens composite to javafx.base;
    opens Factory to javafx.base;
    opens decorator to javafx.base;

    exports App;

    exports ViewController;
    exports model;
    exports composite;
    exports decorator;
    exports fachada;
    exports Factory;
    exports iterator;
    exports proxy;
}