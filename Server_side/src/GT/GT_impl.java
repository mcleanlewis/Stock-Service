package GT;

import helpers.Configuration;

import java.util.List;

import Beans.Stock;
import Interfaces.SendServerSide;

public class GT_impl extends Thread implements SendServerSide {

	private final Configuration configuration;

	public GT_impl(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void run() {

		// connection stuff in here in a loop
		// perhaps pass the non-blocking queue to this class via constructor?

	}

	@Override
	public void sendTick(Stock tick) {
		// TODO Auto-generated method stub
		System.out.println("sent stock");

	}

	@Override
	public void getConnectedClients(List clients) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumberOfConnectedClients() {
		// TODO Auto-generated method stub
		return 0;
	}

}
