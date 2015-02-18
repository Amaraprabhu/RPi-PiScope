package com.piscope;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class PiMain extends Application {

	// Variable declarations
	private Stage stage;
	private BorderPane root;

	// Window Variables
	int PiWindowWidth = 740;
	int PiWindowHeight = 1150;

	// Line Variables
	double PiStrokeWidth = 1.5;
	Paint PiLineDefColour = Color.BLACK;
	Paint PiLineSelColour = Color.RED;

	// Line Path Declaratrions
	Path linePath;
	PiController piController;
	String label;

	// Measurement Variables
	int sf; // Scaling Factor
	int sfd = 1000;

	// Axis Declarations
	double xa1, xa2, ya1, ya2, diff;
	protected double initialX;
	protected double initialY;

	//Title Variable
	String PiVersion = "PiScope Beta v1.0.1";

	@Override
	public void start(Stage primaryStage) {
		try {
			// 
			// Create a Stage and a Scene
						FXMLLoader fxmlLoader = new FXMLLoader();
						root = fxmlLoader.load(getClass().getResource(
								"PiView.fxml").openStream());
						piController = (PiController) fxmlLoader.getController();
						
						Scene PiScene = new Scene(root, PiWindowHeight, PiWindowWidth);
						 

			// Add Mouse Handler to the Scene
			PiMain.MouseHandler mouseHandler = new PiMain.MouseHandler(root);

			// Associate Handler to various Mouse Events
			root.setOnMouseClicked(mouseHandler);
			root.setOnMouseDragged(mouseHandler);
			root.setOnMousePressed(mouseHandler);
			root.setOnMouseReleased(mouseHandler);
			root.setOnMouseMoved(mouseHandler);
			root.setOnMouseDragEntered(mouseHandler);

			// Add Path for the line and Path definition
			linePath = new Path();
			linePath.setStrokeWidth(PiStrokeWidth);
			linePath.setStroke(PiLineDefColour);
			root.getChildren().add(linePath);

			PiScene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			///primaryStage.initStyle(StageStyle.UNDECORATED);
			//primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(PiScene);
			primaryStage.setTitle(PiVersion);
			primaryStage.getIcons().add(
					new Image(PiMain.class.getResourceAsStream("icon.png")));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class MouseHandler implements EventHandler<MouseEvent> {

		// Variable Declarations
		private boolean gotFirst = false;
		private Line line;
		private Pane pane;
		private double x1, y1, x2, y2;
		private LineHandler lineHandler;

		public MouseHandler(Pane pane) {
			this.pane = pane;
			lineHandler = new LineHandler(pane);
		}

		@Override
		public void handle(MouseEvent event) {
			if (event.getClickCount() == 2 && event.isPrimaryButtonDown()) {
				if (!gotFirst) {

					x1 = x2 = event.getX();
					xa1 = piController.getxAxis(xa1);
					y1 = y2 = event.getY();
					ya1 = piController.getyAxis(ya1);
					line = new Line(x1, y1, x2, y2);
					line.setStrokeWidth(2.5);
					pane.getChildren().add(line);
					gotFirst = true;
				}

				else {
					line.setOnMouseEntered(lineHandler);
					line.setOnMouseExited(lineHandler);
					line.setOnMouseDragged(lineHandler);
					line.setOnMousePressed(lineHandler);
					// to consume the event
					line.setOnMouseClicked(lineHandler);
					line.setOnMouseReleased(lineHandler);
					line = null;
					gotFirst = false;
				}

			} else {
				if (line != null) {
					x2 = event.getX();
					xa2 = piController.getxAxis(xa2);
					y2 = event.getY();
					ya2 = piController.getyAxis(ya2);
					// update line
					line.setEndX(x2);
					line.setEndY(y2);
					diff = ya2 - ya1;
					if (diff <= 1)
						sf = 1000;
					else if (diff > 1 && diff < 10)
						sf = 1000;
					else if (diff >= 10 && diff < 100)
						sf = 10000;
					else if (diff >= 100 && diff < 1000)
						sf = 100000;
					else if (diff >= 1000 && diff < 10000)
						sf = 1000000;
					else if (diff >= 10000 && diff < 100000)
						sf = 10000000;

					label = String.format(
							"Voltage : %f V  Time: %f ms Frequency : %f Hz",
							xa2 - xa1, ya2 - ya1, (1 / (ya2 - ya1)) * sfd);
					piController.update(label);
				}
			}
		}

	}

	class LineHandler implements EventHandler<MouseEvent> {
		double x, y;
		Pane pane;

		public LineHandler(Pane pane) {
			this.pane = pane;
		}

		@Override
		public void handle(MouseEvent e) {
			Line l = (Line) e.getSource();

			// remove line on right click
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED
					&& e.isSecondaryButtonDown()) {
				pane.getChildren().remove(l);
			} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED
					&& e.isPrimaryButtonDown()) {
				double tx = e.getX();
				double ty = e.getY();
				double dx = tx - x;
				double dy = ty - y;
				l.setStartX(l.getStartX() + dx);
				l.setStartY(l.getStartY() + dy);
				l.setEndX(l.getEndX() + dx);
				l.setEndY(l.getEndY() + dy);
				x = tx;
				y = ty;
			} else if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
				// just to show that the line is selected
				x = e.getX();
				y = e.getY();
				l.setStroke(PiLineSelColour);
			} else if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
				l.setStroke(PiLineDefColour);
			}
			// should not pass event to the parent
			e.consume();

		}

	}

	public static void main(String[] args) {
		launch(args);
	}
	
	

}
