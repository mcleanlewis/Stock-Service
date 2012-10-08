package Client;

//this is a short class to store the last value of stock in an ArrayList, this is to reduce the 
//amount of overhead in the RDB by using java to analyze if it requires updating the stock
//I've used prime multiplication to compare the 5 values and it returns a boolean of true if changed and false
//if it hasn't

public class LastStock {

	private float bid, ask, price, low, high;
	private String symbol;
	
	
	//constructor
public LastStock(String symbol){
		
		
		this.symbol = symbol;
		low = 0;
		
	}
	
//to check if it's the 1st run of the updater
public boolean isFirst(){
	
	if(low != 0){
		return false;
	}
	else{
		return true;
	}
	
}

// to input the 1st run of stock prices
	public void firstStock(float bid,float ask, float price,float low,float high){
		
		this.bid = bid; 
		this.price = price;
		this.low = low; 
		this.high= high;
		this.ask = ask;
		
		
	}
	
// to compare the next run of stock prices, by multiplying floats by increasing primes and comparing
// the 2 answers, if is is false the variables are changed and true is returned
	public boolean compare(float bid,float ask, float price,float low,float high){
		
		float comparison = (bid*3 * ask*7 * price*11 * low*13 * high*17);
		
		float current = (this.bid*3 * this.ask*7 * this.price*11 * this.low*13 * this.high*17);
		
		if( current == comparison){
			
			return false;
		}
		
		else{
			this.bid = bid; 
			this.price = price;
			this.low = low; 
			this.high= high;
			this.ask = ask;
			return true;	
		}
		
		
	}
	
	public String getSymb(){
		return symbol;
	}
	
}
