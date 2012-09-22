package stock_server;
import java.net.InetAddress;


public class Hosts {
		
	int port;
	InetAddress inet;
	
	public Hosts(InetAddress inet, int port){
		
		this.port = port;
		this.inet = inet;
	
	}
	
	public InetAddress getInet(){
		
		return inet;
		
	}
	
	public int getPort(){
		
		return port;
		
	}
	
	
}
