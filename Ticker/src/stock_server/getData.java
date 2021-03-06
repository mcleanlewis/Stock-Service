package stock_server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

import javax.swing.text.AbstractDocument.BranchElement;

public class getData implements Runnable {

	private ArrayList<LastStock> symb;
	// private ArrayList<LastStock> symbHist;
	private Ticker ticker;
	private String shortcode;
	float bid, ask, price, low, high;
	Calendar cal;
	SimpleDateFormat date, time;
	QuoteServerThread server;
	DatagramSocket socket;

	public getData() {

		// this.con = con;
		ticker = new Ticker();
		
	
		symb = new ArrayList<LastStock>();
		symb = GC.getList();
		cal = Calendar.getInstance();
		date = new SimpleDateFormat("dd-MMM-yy");
		time = new SimpleDateFormat("dd-MMM-yy hh:mm.ss.SSS a");
		// symbHist = new ArrayList<LastStock>();

		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// socket connection server - runs on another thread
		try {
			server = new QuoteServerThread();
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getResults() {

		for (int i = 0; i < symb.size(); i++) {

			LastStock current = symb.get(i);

			// symbol lasttrade 52wlow 52whigh daysvaluechange lasttradeprice
			// lasttradetime askRT bidRT

			String[] results = ticker.getStockInfo(current.getSymb());

			try {
				// shortcode = results[0].replaceAll("\"", "");
				// price = (Float.valueOf(results[5]));
				// low = (Float.valueOf(results[2]));
				// high = (Float.valueOf(results[3]));
				// bid = (Float.valueOf(results[8]));
				// ask = (Float.valueOf(results[7]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// to check if this is the 1st run, if so populate the history
			// variables
			if (current.isFirst()) {
				current.firstStock(bid, ask, price, low, high);
				sendPrice();
			}

			// to compare the values to last stock history
			else if (current.compare(bid, ask, price, low, high)) {
				sendPrice();
			} else {
				System.out.println("same as before");
			}

		}// end else if

	}

	private void sendPrice() {

		Calendar rightNow = Calendar.getInstance();
		Statement s;

		try {

			// if(conn.isConnected() == true){
			// conn.sendPrice( shortcode + "," +time.format(rightNow.getTime())+
			// "," +price+ "," +low+ "," +high+ "," +bid+ "," +ask);

			// }

			// to send to the socket server

			String quote = "" + shortcode + ","
					+ time.format(rightNow.getTime()) + "," + price + "," + bid
					+ "," + ask + "";

			if (server.connStatus() == true) {
				ArrayList<Hosts> hosts = server.getHosts();

				byte[] buf = new byte[256];

				DatagramPacket packet;

				for (int i = 0; i < hosts.size(); i++) {

					Hosts temp = hosts.get(i);

					System.out.println(temp.getPort() + "  " + temp.getInet());

					buf = quote.getBytes();

					// send the response to the client at "address" and "port"

					packet = new DatagramPacket(buf, buf.length,
							temp.getInet(), temp.getPort());

					socket.send(packet);

				}
				// socket.close();
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void ReadFile() throws IOException {
		BufferedReader br = null;
		try {
			String line;
			HashMap<String, String> codes = new HashMap<String, String>();
			br = new BufferedReader(new FileReader(
					"stockcodes.txt"));
			StringTokenizer st = null;

			while ((line = br.readLine()) != null) {
				st = new StringTokenizer(line, "/n");
				while (st.hasMoreTokens()) {
					codes.put(st.nextToken(), null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	finally{
		br.close();
	}

	}
}
