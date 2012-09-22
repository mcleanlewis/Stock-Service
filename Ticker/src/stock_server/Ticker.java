package stock_server;
import java.io.BufferedReader;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLConnection;  
import java.util.Date;  
import java.util.HashMap;  

  
public class Ticker {  
 
    
	String[] stockInfo;
	
  
  public Ticker() {}  
  
    //This is synched so we only do one request at a time  
    //If yahoo doesn't return stock info we will try to return it from the map in memory  
    public String[] getStockInfo(String symbol) {  
        try {  
        	
        	
        	String ticker;
        	Float price;
        	float bid;
        	float ask;
        	float change;
        	
        	
        	//"http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=st1jkw4l1lb2b3"
        	//symbol last trade 52wlow 52whigh daysvaluechange last tradeprice lasttradetime askRT bidRT 
        	
            URL yahoofin = new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=st1jkw4l1lb2b3&e=.csv");  
            URLConnection yc = yahoofin.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));  
            String inputLine;  
            while ((inputLine = in.readLine()) != null) {  
                stockInfo = inputLine.split(",");  
            
            }  
            in.close();  
        } catch (Exception ex) {  
              
        }  
        return stockInfo; 
     }  
}  