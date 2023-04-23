package fr.an.tests.javafxwhiteapp.Vue;

import fr.an.tests.javafxwhiteapp.Elements.BaseDrawingElements;
import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;
import fr.an.tests.javafxwhiteapp.Elements.DrawingPt;
import fr.an.tests.javafxwhiteapp.Modele.DrawingDocModel;
import fr.an.tests.javafxwhiteapp.Vue.CanvasDrawingView;
import fr.an.tests.javafxwhiteapp.Vue.TextDrawingView;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleApp extends Application {

	@Override
	public void start(Stage stage) {
		BorderPane mainBorderPanel = new BorderPane();

		VBox menuAndToolbar = new VBox();
		{ // MenuBar with "File" menu
			MenuBar mb = new MenuBar();
			menuAndToolbar.getChildren().add(mb);

			Menu fileMenu = new Menu("File");
			mb.getMenus().add(fileMenu);
			MenuItem openMenuItem = new MenuItem("Open");
			fileMenu.getItems().add(openMenuItem);
			MenuItem saveMenuItem = new MenuItem("Save");
			fileMenu.getItems().add(saveMenuItem);
		}

		{ // button Toolbar
			ToolBar toolBar = new ToolBar();
			menuAndToolbar.getChildren().add(toolBar);

			Button button1 = new Button("button1");
			toolBar.getItems().add(button1);
		}
		mainBorderPanel.setTop(menuAndToolbar);

		DrawingDocModel model = new DrawingDocModel();

		DrawingElement content = createSimpleDrawing();
		model.setContent(content);

		{ // SplitPane( view1 | view2 )
			TextDrawingView view1 = new TextDrawingView(model);
			Node view1Comp = view1.getComponent();

			CanvasDrawingView view2 = new CanvasDrawingView(model);
			Node view2Comp = view2.getComponent();
			SplitPane splitViewPane = new SplitPane(view1Comp, view2Comp);
			mainBorderPanel.setCenter(splitViewPane);
		}

		Scene scene = new Scene(mainBorderPanel, 640, 480);
		stage.setScene(scene);
		stage.show();
	}

	private static DrawingElement createSimpleDrawing() {
		BaseDrawingElements.TextDrawingElement text = new BaseDrawingElements.TextDrawingElement("Hello", new DrawingPt());
		BaseDrawingElements.LineDrawingElement line = new BaseDrawingElements.LineDrawingElement(new DrawingPt(100, 130),
				new DrawingPt(200, 230));
		BaseDrawingElements.RectangleDrawingElement rectangle = new BaseDrawingElements.RectangleDrawingElement(
				new DrawingPt(100, 300), new DrawingPt(200, 350));
		BaseDrawingElements.CircleDrawingElement circle = new BaseDrawingElements.CircleDrawingElement(new DrawingPt(150, 400), 45);
		BaseDrawingElements.GroupDrawingElement res = new BaseDrawingElements.GroupDrawingElement();
		res.add(text);
		res.add(line);
		res.add(rectangle);
		res.add(circle);

		return res;
	}
}