package GT;

import helpers.Configuration;

import java.util.List;

import Beans.Stock;
import Interfaces.SendServerSide;

public class GT_impl extends Thread implements SendServerSide {

	private final Configuration configuration;

	public GT_impl(Configuration configuration) {
		this.configuration = configuration;

		// start a thread here

	}


	@Override
	public void run() {


	}

	@Override
	public void sendTick(Stock tick) {

		// uses imp for communication
		//

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
