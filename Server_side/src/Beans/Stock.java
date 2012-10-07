package Beans;

import java.io.Serializable;

public class Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6405852471831208L;
	private String name;
	private float price;
	private boolean hasUpdated;
	private long updateTime;

	public Stock(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	/**
	 * @return the hasUpdated
	 */
	public boolean isHasUpdated() {
		return hasUpdated;
	}
	/**
	 * @param hasUpdated the hasUpdated to set
	 */
	public void setHasUpdated(boolean hasUpdated) {
		this.hasUpdated = hasUpdated;
	}

	public boolean process(String readLine) throws Exception {

		String[] values = readLine.split(",");
		if (values[1].replace("\"", "").equals(name)) {
			throw new Exception("Yahoo gave wrong info");
		}

		float newPrice = Float.parseFloat(values[5].replace("\"", ""));
		if(newPrice == price){
			return false;
		}
		price = newPrice;
		updateTime = System.currentTimeMillis();
		return true;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
