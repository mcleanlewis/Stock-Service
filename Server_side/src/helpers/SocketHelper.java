package helpers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SocketHelper {

	public static String convertStreamToString(InputStream is)
			throws IOException {
		BufferedInputStream bin = new BufferedInputStream(is);
		byte[] contents = new byte[1024];

		int bytesRead = 0;
		String strFileContents = null;
		while ((bytesRead = bin.read(contents)) != -1) {
			strFileContents = new String(contents, 0, bytesRead);
		}
		System.out.print(strFileContents);
		return strFileContents;
	}

}
