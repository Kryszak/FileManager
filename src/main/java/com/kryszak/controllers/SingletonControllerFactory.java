package com.kryszak.controllers;

public class SingletonControllerFactory {

    private static MainController mainController;

    public static MainController getMainController() {
        if (mainController == null) {
            mainController = new MainController();
        }
        return mainController;
    }

}
