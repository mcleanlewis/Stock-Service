package Interfaces;

import java.util.List;

import Beans.Stock;

public interface SendServerSide {

	public void sendTick(Stock tick);

	public void getConnectedClients(List clients);

	public int getNumberOfConnectedClients();

}
