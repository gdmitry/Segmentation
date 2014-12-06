package com.segmentation_tool;

import com.domain.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import com.domain.MatlabAPI;
import com.domain.MatlabDecorator;
import com.domain.WindowPatch;
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
import javafx.scene.control.Button;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class ClusterController implements Initializable {

	@FXML
	Parent root;
	@FXML
	private AnchorPane viewId;
	@FXML
	private Button sickButton;
	@FXML
	private Button phoneButton;
	@FXML
	private Button suspectButton;
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

	static MatlabAPI mAPI = new MatlabDecorator();
	ClusterToolData cTool;
	private String imagePath = "C:\\";
	BufferedImage mainViewImage;
	Object[] USImage = null;
	int log_number = 0;
	String logMessage = null;
	String typePatch = "sick";

	HashSet<WindowPatch> patches = new HashSet<>();
	HashMap<Integer, Object[]> objectImages = new HashMap<Integer, Object[]>();
	HashMap<Integer, BufferedImage> bufferedImages = new HashMap<Integer, BufferedImage>();
	int number_history = 0;

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
		assert viewId != null : "fx:id=\"viewId\" was not injected: check your FXML file 'ClusterWindow.fxml'.";
		assert phoneButton != null : "fx:id=\"phoneButton\" was not injected: check your FXML file 'ClusterWindow.fxml'.";
		assert suspectButton != null : "fx:id=\"suspectButton\" was not injected: check your FXML file 'ClusterWindow.fxml'.";
		assert sickButton != null : "fx:id=\"sickButton\" was not injected: check your FXML file 'ClusterWindow.fxml'.";

		mainView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				double initX = me.getX();
				double initY = me.getY();
				System.out.println("moved " + initX + " " + initY);
				System.out.println("clicked");
				double i = me.getX();
				double j = me.getY();
				double w = Double.parseDouble(winSize.getText());
				if ((i < mainViewImage.getWidth() - w)
						&& (j < mainViewImage.getHeight() - w)) {
					if (typePatch.equals("sick")) {
						fillWindow(i, j, Color.RED);
					}
					if (typePatch.equals("phone")) {
						fillWindow(i, j, Color.GREEN);
					}
					if (typePatch.equals("suspect")) {
						fillWindow(i, j, Color.ORANGE);
					}

					// drawWindow(i, j);
				}
				me.consume();
			}
		});

		mainView.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {

				double i = me.getX();
				double j = me.getY();
				double w = Double.parseDouble(winSize.getText());
				if ((i < mainViewImage.getWidth() - w)
						&& (j < mainViewImage.getHeight() - w))
					drawWindow(i, j);
				me.consume();
			}
		});

		sickButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				sickClicked();
			}
		});
		suspectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				suspectClicked();
			}
		});
		phoneButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				phoneClicked();
			}
		});
	}

	@FXML
	void openUSImage(ActionEvent event) throws IOException {
		removeFolders();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(imagePath));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			objectImages.clear();
			bufferedImages.clear();
			mainViewImage = ImageIO.read(file);
			USImage = mAPI.readImage(file.getAbsolutePath());
			patches.clear();
			displayMainView();
			// cTool.setCanvasHeight(mainView.getFitHeight());
			// cTool.setCanvasWidth(mainView.getFitWidth());
			objectImages.put(new Integer(objectImages.size() + 1), USImage);
			bufferedImages.put(new Integer(bufferedImages.size() + 1),
					mainViewImage);
			number_history = bufferedImages.size() + 1;
			logMessage = "Open image: " + file.getAbsolutePath();
		}
	}

	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	void displayMainView() {
		mainView.setImage(null);
		if (mainViewImage == null)
			return;
		Canvas canvas = new Canvas(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.drawImage(SwingFXUtils.toFXImage(mainViewImage, null),
				0, 0);
		WritableImage wim = new WritableImage(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		canvas.snapshot(null, wim);
		mainView.setImage(wim);
		mainView.setFitHeight(mainViewImage.getHeight());
		mainView.setFitWidth(mainViewImage.getWidth());
		mainView.setPreserveRatio(true);
		mainView.setSmooth(true);
		mainView.setCache(true);
	}

	Random random = new Random();

	public void drawWindow(double i, double j) {
		double w = Double.parseDouble(winSize.getText());
		i = Math.floor(i / w) * w;
		j = Math.floor(j / w) * w;

		double lineWidth = 1;

		mainView.setImage(null);
		if (mainViewImage == null)
			return;
		Canvas canvas = new Canvas(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(SwingFXUtils.toFXImage(mainViewImage, null), 0, 0);
		drawPatches(gc);
		gc.setStroke(Color.RED);
		gc.setLineWidth(lineWidth);
		gc.strokeLine(i, j, i, j + w);
		gc.strokeLine(i, j, i + w, j);
		gc.strokeLine(i, j + w, i + w, j + w);
		gc.strokeLine(i + w, j, i + w, j + w);
		WritableImage wim = new WritableImage(mainViewImage.getWidth(),
				mainViewImage.getHeight());
		canvas.snapshot(null, wim);
		mainView.setImage(wim);
		mainView.setFitHeight(mainViewImage.getHeight());
		mainView.setFitWidth(mainViewImage.getWidth());
		mainView.setPreserveRatio(true);
		mainView.setSmooth(true);
		mainView.setCache(true);
	}

	private void drawPatches(GraphicsContext gc) {
		double w = Double.parseDouble(winSize.getText());

		Iterator<WindowPatch> iterator = patches.iterator();
		while (iterator.hasNext()) {
			WindowPatch p = iterator.next();
			gc.setFill(p.color);
			double i = Math.floor(p.i / w) * w;
			double j = Math.floor(p.j / w) * w;
			gc.fillRect(i, j, w, w);
		}
	}

	public void fillWindow(double i, double j, Color c) {
		patches.add(new WindowPatch(i, j, c));
	}

	private void createImages() {
		double w = Double.parseDouble(winSize.getText());
		Iterator<WindowPatch> iterator = patches.iterator();
		while (iterator.hasNext()) {
			WindowPatch p = iterator.next();
			double i = Math.floor(p.i / w) * w;
			double j = Math.floor(p.j / w) * w;
			BufferedImage image = mainViewImage.getSubimage((int) i, (int) j,
					(int) w, (int) w);
			int randomNumber = random.nextInt(10000 - 1) + 1;
			if (p.color == Color.RED)
				try {
					ImageIO.write(image, "png", new File("C:\\sick\\" + "sick_"
							+ randomNumber + ".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (p.color == Color.GREEN)
				try {
					ImageIO.write(image, "png", new File("C:\\phone\\"
							+ "phone_" + randomNumber + ".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (p.color == Color.ORANGE)
				try {
					ImageIO.write(image, "png", new File("C:\\suspect\\"
							+ "suspect_" + randomNumber + ".png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}

	@FXML
	void runSegmentation(ActionEvent event) {
		createImages();
		HashSet<String> hs = fillImageDB("C:\\sick");
		Iterator<String> iterator = hs.iterator();
		while (iterator.hasNext()) {
			MWNumericArray filt = (MWNumericArray) (mAPI.getDFT(iterator.next())[0]);
			System.out.println(filt);
		}
	}

	void sickClicked() {
		typePatch = "sick";
	}

	void suspectClicked() {
		typePatch = "suspect";
	}

	void phoneClicked() {
		typePatch = "phone";
	}

	private HashSet<String> fillImageDB(String pathname) {
		HashSet<String> paths = new HashSet<>();
		try {

			File path = new File(pathname);
			File[] files;
			if (path.isFile()) {
				files = new File[] { path };
			} else {
				files = path.listFiles();
				// Arrays.sort(files, new FilesComparator());
			}

			for (final File f : files) {
				if (!f.isDirectory()) {
					System.out.println(f.getAbsolutePath());
					if (isImage(f.getAbsolutePath())) {
						// System.out.println(f.getName());
						paths.add(f.getAbsolutePath());
					}
				}
			}
		} catch (Exception ex) {

		}
		return paths;
	}

	private boolean isImage(String str) {

		int dotPos = str.lastIndexOf(".") + 1;
		if (str.substring(dotPos).equals("jpg")) {
			return true;
		}
		if (str.substring(dotPos).equals("gif")) {
			return true;
		}
		if (str.substring(dotPos).equals("bmp")) {
			return true;
		}
		if (str.substring(dotPos).equals("png")) {
			return true;
		}
		return false;
	}

	private void createFolders(String path) {
		File index = new File(path);
		if (!index.exists()) {
			index.mkdir();
		} else {

			String[] entries = index.list();
			for (String s : entries) {
				File currentFile = new File(index.getPath(), s);
				currentFile.delete();
			}
			index.delete();
			index.mkdir();
		}
	}

	private void removeFolders() {
		createFolders("C:/sick");
		createFolders("C:/suspect");
		createFolders("C:/phone");
	}
}
