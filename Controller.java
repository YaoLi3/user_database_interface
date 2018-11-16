package assignment3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {
	/** The model manipulates the data */
	private Model model;
	
	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

    @FXML
    private Button load;

    @FXML
    private Button save;

    @FXML
    private Button create;

    @FXML
    private Button deleteSelectedChar;

    @FXML
    private Button deleteName;

    @FXML
    private Button newChar;

    @FXML
    private Button newSuperChar;

    @FXML
    private Button search;

    @FXML
    private Button clearSearch;

    @FXML
    private Button changeImage;

    @FXML
    private Button saveChange;

    @FXML
    private Label loadDatabase;

    @FXML
    private Label title;

    @FXML
    private Label createNewDatabase;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField databaseName;

    @FXML
    private TextField charName;

    @FXML
    private TextField charNameSearch;

    @FXML
    private Label searchByName;

    @FXML
    private Label clearCurrentSearch;

    @FXML
    private ImageView imageView;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label traitsLabel;

    @FXML
    private Label powerRankingLabel;

    @FXML
    private Label powersLabel;

    @FXML
    private Label charNameDisplay;

    @FXML
    private TextField description;

    @FXML
    private TextField powerRanking;

    @FXML
    private TextArea traits;

    @FXML
    private TextArea powers;

    @FXML
    private Separator bar;
	
	@FXML
	private ObservableList<String> listViewData = FXCollections.observableArrayList();
	
	@FXML
	public static Alert alert = new Alert(AlertType.INFORMATION);
	
	/** Store boolean value of whether a character is SuperCharacter */
	public static boolean isSuperChar = false;
	
	/** Name of the character being selected in the list view */
	public static String selectedChar;
	
	/** The path of the image of a character */
	public String imagePath;
	
	/** Database loading status */
	public static boolean loadStatus = false;
	
	/**
	 * Send message to user when needed
	 * @param s: String, message shared with users
	 */
	public void sendMessage(String s) {
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.showAndWait();
	}
	
	/**
	 * Set data being displayed in the main window
	 * @param c: Character object c
	 */
	public void setMainWindow(Character c) {
		charNameDisplay.setText(c.getName());
		description.setText(c.getDescription());
		traits.setText(c.getTraits().toString());
		
		try {
			imageView.setImage(new Image(new FileInputStream(c.getImagePath())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (c instanceof SuperCharacter) {
			powerRanking.setText(String.valueOf(
					((SuperCharacter) c).getPowerRanking()));
			powers.setText(((SuperCharacter) c).getPowers().toString());
		}else {
			powerRanking.setText("not avaliable");
			powers.setText("not avaliable");
		}
	 }
	
	/**
	 * Reset main window, to display default data
	 */
	 public void reset() {
		 description.setText("");
		 traits.setText("");
		 powerRanking.setText("");
		 powers.setText("");
		 File file = new File("images/default.png");
		 Image image = new Image(file.toURI().toString());
		 imageView.setImage(image);
	 }
	 
	 @FXML
	 public void load(ActionEvent event) throws Exception{
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Character Database File");
		 File file = fileChooser.showOpenDialog(new Stage());
		 if (file != null) {
			 String filePath = file.getPath();
			 model.loadDatabase(filePath);
			 List<String> names = model.getNames();
			 for (String i : names) {
				 listViewData.add(i);}
			 listView.setItems(listViewData.sorted());
			 loadStatus = true;
		 }
	 }
	 
	 @FXML
	 public void save(ActionEvent event) throws IOException {
		 try {
			 model.saveDatabase();
		 }catch (RuntimeException e) {
			 sendMessage("Database does not exist. \nLoad a database first.");
		 }
	 }
	 
	 @FXML
	 public void createDatabase(ActionEvent event) throws Exception {
		 String name = databaseName.getText();
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Character Database File");
		 fileChooser.setInitialFileName(name+".dat");
		 File file = fileChooser.showSaveDialog(new Stage());
		 if (file != null && !file.exists() && file.getParentFile().isDirectory()) {
			 CharacterDatabase database = new CharacterDatabase(file.getPath());
			 database.save();
		 }
	 }
	 
	 @FXML
	 public void select(MouseEvent event) {
		 String value = (String) listView.getSelectionModel().getSelectedItem();
		 selectedChar = value;
		 if (model.searchCharacter(value) != null) {
			 setMainWindow(model.searchCharacter(value));
		 }else {
			 charNameDisplay.setText(value);
			 reset();
		 }
	 }
	 
	 @FXML
	 public void deleteSelected(ActionEvent event) throws Exception {
		 try {
			 Character character = model.searchCharacter(selectedChar);
			 if (character != null) {
				 model.deleteCharacter(character);
				 model.saveDatabase();
				 listViewData.remove(selectedChar);
				 charNameDisplay.setText("Name");
				 reset();
			 }else {
				 listViewData.remove(selectedChar);
			 }
		 }catch(NullPointerException n) {
			 sendMessage("Please select a character first.");
		 }
	 }
	 
	 @FXML
	 public void deleteByName(ActionEvent event) throws FileNotFoundException, Exception {
		 String value = charName.getText();
		 if (value.trim().equals("")) {
			 sendMessage("Enter a name first.");
		 }else {
			 if (model.searchCharacter(value) != null) {
				 model.deleteCharacter(model.searchCharacter(value));
				 model.saveDatabase();
				 listViewData.remove(value);
				 reset();
			 }else {
				 sendMessage("Character not exist");
			 }
		 }
	 }

    @FXML
    public void search(ActionEvent event) {
    		String name = charNameSearch.getText();
    		if (name.trim().equals("")) {
    			sendMessage("Enter a name first.");
    		}else {
    			try {
    				setMainWindow(model.searchCharacter(name));
    			}catch (NullPointerException n) {
   				 sendMessage("Character not exist");
    			}
    		}
    }
    
	@FXML
	public void clearSearch(ActionEvent event) {
		if (loadStatus) {
			charName.setText("");
			charNameSearch.setText("");
			charNameDisplay.setText("Name");
			reset();
		}else {
			sendMessage("Load database first please.");
		}
	}

	@FXML
	public void newCharacter(ActionEvent event) {
		isSuperChar = false;
		String name = charName.getText();
		if (name.trim().equals("")) {
			sendMessage("Please enter a name first");
		}else if (model.searchCharacter(name) != null){
			sendMessage("Character exists.");
		}else {
			listViewData.add(name);
			powerRanking.setEditable(isSuperChar);
			powers.setEditable(isSuperChar);
		}
	}
	
	@FXML
	public void newSuperCharacter(ActionEvent event) {
		isSuperChar = true;
		String name = charName.getText();
		if (name.trim().equals("")) {
			sendMessage("Please enter a name first");
		}else if (model.searchCharacter(name) != null){
			sendMessage("Character exists.");
		}else {
			listViewData.add(name);
			powerRanking.setEditable(isSuperChar);
			powers.setEditable(isSuperChar);
		}
	}

	@FXML
	public void changeImage(ActionEvent event) {
		if (loadStatus) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Change Images");
			File file = fileChooser.showOpenDialog(new Stage());
			if (file != null) {
				this.imagePath = file.getPath();
				Image image = new Image(file.toURI().toString());
				imageView.setImage(image);
			}
		}else {
			sendMessage("Load a database first please");
		}
	}
	
	/**
	 * Get the data for the new characters
	 */
	public void createNewCharacters() {
		if (isSuperChar) {
			SuperCharacter c;
			try {
				c = new SuperCharacter (charName.getText(), description.getText(), Integer.valueOf(powerRanking.getText()));
				c.addTrait(traits.getText());
				c.addPower(powers.getText());
				c.setImagePath(this.imagePath);	
				model.updateCharacter(c);
			} catch (NumberFormatException | IllegalPowerRankingException e) {
				sendMessage("Need the right information");
			}
		}else {
			try {
				Character c = new Character (charName.getText(), description.getText());
				c.addTrait(traits.getText());
				c.setImagePath(this.imagePath);	
				model.updateCharacter(c);
			}catch(IllegalArgumentException i) {
				sendMessage("Need the right information");
			}
		}
	}
	
	/**
	 * Capture the data for existed characters
	 */
	public void changeCurrentCharacters() {
		if (model.searchCharacter(selectedChar) instanceof SuperCharacter) {
			String imagePath = model.searchCharacter(selectedChar).getImagePath();
			SuperCharacter c;
			try {
				c = new SuperCharacter 
						(charNameDisplay.getText(), description.getText(), 
								Integer.valueOf(powerRanking.getText()));
				c.addTrait(traits.getText());
				c.addPower(powers.getText());
				c.setImagePath(imagePath);	
				model.updateCharacter(c);
			} catch (NumberFormatException | IllegalPowerRankingException e) {
				sendMessage("Need the right information");
			}
		}else {
			try {
				String imagePath = model.searchCharacter(selectedChar).getImagePath();
				Character c = new Character (charNameDisplay.getText(), description.getText());
				c.addTrait(traits.getText());
				c.setImagePath(imagePath);	
				model.updateCharacter(c);
			}catch(IllegalArgumentException i) {
				sendMessage("Need the right information");
			}
		}
	}
		
	@FXML
	public void saveChanges(ActionEvent event) throws IOException {
		if (loadStatus) {
			if (model.searchCharacter(selectedChar) == null) {
				createNewCharacters();	
			}else {
				changeCurrentCharacters();
			}
		}else {
			sendMessage("Load a database first please");
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model = new Model();	
		File file = new File("images/default.png");
        Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		charNameDisplay.setText("Name");
	}
}
