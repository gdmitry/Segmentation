package com.presentation;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MainWindowController implements Initializable {

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
	private TableView<?> history_table;
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
    @FXML
    private PieChart chart;
    @FXML
    private ListView<String> log_list;
    
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
		assert history_table != null : "fx:id=\"history_table\" was not injected: check your FXML file 'MainWindow.fxml'.";
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
        assert chart != null : "fx:id=\"chart\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert log_list != null : "fx:id=\"log_list\" was not injected: check your FXML file 'MainWindow.fxml'.";
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			final KeyCombination undo = new KeyCodeCombination(KeyCode.Z,
					KeyCombination.CONTROL_DOWN);
			final KeyCombination redo = new KeyCodeCombination(KeyCode.X,
					KeyCombination.CONTROL_DOWN);
			@Override
			public void handle(KeyEvent event) {
				if (undo.match(event)) {
					System.out.println("Undo");
					if (number_history>1) {
						number_history--;
						mainViewImage=bufferedImages.get(new Integer(number_history));
						USImage=objectImages.get(new Integer(number_history));
						displayMainView();
					}
				}
				
				if (redo.match(event)) {
					System.out.println("Redo");
					if (number_history<bufferedImages.size()) {
						number_history++;
						mainViewImage=bufferedImages.get(new Integer(number_history));
						USImage=objectImages.get(new Integer(number_history));
						displayMainView();
					}
				}
			}

		});  
	}

	

	@FXML
	void openUSImage(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(imagePath));
		displayMessage("Openning an image... ");

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

	private void populateData(int[] mas) {
		Arrays.sort(mas);
		double s=mas[0]+mas[1]+mas[2];
		chart.setData(null);
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Sick", Math.round(mas[0]/s*100)),
                new PieChart.Data("Suspected",Math.round(mas[1]/s*100)),
                new PieChart.Data("Well", Math.round(mas[2]/s*100)));        
        chart.setData(pieChartData);
        chart.setTitle("Segmented regions");        
        

        
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 20 arial;");
        String[] pieColors={"red", "orange","#04B404"};
        int i=0;
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {                    	
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        caption.setText(String.valueOf(data.getPieValue()) + "%");
                        caption.setVisible(true);
                     }
                });
            data.getNode().setStyle(
            	      "-fx-pie-color: " + pieColors[i % pieColors.length] + ";"
            	    );          
            	    i++;
        }
        Set<Node> items = chart.lookupAll("Label.chart-legend-item");
   i = 0;
        // these colors came from caspian.css .default-color0..4.chart-pie
        Color[] colors = { Color.RED, Color.ORANGE, Color.web("#04B404")};
        for (Node item : items) {
          Label label = (Label) item;
          //final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
          Circle rectangle = new Circle(10, colors[i]);
          //final Glow niceEffect = new Glow();
      //    niceEffect.setInput(new Reflection());
        //  rectangle.setEffect(niceEffect);
          label.setGraphic(rectangle);
          i++;
        }
        
        ((AnchorPane) root).getChildren().add( caption);
	}
	
	public void displayMessage(String message) {
		System.out.println(message);
	}
	@FXML
    void mainViewClicked(ActionEvent event) {

    }

    @FXML
    void mainViewMoved(ActionEvent event) {

    }

	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	void displayMainView() {
		mainView.setImage(null);
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
			populateData(new int[]{((MWNumericArray)USImage[1]).getInt(1),((MWNumericArray)USImage[2]).getInt(1),((MWNumericArray)USImage[3]).getInt(1)});
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
	        log_list.setItems(items);
	        log_list.
	}
}
