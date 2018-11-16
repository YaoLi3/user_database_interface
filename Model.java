package assignment3;

import java.io.IOException;
import java.util.List;

/**
 * Model class for the Character Editor
 * @author leggy (Lachlan Healey)
 */
public class Model {
	/**
	 * The database
	 */
	private CharacterDatabase database;

	public Model() {
	}
	/**
	 * Load a database Set object from file
	 * @param filePath: String, file path to database store
	 * @throws Exception
	 */
	public void loadDatabase(String filePath) throws Exception {
		database = new CharacterDatabase(filePath);
		database.load();
	}
	/**
	 * Get the names of characters
	 * @return: a list of String objects 
	 * which are names of character in the database
	 */
	public List<String> getNames() {
		List<String> names = database.getCharacterNames();
		return names;
	}
	/**
	 * Save a database Set object to file
	 * @throws IOException
	 */
	public void saveDatabase() throws IOException {
		database.save();
	}
	/**
	 * Add a character to the database
	 * @param c: Character object
	 */
	public void addCharacter(Character c) {
		database.add(c);
	}
	/**
	 * Updates a character in the database if it present, adding the character if it
	 * is not present.
	 * @param c: Character object
	 */
	public void updateCharacter(Character c) {
		database.update(c);
	}
	/**
	 * Remove wanted character from the database
	 * @param character: Character object
	 */
	public void deleteCharacter(Character character) {
		database.remove(character);
	}
	/**
	 * Search a character in database by its name
	 * @param name: String, character's name
	 * @return The clone of the character if found,
	 * 			null if result not found
	 */
	public Character searchCharacter(String name) {
		return database.search(name);
	}
}


