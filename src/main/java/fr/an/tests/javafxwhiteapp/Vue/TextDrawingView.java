package fr.an.tests.javafxwhiteapp.Vue;

import fr.an.tests.javafxwhiteapp.Modele.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.Modele.DrawingModelListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class TextDrawingView extends DrawingView  implements DrawingModelListener {

    protected BorderPane component;
    protected TextArea textArea;
    protected Button applyButton;

    public TextDrawingView(DrawingDocModel model) {
        super(model);
        model.addListener(this); // publish&subscribe design pattern
        this.component = new BorderPane();
        this.textArea = new TextArea();
        component.setCenter(textArea);
        this.applyButton = new Button("Apply");
        component.setBottom(applyButton);
        refreshModelToView();
    }

    @Override
    public Node getComponent() {
        return component;
    }
    public void refreshModelToView() {
        String text = model.getContent();
        textArea.setText(text);
    }

    @Override
    public void onModelChange() {
        System.out.println("(from subscribe): model to view change");
        refreshModelToView();
    }
}
