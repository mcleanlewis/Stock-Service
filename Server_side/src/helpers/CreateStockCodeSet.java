package helpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import Beans.Stock;

public class CreateStockCodeSet {

	/**
	 * Helper class to create and persist the stock codes
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String lineString;
		BufferedReader br = null;

		HashSet<Stock> hashSetToPersist = new HashSet<Stock>();
		try {
			br = new BufferedReader(new FileReader("stockcodes.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while ((lineString = br.readLine()) != null) {
			hashSetToPersist.add(new Stock(lineString));
		}

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream("HashStockSet.data");
			out = new ObjectOutputStream(fos);
			out.writeObject(hashSetToPersist);
			out.close();
			System.out.println("Object Persisted");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}




