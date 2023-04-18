package fr.an.tests.javafxwhiteapp.Vue;

import fr.an.tests.javafxwhiteapp.Elements.BaseDrawingElements;
import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;
import fr.an.tests.javafxwhiteapp.Modele.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.Modele.DrawingModelListener;
import fr.an.tests.javafxwhiteapp.Visitors.DrawingElementVisitor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class CanvasDrawingView extends DrawingView implements DrawingModelListener {

    protected BorderPane component;
    // to add javafx.scene.shape.* objects converted from model
    protected Pane drawingPane;

    public CanvasDrawingView(DrawingDocModel model) {
        super(model);
        model.addListener(this); // publish&subscribe design pattern
        this.component = new BorderPane();
        drawingPane = new Pane();
        component.setCenter(drawingPane);
        refreshModelToView();
    }


    @Override
    public Node getComponent() {
        return component;
    }

    protected void refreshModelToView() {
        DrawingElement content = model.getContent();
        drawingPane.getChildren().clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor();
        content.accept(visitor);
    }

    @Override
    public void onModelChange() {
        System.out.println("(from subscribe): model to view change" + "changement du canvas");
        refreshModelToView();
    }

    protected class JavafxDrawingElementVisitor extends DrawingElementVisitor {
        protected void add(Node node) {
            drawingPane.getChildren().add(node);
        }
        @Override
        public void caseText(BaseDrawingElements.TextDrawingElement p) {
            add(new javafx.scene.text.Text(p.pos.x, p.pos.y, p.text));
        }
        @Override
        public void caseLine(BaseDrawingElements.LineDrawingElement p) {
            add(new javafx.scene.shape.Line(p.start.x, p.start.y, p.end.x, p.end.y));
        }

        @Override
        public void caseRect(BaseDrawingElements.RectangleDrawingElement p) {
            add(new javafx.scene.shape.Rectangle(p.upLeft.x, p.upLeft.y,
                    p.downRight.x-p.upLeft.x, p.downRight.y-p.upLeft.y));
        }
        @Override
        public void caseCircle(BaseDrawingElements.CircleDrawingElement p) {
            add(new javafx.scene.shape.Circle(p.center.x, p.center.y, p.radius));
        }
        @Override
        public void caseGroup(BaseDrawingElements.GroupDrawingElement p) {
            for(DrawingElement child: p.elements) {
                // *** recurse ***
                child.accept(this);
            }
        }
        @Override
        public void caseOther(DrawingElement p) {
// "not implemented/recognized drawingElement "+ p.getClass().getName();
        }

    }
}