package fr.an.tests.javafxwhiteapp.Vue;

import fr.an.tests.javafxwhiteapp.Modele.DrawingDocModel;

public abstract class DrawingView {

    protected DrawingDocModel model;
    public DrawingView(DrawingDocModel model) {
        this.model = model;
    }

    public abstract javafx.scene.Node getComponent();
    // more later: Subscriber design-pattern
}
