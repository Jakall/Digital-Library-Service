package PastExamples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 */

/**
 * @author een570
 *
 */
public class Calc1Client {
	
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
			Socket s = new Socket("localhost",7);
			
			BufferedReader sbr = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter spr = new PrintWriter(s.getOutputStream());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Enter an operand:");
			String op1 = br.readLine();
			
			Calc1Client.sendMessage(spr, "opa:int:"+op1);
			String resp = Calc1Client.recvMessage(sbr);
			System.out.println("Got:"+resp);
			
			System.out.println("Enter an operator:");
			String opr = br.readLine();
			
			Calc1Client.sendMessage(spr, "opr:"+opr);
			resp = Calc1Client.recvMessage(sbr);
			System.out.println("Got:"+resp);
			
			System.out.println("Enter an operand:");
			String op2 = br.readLine();
			
			Calc1Client.sendMessage(spr, "opa:int:"+op2);
			resp = Calc1Client.recvMessage(sbr);
			System.out.println("Got:"+resp);
			resp = Calc1Client.recvMessage(sbr);
			if (resp!=null) {
				System.out.println(resp);
			}
			Calc1Client.sendMessage(spr, "OK");
			
			sbr.close();
			spr.close();
			br.close();
			s.close();

			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
