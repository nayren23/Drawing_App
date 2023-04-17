package fr.an.tests.javafxwhiteapp.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

    	{ // SplitPane( view1 | view2 )
	    	VBox view1 = new VBox();
	    	view1.getChildren().add(new Text("Hello 1"));
	
	    	VBox view2 = new VBox();
			view2.getChildren().add(new Text("Hello 2"));
	 
			SplitPane splitViewPane = new SplitPane(view1, view2);
			mainBorderPanel.setCenter(splitViewPane);
    	}
    	
		Scene scene = new Scene(mainBorderPanel, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

}
