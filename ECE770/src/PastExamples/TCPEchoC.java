package PastExamples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;




public class TCPEchoC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		
		try {
			
			Socket s = new Socket(hostname,port);
			
			BufferedInputStream sin = new BufferedInputStream(s.getInputStream());
			BufferedOutputStream sout = new BufferedOutputStream(s.getOutputStream());
			
			BufferedReader bin = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Enter Data:");
			String user;
			while((user=bin.readLine()) != null) {
				
				sout.write(user.getBytes());
				sout.flush();
				
				System.out.println("Sent:"+user);
				
				byte[] serv = new byte[user.length()];
				
				sin.read(serv);
				
				System.out.println("Read:"+(new String(serv)));
				
				System.out.println("Enter Data:");

			}
			
			sin.close();
			sout.close();
			bin.close();
			
			s.close();
			
			
		} catch (Exception e) {
			System.out.println("Error:"+e.getMessage());
		}

	}

}
