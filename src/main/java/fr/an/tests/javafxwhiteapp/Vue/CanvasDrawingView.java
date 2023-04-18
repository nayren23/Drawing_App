package fr.an.tests.javafxwhiteapp.Vue;

import fr.an.tests.javafxwhiteapp.Elements.BaseDrawingElements;
import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;
import fr.an.tests.javafxwhiteapp.Elements.DrawingPt;
import fr.an.tests.javafxwhiteapp.Handler.DefaultSelectToolStateHandler;
import fr.an.tests.javafxwhiteapp.Handler.ToolStateHandler;
import fr.an.tests.javafxwhiteapp.Modele.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.Modele.DrawingModelListener;
import fr.an.tests.javafxwhiteapp.Visitors.DrawingElementVisitor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class CanvasDrawingView extends DrawingView implements DrawingModelListener {

    protected BorderPane component;
    // to add javafx.scene.shape.* objects converted from model
    protected Pane drawingPane;

    protected ToolStateHandler currToolStateHandler = new DefaultSelectToolStateHandler();
    protected ObservableList<Node> currToolShapes = FXCollections.observableArrayList();
    protected BaseDrawingElements.CircleDrawingElement currEditLineStartPt;
    protected BaseDrawingElements.CircleDrawingElement currEditLineEndPt;
    protected BaseDrawingElements.LineDrawingElement currEditLine;

    
    public CanvasDrawingView(DrawingDocModel model) {
        super(model);
        model.addListener(this); // publish&subscribe design pattern
        this.component = new BorderPane();
        drawingPane = new Pane();
        component.setCenter(drawingPane);
        { // button Toolbar
            ToolBar toolBar = new ToolBar();
            component.setTop(toolBar);
            Button resetToolButton = new Button("Reset");
            resetToolButton.setOnAction(e -> onClickToolReset());
            toolBar.getItems().add(resetToolButton);
            Button newLineButton = new Button("+Line");
            newLineButton.setOnAction(e -> onClickToolNewLine());
            toolBar.getItems().add(newLineButton);
        }
        refreshModelToView();
    }

    @Override
    public Node getComponent() {
        return component;
    }

    protected void refreshModelToView() {
        DrawingElement content = model.getContent();
        drawingPane.getChildren().clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor(drawingPane.getChildren());
        content.accept(visitor);

        drawingPane.setOnMouseEntered(e -> currToolStateHandler.onMouseEntered());
        drawingPane.setOnMouseMoved(e -> currToolStateHandler.onMouseMove(e));
        drawingPane.setOnMouseClicked(e -> currToolStateHandler.onMouseClick(e));
    }

    protected void updateCurrEditTool() {
        drawingPane.getChildren().removeAll(currToolShapes);
        currToolShapes.clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor(currToolShapes);//
        if (currEditLineStartPt != null) { currEditLineStartPt.accept(visitor); }
        if (currEditLineEndPt != null) { currEditLineEndPt.accept(visitor); }
        if (currEditLine != null) { currEditLine.accept(visitor); }
        drawingPane.getChildren().addAll(currToolShapes);
    }

    private void onClickToolReset() {
        this.currToolStateHandler = new DefaultSelectToolStateHandler();
        currEditLineStartPt = null;
        currEditLineEndPt = null;
        currEditLine = null;
        refreshModelToView();
    }

    private void onClickToolNewLine() {
        this.currToolStateHandler = new StateInit_LineToolStateHandler();
    }
    protected void setToolHandler(ToolStateHandler p) {
        currToolStateHandler = p;
        updateCurrEditTool();
    }

    protected class StateInit_LineToolStateHandler extends DefaultSelectToolStateHandler {
        @Override
        public void onMouseEntered() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            double x = event.getX(), y = event.getY();
            DrawingPt pt = new DrawingPt(x, y);
            currEditLineStartPt = new BaseDrawingElements.CircleDrawingElement(pt, 2);
            currEditLineEndPt = new BaseDrawingElements.CircleDrawingElement(pt, 2);
            currEditLine = new BaseDrawingElements.LineDrawingElement(pt, pt);
            updateCurrEditTool();
            setToolHandler(new StatePt1_LineToolStateHandler());
        }
    }

    protected class StatePt1_LineToolStateHandler extends DefaultSelectToolStateHandler {

        @Override
        public void onMouseEntered() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseMove(MouseEvent event) {
            double x = event.getX(), y = event.getY();
            currEditLineEndPt.center = currEditLine.end = new DrawingPt(x, y);
            updateCurrEditTool();
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            BaseDrawingElements.LineDrawingElement addToModel = currEditLine;
            BaseDrawingElements.GroupDrawingElement content = (BaseDrawingElements.GroupDrawingElement) model.getContent();
            content.elements.add(addToModel);
            model.setContent(content);
            currEditLine = null;
            currEditLineStartPt = null;
            currEditLineEndPt = null;
            updateCurrEditTool();
            setToolHandler(new DefaultSelectToolStateHandler());
        }
    }
    @Override
    public void onModelChange() {
        System.out.println("(from subscribe): model to view change" + "changement du canvas");
        refreshModelToView();
    }

    protected class JavafxDrawingElementVisitor extends DrawingElementVisitor {

        protected List<Node> results;

        public JavafxDrawingElementVisitor(List<Node> results) {
            this.results = results;
        }

        protected void add(Node node) {
            results.add(node);
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