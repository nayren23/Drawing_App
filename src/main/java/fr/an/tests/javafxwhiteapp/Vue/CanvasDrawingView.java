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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class CanvasDrawingView extends DrawingView implements DrawingModelListener {

    protected BorderPane component;
    protected Pane drawingPane;
    protected ToolStateHandler currToolStateHandler = new DefaultSelectToolStateHandler();
    protected ObservableList<Node> currToolShapes = FXCollections.observableArrayList();

    protected BaseDrawingElements.CircleDrawingElement currEditLineStartPt;
    protected BaseDrawingElements.CircleDrawingElement currEditLineEndPt;
    protected BaseDrawingElements.LineDrawingElement currEditLine;

    //List des Elements
    protected BaseDrawingElements.GroupDrawingElement content = (BaseDrawingElements.GroupDrawingElement) model.getContent();

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

            //Button +Line
            Button newLineButton = new Button("+Line");
            newLineButton.setOnAction(e -> onClickToolNewLine());
            toolBar.getItems().add(newLineButton);

            //Button +Lines
            Button newLinesButton = new Button("+Lines");
            newLinesButton.setOnAction(e -> onClickToolNewLines());
            toolBar.getItems().add(newLinesButton);
        }
        refreshModelToView();
    }

    private void GetCoordonee(MouseEvent event) {
        double x = event.getX(), y = event.getY();
        DrawingPt pt = new DrawingPt(x, y);
        currEditLineStartPt = new BaseDrawingElements.CircleDrawingElement(pt, 2);
        currEditLineEndPt = new BaseDrawingElements.CircleDrawingElement(pt, 2);
        currEditLine = new BaseDrawingElements.LineDrawingElement(pt, pt);
    }

    private void setNull(){
        currEditLine = null;
        currEditLineStartPt = null;
        currEditLineEndPt = null;
    }

    private void onMouseMoved(MouseEvent event){
        double x = event.getX(), y = event.getY();
        currEditLineEndPt.center = currEditLine.end = new DrawingPt(x, y);
        updateCurrEditTool();
    }


    private void onClickToolNewLines() {
        this.currToolStateHandler = new StateInit_LinesToolStateHandler();
    }

    protected class StateInit_LinesToolStateHandler extends DefaultSelectToolStateHandler {
        @Override
        public void onMouseEntered() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            GetCoordonee(event);
            content.elements.add(currEditLine);
            updateCurrEditTool();
            setToolHandler(new StatePt2_LinesToolStateHandler());
        }
    }

    /**
     Classe interne qui gère l'état de l'outil de dessin de lignes à deux points.
     Hérite de DefaultSelectToolStateHandler.
     */
    protected class StatePt2_LinesToolStateHandler extends DefaultSelectToolStateHandler {

        /**
         Modifie le curseur pour un curseur en forme de croix.
         */
        @Override
        public void onMouseEntered() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        /**
         Met à jour les coordonnées du deuxième point de la ligne en cours d'édition et met à jour l'outil de dessin.
         @param event L'événement de la souris.
         */
        @Override
        public void onMouseMove(MouseEvent event) {
            onMouseMoved(event);
        }

        /**
         Ajoute la ligne en cours d'édition au contenu du modèle, réinitialise l'outil de dessin
         et passe à l'outil de sélection.
         @param event L'événement de la souris.
         */
        @Override
        public void onMouseClick(MouseEvent event) {
            double x = event.getX(), y = event.getY();
            DrawingPt pt = new DrawingPt(x, y);
            currEditLineEndPt = new BaseDrawingElements.CircleDrawingElement(pt, 2);
            currEditLine.end = pt;

            BaseDrawingElements.LineDrawingElement addToModel = currEditLine;
            content.elements.add(addToModel);

            model.setContent(content);

            currEditLineStartPt = currEditLineEndPt;
            currEditLine = new BaseDrawingElements.LineDrawingElement(pt, pt);

            updateCurrEditTool();

        }

        @Override
        public void OnRightMousePressed(MouseEvent event) {
            addLineToContent();
            System.out.println("c'est la fin du grand trait");
        }
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
        drawingPane.setOnMouseClicked(e ->currToolStateHandler.onMouseClick(e));
        drawingPane.setOnMousePressed (e -> {
            if (e.isSecondaryButtonDown())
                currToolStateHandler.OnRightMousePressed(e);
        });
    }

    /**
     Cette méthode permet de mettre à jour l'outil de dessin courant en supprimant toutes les formes
     de l'outil de dessin courant et en les remplaçant par les nouvelles formes créées par l'utilisateur.
     Elle utilise un visiteur JavaFX pour parcourir les éléments de dessin actuellement sélectionnés,
     tels que la ligne de début, la ligne de fin, la ligne de dessin actuelle et toutes les lignes
     actuelles, afin de mettre à jour leur affichage dans le dessin.
     @return void
     */
    protected void updateCurrEditTool() {
        drawingPane.getChildren().removeAll(currToolShapes);
        currToolShapes.clear();
        JavafxDrawingElementVisitor visitor = new JavafxDrawingElementVisitor(currToolShapes);//
        if (currEditLineStartPt != null) { currEditLineStartPt.accept(visitor); }
        if (currEditLineEndPt != null) { currEditLineEndPt.accept(visitor); }
        if (currEditLine != null) {
            currEditLine.accept(visitor);
        }
        content.elements.forEach(element-> element.accept(visitor) );
        drawingPane.getChildren().addAll(currToolShapes);
    }

    private void onClickToolReset() {

        // Vider les éléments du contenu
        content.elements.clear();

        // Réinitialiser le modèle et les éléments de dessin
        model.setContent(content);
        setNull();

        // Effacer le panneau de dessin
        drawingPane.getChildren().clear();

        //Vide le tableau de lignes
        content.elements.clear();

    }

    private void onClickToolNewLine() {
        this.currToolStateHandler = new StateInit_LineToolStateHandler();
    }
    protected void setToolHandler(ToolStateHandler p) {
        currToolStateHandler = p;
        updateCurrEditTool();
    }


    public void addLineToContent(){
        BaseDrawingElements.LineDrawingElement addToModel = currEditLine;
        content.elements.add(addToModel);
        model.setContent(content);
        setNull();
        updateCurrEditTool();
        setToolHandler(new DefaultSelectToolStateHandler());
        drawingPane.setCursor(Cursor.DEFAULT);
    }

    protected class StateInit_LineToolStateHandler extends DefaultSelectToolStateHandler {
        @Override
        public void onMouseEntered() {
            drawingPane.setCursor(Cursor.CROSSHAIR);
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            GetCoordonee(event);
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
            onMouseMoved(event);
        }
        @Override
        public void onMouseClick(MouseEvent event) {
            addLineToContent();
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