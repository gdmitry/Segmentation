package com.presentation;


import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainScene extends Application {

	public static void main(String[] args) {
		Application.launch(MainScene.class, (java.lang.String[]) null);
		/*ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"ApplicationContext.xml");
		MatlabAPI a = (MatlabAPI) applicationContext.getBean("MatlabAPI");
		Object[] res = null;
		// res=a.getStatisticalFeatures("C:\\11.png", 20);
		res=a.readImage("C:\\11.png");
		
		res = a.getHomomorphicFilter(res, 10);
		MWNumericArray filt = (MWNumericArray) (res[0]);
		getImageFromMWNumericArray(filt);
		//*
	/*	BufferedImage im;
		try {
			im = ImageIO.read(new File("C:\\11.png"));
			res = a.getHomomorphicFilter(toHalfToningImage(im), 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	// setenv('JAVA_HOME','C:\Program Files\Java\jdk1.6.0_32');
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane page = (AnchorPane) FXMLLoader.load(MainScene.class
					.getResource("MainWindow.fxml"));
			Scene scene = new Scene(page);
			primaryStage.setScene(scene);
			primaryStage.setTitle("USImageProcessing");
			primaryStage.setResizable(false);
			primaryStage.show();			
		} catch (Exception ex) {
			Logger.getLogger(MainScene.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
