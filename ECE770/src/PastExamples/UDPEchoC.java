package PastExamples;

import java.io.*;
import java.net.*;

public class UDPEchoC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		try {
			DatagramSocket ds = new DatagramSocket();
			
			InetAddress raddr = InetAddress.getByName(hostname); 
			
			String userin;
			BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Enter data:");
			
			while((userin=bin.readLine())!=null) {
				
				DatagramPacket pout = new DatagramPacket(userin.getBytes(), userin.length(), raddr, port);
				
				ds.send(pout);
				
				byte buf[] = new byte[userin.length()];
				DatagramPacket pin = new DatagramPacket(buf, userin.length());
								
				ds.receive(pin);
				System.out.println("From:"+pin.getAddress()+":"+pin.getPort());
				//System.out.println("From:"+pin.getSocketAddress());
				
				System.out.println("Got:"+(new String(buf)));
				System.out.println("Enter data:");
			}
			
			
			
			
			
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		
		

	}

}
