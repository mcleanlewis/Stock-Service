package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration extends Properties {

	/**
	 * Helper to get the config file
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Configuration() {

		try {
			this.load(new FileInputStream(
					"/Users/lewismclean/Dropbox/CodeWorkspaces/Stock-Service/Server_side/system.prefs"));

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}