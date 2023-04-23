package fr.an.tests.javafxwhiteapp.Handler;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public abstract class ToolStateHandler {

    public abstract void onMouseEntered();
    public abstract void onMouseMove(MouseEvent event);
    public abstract void onMouseClick(MouseEvent event);
    public abstract void onKeyPressed(KeyEvent event);

}
