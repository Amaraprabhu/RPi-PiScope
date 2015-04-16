package com.piscope;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PiPreferenceController implements Initializable {

	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;
	PiController picontroller;
	private Scene scene;

	// Checkboxes
	@FXML
	private CheckBox HGrid = new CheckBox();
	@FXML
	private CheckBox VGrid = new CheckBox();
	@FXML
	private CheckBox HZero = new CheckBox();
	@FXML
	private CheckBox VZero = new CheckBox();
	@FXML private CheckBox PlotH = new CheckBox();
	@FXML private CheckBox DropS = new CheckBox();

	// Colour Picker Fields
	@FXML
	private ColorPicker CHGrid=new ColorPicker();
	@FXML
	private ColorPicker CVGrid=new ColorPicker();
	@FXML
	private ColorPicker CHZero=new ColorPicker();
	@FXML
	private ColorPicker CVZero=new ColorPicker();
	@FXML
	private ColorPicker CPlotB=new ColorPicker();
	@FXML
	private ColorPicker CLine=new ColorPicker();
	@FXML
	private ColorPicker CLineS=new ColorPicker();
	@FXML private ColorPicker CPlotH=new ColorPicker();
	@FXML private ColorPicker CDropS=new ColorPicker();

	//Opacity Variables
	@FXML private Slider OHGrid = new Slider();
	@FXML private Slider OVGrid = new Slider();
	@FXML private Slider OHZero = new Slider();
	@FXML private Slider OVZero = new Slider();
	@FXML private Slider OLine = new Slider();
	@FXML private Slider OLineS = new Slider();
	@FXML private Slider OPlotH = new Slider();
	@FXML private Slider ODropS = new Slider();
	
	
	//Width Variables
	@FXML private Slider WHGrid = new Slider();
	@FXML private Slider WVGrid = new Slider();
	@FXML private Slider WHZero = new Slider();
	@FXML private Slider WVZero = new Slider();
	@FXML private Slider WLine = new Slider();
	@FXML private Slider WLineS = new Slider();
	@FXML private Slider WPlotH = new Slider();
	@FXML private Slider WDropS = new Slider();
	
	static //Colour Variables
	int[] rgb=new int[3];

	PiMain main = new PiMain();

	@FXML
	private CheckBox vgrid = new CheckBox();

	@FXML
	private Button prefButton;

	public PiPreferenceController() {
		// TODO Auto-generated constructor stub

	}

	public void readProp() {
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// Grid lines property
			HGrid.setSelected(Boolean.valueOf(prop.getProperty("HGrid")));
			VGrid.setSelected(Boolean.valueOf(prop.getProperty("VGrid")));
			HZero.setSelected(Boolean.valueOf(prop.getProperty("HZero")));
			VZero.setSelected(Boolean.valueOf(prop.getProperty("VZero")));
			PlotH.setSelected(Boolean.valueOf(prop.getProperty("PlotH")));
			DropS.setSelected(Boolean.valueOf(prop.getProperty("DropS")));
			

			
		    CHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));			
			CVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));			
			CHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));			
			CVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));

			// Grid Colour line Property
			
			HextoRGB(prop.getProperty("CHGrid"));
			CHGrid.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CVGrid"));
			CVGrid.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CHZero"));
			CHZero.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));			
			HextoRGB(prop.getProperty("CVZero"));
			CVZero.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CPlotB"));
			CPlotB.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CLine"));
			CLine.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			HextoRGB(prop.getProperty("CLineS"));
			CLineS.setValue(Color.rgb(rgb[0], rgb[1], rgb[2]));
			
			//Opacity variables
			OHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));
			OVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));
			OHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));
			OVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));
			OPlotH.setDisable(!Boolean.valueOf(prop.getProperty("PlotH")));
			ODropS.setDisable(!Boolean.valueOf(prop.getProperty("DropS")));
			
			
			//Width Variables
			WHGrid.setDisable(!Boolean.valueOf(prop.getProperty("HGrid")));
			WVGrid.setDisable(!Boolean.valueOf(prop.getProperty("VGrid")));
			WHZero.setDisable(!Boolean.valueOf(prop.getProperty("HZero")));
			WVZero.setDisable(!Boolean.valueOf(prop.getProperty("VZero")));
			WPlotH.setDisable(!Boolean.valueOf(prop.getProperty("PlotH")));
			WDropS.setDisable(!Boolean.valueOf(prop.getProperty("DropS")));
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			if (input != null)
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

	}

	public void writeProp(String key, String value) {
		prop.setProperty(key, value);
	}

	void dialogBuild() throws IOException {
		Stage dialogStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"PiPreference.fxml"));
		//loader.setController(new PiPreferenceController());
		BorderPane root = (BorderPane) loader.load();
		scene = new Scene(root);
		dialogStage.setScene(scene);
		dialogStage.initStyle(StageStyle.UTILITY);
		dialogStage.showAndWait();
		

	}
	

	//This method is used to convert Color object to String
	public static String ColortoHex( Color color )
    {
        return String.format( "%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
	
	//This method is used to convert Hex String to RGB
	public static int[] HextoRGB(final String hex)
	{
	    //final int[] ret = new int[3];
	    for (int i = 0; i < 3; i++)
	    {
	        rgb[i] = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
	    }
	    return rgb;
	    }

	
	


	public void savePref() {
		try {
			output = new FileOutputStream("config.properties");

			// Set properties of Grid lines
			prop.setProperty("HGrid", String.valueOf(HGrid.isSelected()));
			prop.setProperty("VGrid", String.valueOf(VGrid.isSelected()));
			prop.setProperty("HZero", String.valueOf(HZero.isSelected()));
			prop.setProperty("VZero", String.valueOf(VZero.isSelected()));
			prop.setProperty("PlotH", String.valueOf(PlotH.isSelected()));
			prop.setProperty("DropS", String.valueOf(DropS.isSelected()));

			// Set property of colour
			prop.setProperty("CHGrid",ColortoHex(CHGrid.getValue()));
			prop.setProperty("CVGrid",ColortoHex(CVGrid.getValue()));
			prop.setProperty("CHZero",ColortoHex(CHZero.getValue()));
			prop.setProperty("CVZero",ColortoHex(CVZero.getValue()));			
			prop.setProperty("CPlotB",ColortoHex(CPlotB.getValue()));
			prop.setProperty("CLine",ColortoHex(CLine.getValue()));
			prop.setProperty("CLineS",ColortoHex(CLineS.getValue()));
			
			
			
			 

			prop.store(output, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	// Close Function for button
	@FXML
	public void close() {
		//removeListeners();
		savePref();
		prefButton.getScene().getWindow().hide();
	}

	// This function is used to disable the textfield if grid is not enabled.
	void disable(ColorPicker colourPicker, boolean val) {
		colourPicker.setDisable(val);
	}
	
	// This function is used to disable the textfield if grid is not enabled.
		void disable(Slider slider, boolean val) {
			slider.setDisable(val);
		}

	public void addListeners() {

		// Horizontal Grid Listener
		HGrid.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CHGrid, old_val);
				disable(OHGrid,old_val);
				disable(WHGrid,old_val);
			}
		});

		// Vertical Grid Listener
		VGrid.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CVGrid, old_val);
				disable(OVGrid,old_val);
				disable(WVGrid,old_val);
			}
		});

		// Horizontal Grid Listener
		HZero.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CHZero, old_val);
				disable(OHZero, old_val);
				disable(WHZero, old_val);
			}
		});

		// Horizontal Grid Listener
		VZero.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				disable(CVZero, old_val);
				disable(OVZero, old_val);
				disable(WVZero, old_val);
			}
		});
		
		// Plot Highlight Listener
				PlotH.selectedProperty().addListener(new ChangeListener<Boolean>() {
					public void changed(ObservableValue<? extends Boolean> ov,
							Boolean old_val, Boolean new_val) {
						disable(CPlotH, old_val);
						disable(OPlotH,old_val);
						disable(WPlotH,old_val);
					}
				});
				
				// Drop Shadow Listener
				DropS.selectedProperty().addListener(new ChangeListener<Boolean>() {
					public void changed(ObservableValue<? extends Boolean> ov,
							Boolean old_val, Boolean new_val) {
						disable(CDropS, old_val);
						disable(ODropS,old_val);
						disable(WDropS,old_val);
					}
				});

		/*
		// Horizontal Grid lines Colour Listener
		CHGrid.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CHGrid.lengthProperty().get() == 3
						|| CHGrid.lengthProperty().get() == 6)
					rect1.setFill(Color.valueOf("#" + CHGrid.getText()));
			}
		});

		// Vertical Grid lines Colour Listener
		CVGrid.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CVGrid.lengthProperty().get() == 3
						|| CVGrid.lengthProperty().get() == 6)
					rect2.setFill(Color.valueOf("#" + CVGrid.getText()));
			}
		});

		// Horizontal Zero lines Colour Listener
		CHZero.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CHZero.lengthProperty().get() == 3
						|| CHZero.lengthProperty().get() == 6)
					rect3.setFill(Color.valueOf("#" + CHZero.getText()));
			}
		});

		// Vertical Zero lines Colour Listener
		CVZero.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CVZero.lengthProperty().get() == 3
						|| CVZero.lengthProperty().get() == 6)
					rect4.setFill(Color.valueOf("#" + CVZero.getText()));
			}
		});

		// Plot Background Colour Listener
		CPlotB.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CPlotB.lengthProperty().get() == 3
						|| CPlotB.lengthProperty().get() == 6)
					rect5.setFill(Color.valueOf("#" + CPlotB.getText()));
			}
		});

		// Line Colour Listener
		CLine.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CLine.lengthProperty().get() == 3
						|| CLine.lengthProperty().get() == 6)
					rect6.setFill(Color.valueOf("#" + CLine.getText()));
			}
		});

		// Line Selected Colour Listener
		CLineS.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(
					final ObservableValue<? extends String> observable,
					final String oldValue, final String newValue) {
				if (CLineS.lengthProperty().get() == 3
						|| CLineS.lengthProperty().get() == 6)
					rect7.setFill(Color.valueOf("#" + CLineS.getText()));
			}
		});
		*/

	}

	// This method is used to remove all the listeners
	public void removeListeners() {
		/*
		HGrid.setOnAction(null);
		VGrid.setOnAction(null);
		HZero.setOnAction(null);
		VZero.setOnAction(null);
		CHGrid.setOnAction(null);
		CVGrid.setOnAction(null);
		CHZero.setOnAction(null);
		CVZero.setOnAction(null);
		CPlotB.setOnAction(null);
		CLine.setOnAction(null);
		CLineS.setOnAction(null);
		*/
	}

	// Initialization Function
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addListeners();
		readProp();
	}

	@FXML
	public void reset() {
		try {
			output = new FileOutputStream("config.properties");
			// Set default properties
			prop.setProperty("HGrid", "false");
			prop.setProperty("VGrid", "true");
			prop.setProperty("HZero", "true");
			prop.setProperty("VZero", "false");
			prop.setProperty("CHGrid", "FFFFFF");
			prop.setProperty("CVGrid", "3278fa");
			prop.setProperty("CHZero", "3278fa");
			prop.setProperty("CVZero", "3278fa");
			prop.setProperty("CPlotB", "040603");
			prop.setProperty("CLine", "007701");
			prop.setProperty("CLineS", "A9A9A9");
			prop.setProperty("PlotH", "true");
			prop.setProperty("DropS", "true");

			prop.store(output, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
					readProp();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
}
