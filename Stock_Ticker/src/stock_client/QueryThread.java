package stock_client;

import java.io.IOException;


public class QueryThread extends Thread {

	
		
		public QueryThread() throws IOException {
        	this("QuoteClient");
            }
        public QueryThread(String name) throws IOException {
            super(name);
		
		
		
	}
	
	public void run(){
		
		QuerySelector inst = new QuerySelector();
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);	
	
	}
}
