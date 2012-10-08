package Client;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class QuoteClient {

	public static void main(String[] args) throws UnknownHostException,
			SocketException {
		//if (args.length != 1) {
		//    System.out.println("Usage: java QuoteClient <hostname>");
		//    return;
		//}
		// server is 46.137.111.115 or local is 127.0.0.1
		SimpleTableDemo gui = new SimpleTableDemo();
		InetAddress server = InetAddress.getByName("127.0.0.1");


		// get a datagram socket to recieve on 3446
		DatagramSocket socket = new DatagramSocket(3446);
		//System.out.println(args[0]);

		//SimpleTableDemo gui = new SimpleTableDemo();
		//InetAddress server = InetAddress.getByName("46.137.117.183");


		// get a datagram socket
		// socket = new DatagramSocket(3445);




		// send request
		byte[] buf = new byte[256];
		//InetAddress address = InetAddress.getByName(args[0]);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, server, 3445);
		try {
			socket.send(packet);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("packet sent");

		boolean loop = true;

		try {
			gui.createAndShowGUI();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(loop){
			// get response
			packet = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(packet != null){
				// display response
				String received = new String(packet.getData(), 0, packet.getLength());
				System.out.println(received);
				String tempArray [] = received.split(",");

				for(int i=0;i<tempArray.length;i++){
					System.out.println(tempArray[i]);

				}

				//Stock tempStock = new Stock(tempArray[0],Float.parseFloat(tempArray[2]),Float.parseFloat(tempArray[3]),Float.parseFloat(tempArray[1]));
				gui.updatePrice(received);



			}

		}

	}
	//socket.close();
}

