package com.namal.arch;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main class.
 * @author namalgac
 *
 */
public class Main extends Application{
	
	private Stage primaryStage;
	private AnchorPane generalLayout;
	
	@Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Archi");
        
        loadGeneraLayout();
    }
	
	public void loadGeneraLayout(){
		try {
            // Load general overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("./view/GeneralLayout.fxml"));
            generalLayout = (AnchorPane) loader.load();

            // Show the scene containing the general layout.
            Scene scene = new Scene(generalLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
         /*// Give the controller access to the main app.
            GeneralLayoutController controller = loader.getController();
            controller.setMainApp(this);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Uncomment/Comment for enable/disable UI
		launch(args);
	}

}
