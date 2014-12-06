package com.segmentation_tool;

import java.util.logging.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClusterConstructor extends Application {

	public static void main(String[] args) {
		Application.launch(ClusterConstructor.class, (java.lang.String[]) null);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AnchorPane page = (AnchorPane) FXMLLoader.load(ClusterConstructor.class
					.getResource("ClusterWindow.fxml"));
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ClusterTool");
		//	primaryStage.setResizable(false);
			primaryStage.show();			
		} catch (Exception ex) {
			Logger.getLogger(ClusterConstructor.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}
}
