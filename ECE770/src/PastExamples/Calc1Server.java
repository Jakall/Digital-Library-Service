package PastExamples;

import java.io.*;
import java.net.*;

/**
 * 
 */

/**
 * @author een570
 *
 */
public class Calc1Server {
	
	public static void sendMessage(PrintWriter spr, String mess) {
		int len = mess.length();
		spr.print(String.format("%02d",len)+":"+mess);
		spr.flush();
	}
	
	public static String recvMessage(BufferedReader sbr) {
		try {
			char[] slen = new char[3];
			sbr.read(slen,0,3);
			int len = Integer.parseInt(new String(slen,0,2));
			char[] sdata = new char[len];
			sbr.read(sdata,0,len);
			return new String(sdata);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			ServerSocket ss = new ServerSocket(45000);
			
			Socket cs = ss.accept();
			
			System.out.println("Received connection from:"+cs.getInetAddress().getHostAddress()+":"+cs.getPort());
			
			BufferedReader cbr = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			PrintWriter cpr = new PrintWriter(cs.getOutputStream());
			
			String req = recvMessage(cbr);
			System.out.println(req);
			String[] tokens = req.split(":");
			int op1 = Integer.parseInt(tokens[2]);
			sendMessage(cpr,"OK");
			
			
			req = recvMessage(cbr);
			System.out.println(req);
			String[] tokens2 = req.split(":");
			String opr = tokens2[1];
			sendMessage(cpr,"OK");

			req = recvMessage(cbr);
			System.out.println(req);
			String[] tokens3 = req.split(":");
			int op2 = Integer.parseInt(tokens3[2]);
			sendMessage(cpr,"OK");
			
			int result = 0;
			switch(opr.charAt(0)) {
			case '+':
				result = op1+op2;
				break;
			default:
				break;
			}
			
			sendMessage(cpr,"res:int:"+result);
			req = recvMessage(cbr);
			System.out.println(req);
			
			cbr.close();
			cpr.close();
			cs.close();
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
