module CinemaUQ {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens app to javafx.fxml;
    exports app;

    opens composite to javafx.fxml;
    exports composite;

    opens decorator to javafx.fxml;
    exports decorator;

    opens fachada to javafx.fxml;
    exports fachada;

    opens factory to javafx.fxml;
    exports factory;

    opens iterator to javafx.fxml;
    exports iterator;

    opens model to javafx.fxml;
    exports model;

    opens proxy to javafx.fxml;
    exports proxy;

    opens viewController to javafx.fxml;
    exports viewController;

    opens controller to javafx.fxml;
    exports controller;
}