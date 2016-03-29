package com.namal.arch.view;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class UIController {

	public UIController() {
		// TODO Auto-generated constructor stub
	}
	
	//Each class will have a ref to its own pane
	protected Pane refPane;
	protected UIMainClass mainApp;
	
	/**
	 * @param FXMLFile Location of the FXMLFile
	 * @return The loader of the loaded file
	 */
	public FXMLLoader getLoaderFromFile(String FXMLFile)
    {
	   //Loading the FXMLFile into an URL
	   URL url = getClass().getClassLoader().getResource(FXMLFile);
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(url);
       loader.setBuilderFactory(new JavaFXBuilderFactory());
       return loader;
    }

	/**
	 * @param FXMLFile Location of the FXMLFile
	 * @param module The Pane where the module will be attached
	 * @return The loader of the loaded file
	 */
	public FXMLLoader loadingModule(String FXMLFile, Pane module){
		try {
			FXMLLoader loader = getLoaderFromFile(FXMLFile);
			
	        //Loading the module and setting the anchors
	        AnchorPane newModule = (AnchorPane) loader.load();
			
	        AnchorPane.setBottomAnchor(newModule, 0.0);
	        AnchorPane.setTopAnchor(newModule, 0.0);
	        AnchorPane.setLeftAnchor(newModule, 0.0);
	        AnchorPane.setRightAnchor(newModule, 0.0);
	        
	        module.getChildren().clear();
	        module.getChildren().add(newModule);
	        
	        return loader;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
