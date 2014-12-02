package com.segmentation_tool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;





import com.domain.MatlabAPI;
import com.domain.MatlabDecorator;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import com.service.ImageManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ClusterController implements Initializable {

	@FXML
	Parent root;
	@FXML
	private ResourceBundle resources;
	@FXML
	private MenuItem menuItemOpen;
	@FXML
	private MenuItem menuItemSave;
	@FXML
	private ImageView mainView;
	@FXML
	private MenuItem menuItemOpenUSImage;
	@FXML
	private MenuItem menuItemRun;
	@FXML
	private MenuItem menuItemExit;
	@FXML
	private TextField sigma;
	@FXML
	private TextField winSize;
    @FXML
    private RadioMenuItem radioStatistical;
    @FXML
    private RadioMenuItem radio2DFT;
    @FXML
    private RadioMenuItem radioDFT;
    @FXML
    private RadioMenuItem radioFCM;
    @FXML
    private RadioMenuItem radioPNN;
    @FXML
    private RadioMenuItem radioKNN;
    
    
	static MatlabAPI mAPI=new MatlabDecorator();
	private String imagePath = "C:\\";
	BufferedImage mainViewImage;
	Object[] USImage = null;
	int log_number=0;
	
	HashMap<Integer, Object[]> objectImages = new HashMap<Integer, Object[]>();
	HashMap<Integer, BufferedImage> bufferedImages = new HashMap<Integer, BufferedImage>();
	int number_history=0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		assert menuItemSave != null : "fx:id=\"menuItemSave\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert menuItemOpen != null : "fx:id=\"menuItemOpen\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert menuItemExit != null : "fx:id=\"menuItemExit\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert menuItemOpenUSImage != null : "fx:id=\"menuItemOpenUSImage\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert mainView != null : "fx:id=\"mainView\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert menuItemRun != null : "fx:id=\"menuItemRun\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert sigma != null : "fx:id=\"sigma\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert winSize != null : "fx:id=\"winSize\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radioStatistical != null : "fx:id=\"radioStatistical\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radioKNN != null : "fx:id=\"radioKNN\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radio2DFT != null : "fx:id=\"radio2DFT\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radioDFT != null : "fx:id=\"radioDFT\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radioFCM != null : "fx:id=\"radioFCM\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert radioPNN != null : "fx:id=\"radioPNN\" was not injected: check your FXML file 'MainWindow.fxml'.";
      
       double initX;
       double initY;
        mainView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //System.out.println("Clicked, x:" + me.getSceneX() + " y:" + me.getSceneY());
                //the event will be passed only to the circle which is on front
              /*  initX = me.getSceneX();
                initY = me.getSceneY();*/
            	System.out.println("clicked");
                me.consume();
            }
        });
        
        mainView.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                System.out.println("Clicked, x:" + me.getSceneX() + " y:" + me.getSceneY());
                //the event will be passed only to the circle which is on front
                displayMainView();
              double  initX = me.getX();
              double  initY= me.getY();
            	System.out.println("moved "+initX+" "+initY);
            	drawWindow(initX,initY);
                me.consume();
            }
        });
	}

	private void drawWindow(double i, double j) {	
		double w=Double.parseDouble(winSize.getText());
		 Line line1 = new Line(i, j, i, j+w);
		 Line line2 = new Line(i, j, i+w, j);
		 Line line3 = new Line(i, j+w, i+w, j+w);
		 Line line4 = new Line(i+w, j, i+w, j+w);
		 line1.setFill(null);
		 line2.setFill(null);
		 line3.setFill(null);
		 line4.setFill(null);
         line1.setStroke(Color.RED);
         line2.setStroke(Color.RED);
         line3.setStroke(Color.RED);
         line4.setStroke(Color.RED);
         line1.setStrokeWidth(2);
         line2.setStrokeWidth(2);
         line3.setStrokeWidth(2);
         line4.setStrokeWidth(2);
         ((AnchorPane) root).getChildren().add(line1);
         ((AnchorPane) root).getChildren().add(line2);
         ((AnchorPane) root).getChildren().add(line3);
         ((AnchorPane) root).getChildren().add(line4);
	}
	@FXML
	void openUSImage(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(imagePath));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			objectImages.clear();
			bufferedImages.clear();
			mainViewImage = ImageIO.read(file);
			USImage = mAPI.readImage(file.getAbsolutePath());
			displayMainView();
			objectImages.put(new Integer(objectImages.size()+1), USImage);
			bufferedImages.put(new Integer(bufferedImages.size()+1), mainViewImage);
			number_history=bufferedImages.size()+1;
			logMessage="Open image: "+file.getAbsolutePath();
		}
	}

	
	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	void displayMainView() {
		mainView.setImage(null);
		if (mainViewImage==null)  return;
		Canvas canvas = new Canvas(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.drawImage(SwingFXUtils.toFXImage(mainViewImage, null),
				0, 0);
		WritableImage wim = new WritableImage(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		canvas.snapshot(null, wim);
		mainView.setImage(wim);
		mainView.setPreserveRatio(true);
		mainView.setSmooth(true);
		mainView.setCache(true);
	}

	String logMessage=null;
	@FXML
	void runSegmentation(ActionEvent event) {		
		// checks if filtration needed
		try {
			if (Integer.parseInt(sigma.getText()) != -1) {
				USImage = mAPI.getHomomorphicFilter(USImage,
						Integer.parseInt(sigma.getText()));
				MWNumericArray filt = (MWNumericArray) (USImage[0]);
				mainViewImage = ImageManager.getImageFromMWNumericArray(filt);
				displayMainView();
				objectImages.put(new Integer(objectImages.size() + 1), USImage);
				bufferedImages.put(new Integer(bufferedImages.size() + 1),
						mainViewImage);
				number_history=bufferedImages.size();
				logMessage=logMessage+"Filtration: Yes";
			}else
				logMessage=logMessage+"Filtration: No";
		} catch (java.lang.NumberFormatException e) {
			System.out.println("Exception: can't get number " + e.getMessage());
		}
		
		Object[] res=null;
		// features
		if (radioStatistical.isSelected()) {			
			res=mAPI.getStatisticalFeatures(USImage,Integer.parseInt(winSize.getText()));
			//System.out.println(res[0]);
		}
		
		if (radioDFT.isSelected()) {			
			res=mAPI.getDFTFeatures(USImage,Integer.parseInt(winSize.getText()));
			//System.out.println(res[0]);
		}
		
		// clustering
		if (radioFCM.isSelected()) {			
			USImage=mAPI.getFCMClustrering(res, Integer.parseInt(winSize.getText()),mainViewImage.getWidth(),mainViewImage.getHeight());
			MWNumericArray filt = (MWNumericArray) (USImage[0]);
			mainViewImage = ImageManager.getColorImageFromMWNumericArray(filt);
			displayMainView();
			objectImages.put(new Integer(objectImages.size() + 1), USImage);
			bufferedImages.put(new Integer(bufferedImages.size() + 1),
					mainViewImage);
			number_history=bufferedImages.size();
		}	
		
	}
	
	private void printLogMessage(String msg) {
		 ObservableList<String> items =FXCollections.observableArrayList (
	        	    "Single", "Double", "Suite", "Family App");
	        
	}
	
}
