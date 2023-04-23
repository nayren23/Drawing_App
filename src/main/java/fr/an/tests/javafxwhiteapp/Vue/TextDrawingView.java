package fr.an.tests.javafxwhiteapp.Vue;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import fr.an.tests.javafxwhiteapp.Elements.BaseDrawingElements;
import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;
import fr.an.tests.javafxwhiteapp.Elements.DrawingPt;
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
        applyButton.setOnAction(e -> onClickApply());
        refreshModelToView();
    }

    private void onClickApply() {
        System.out.println("apply view to model update");
        String text = textArea.getText();
        DrawingElement content = (DrawingElement) xstream.fromXML(text);
        model.setContent(content); // => fireModelChange ..
    }

    XStream xstream = createXStream();
    static XStream createXStream() {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("Pt", DrawingPt.class);
        xstream.alias("Text", BaseDrawingElements.TextDrawingElement.class);
        xstream.alias("Line", BaseDrawingElements.LineDrawingElement.class);
        xstream.alias("Circle", BaseDrawingElements.CircleDrawingElement.class);
        xstream.alias("Rectangle", BaseDrawingElements.RectangleDrawingElement.class);
        xstream.alias("Group", BaseDrawingElements.GroupDrawingElement.class);
        return xstream;
    }

    @Override
    public Node getComponent() {
        return component;
    }

    protected void refreshModelToView() {
        DrawingElement content = model.getContent();
        String text = xstream.toXML(content);
        textArea.setText(text);
    }

    @Override
    public void onModelChange() {
        System.out.println("(from subscribe): model to view change");
        refreshModelToView();
    }
}