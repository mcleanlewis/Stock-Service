/**
 * 
 */
package Sockets;

import helpers.Configuration;

import java.util.List;

import Beans.Stock;
import Interfaces.SendServerSide;

/**
 * @author lewismclean
 *
 */
public class Sockets implements SendServerSide {

	private final Configuration configuration;

	/**
	 * 
	 */
	public Sockets(Configuration configuration) {
		this.configuration = configuration;
		// TODO Auto-generated constructor stub

		// set up connection thread with concurrent queue to handle connection
		// requests via UDP
		// connection request is then handed off a port for a ssl connection
		// cert is used for 1 step auth
		// 2 factor is passed to AUTH service
		// AUTH replies to what stocks are available
		//
		//

	}

	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#sendTick(Beans.Stock)
	 */
	@Override
	public void sendTick(Stock tick) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#getConnectedClients(java.util.List)
	 */
	@Override
	public void getConnectedClients(List clients) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see Interfaces.SendServerSide#getNumberOfConnectedClients()
	 */
	@Override
	public int getNumberOfConnectedClients() {
		// TODO Auto-generated method stub
		return 0;
	}

}
