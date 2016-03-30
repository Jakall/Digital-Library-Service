package projects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;


/**
 * The Class LibraryClient.
 */

/**
 * @author j.callado
 */
public class LibraryClient {

	/**
	 * Send message.
	 *
	 * @param spr the spr
	 * @param mess the mess
	 */
	public static void sendMessage(PrintWriter spr, String mess) {
		int len = mess.length();
		spr.print(String.format("%03d",len)+":"+mess);
		spr.flush();
	}

	/**
	 * Recv message.
	 *
	 * @param sbr the sbr
	 * @return the string
	 */
	public static String recvMessage(BufferedReader sbr) {
		try {
			char[] slen = new char[4];
			sbr.read(slen,0,4);
			int len = Integer.parseInt(new String(slen,0,3));
			char[] sdata = new char[len];
			sbr.read(sdata,0,len);
			return new String(sdata);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void loginNewUser(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{

			LibraryClient.sendMessage(spr, "newUser");
			String resp = LibraryClient.recvMessage(sbr);
			if (resp!= null) {
				System.out.println(resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received In loginNewUser Method");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			System.out.println("Welcome to the Online Digital Library Service!");
			System.out.println("Here, We Will Create Your Personalized Account So You Can Return to Your Selections!");
			System.out.println("Please Create Your userName(a-zA-Z0-9): ");
			String userName = br.readLine();

			LibraryClient.sendMessage(spr, "login:String:"+userName);
			resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received, Invalid userID Creation");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			System.out.println("Creat A Unique Password For Your Account: ");
			String password = br.readLine();

			LibraryClient.sendMessage(spr, "login:String:" + password);
			resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received, Invalid Password Creation");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			if(resp.equals("OK")){
				System.out.println("You Are Now Logged In!");
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void loginReturningUser(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{

			LibraryClient.sendMessage(spr, "returningUser");
			String resp = LibraryClient.recvMessage(sbr);
			if (resp!= null) {
				System.out.println(resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received In ReturningUser Method");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			System.out.println("Welcome Back! Please Provide Your Exisiting Account Credentials Below");

			System.out.println("Enter Your Exisintg userName: ");
			String userName = br.readLine();

			LibraryClient.sendMessage(spr, "login:String:"+ userName);
			resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received, Invalid userID Provided");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			System.out.println("Enter your password: ");
			String password = br.readLine();

			LibraryClient.sendMessage(spr, "login:String:"+password);
			resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received, Invalid Password Provided");
					System.out.println("Returning to Main Menu");
					return;
				}
			}

			if(resp.equals("OK")){
				System.out.println("You Are Now Logged In!");
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * User sends the login request.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void login(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{

			boolean back = false;

			LibraryClient.sendMessage(spr, "login");
			String resp = LibraryClient.recvMessage(sbr);
			if (resp!= null) {
				System.out.println(resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received, Returning to Main Menu");
					return;
				}
			}

			while(!back){
				System.out.println("Enter 1-3 To Login into Your Account or Create a New One:" +'\n' + "1) New User" + '\n' 
						+ "2) Returning User" + '\n' + "3) Back to Main Menu");
				String userChoice = br.readLine();

				switch(userChoice) {
				case "1":
					System.out.println("DEBUG: Entering loginNewUser Method");
					loginNewUser(s, sbr, spr, br);
					return;
				case "2":
					System.out.println("DEBUG: Entering loginReturningUser Method");
					loginReturningUser(s, sbr, spr, br);
					return;
				case "3":
					back = true;
					System.out.println("Returning to Main Menu");
					return;
				default:
					System.out.println("Please Select a Valid Option(1-3)");
					break;
				}
			}

			System.out.println("SYSTEM LOG: Exiting login Method, Returning to Main Menu");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Checks if it is logged in by asking the server to see if it has maintained the authentication token.
	 *
	 * @param sbr the sbr
	 * @param spr the spr
	 * @return true, if is logged in
	 */
	public static boolean isLoggedIn(BufferedReader sbr, PrintWriter spr){

		//try{

		boolean loggedIn = false;

		System.out.println("Checking Login Status...");

		LibraryClient.sendMessage(spr, "isLoggedIn");
		String resp = LibraryClient.recvMessage(sbr);
		if (resp != null) {
			System.out.println(resp);
			if(resp.equals("OK")){
				loggedIn = true;
				System.out.println("You Are Logged In!");
				//return loggedIn;
			} else if(resp.equals("ERR")){
				System.out.println("SYSTEM LOG: Error Received, You Must Login First!");
			}
		}

		//System.out.println("You Are Logged In!");
		//LibraryClient.sendMessage(spr, "OK");

		/*
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(loggedIn);
		return loggedIn;

	}

	/**
	 * Send logout request to the server.
	 * Note: Upon receiving an Error when logged in, the user will be logged out to ensure that his/her information cannot be modified and 
	 * any other unwarranted behavior.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void logout(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		Boolean tokenActive = false;
		tokenActive = isLoggedIn(sbr, spr);
		System.out.println(tokenActive);
		if(tokenActive){

			LibraryClient.sendMessage(spr, "logout");
			String resp = LibraryClient.recvMessage(sbr);
			if (resp!= null) {
				System.out.println(resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received in logout Method");
					System.out.println("You Are Logged Out!"); 
					System.out.println("Returning to Main Menu"); 
					return;
				}
			}

			if(resp.equals("OK")){
				System.out.println("You Are Logged Out!");
			}

		System.out.println("Exiting Client"); 
		return;
		}
		return;
	}

	/**
	 * Send Author specific search request to the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void authorSearch(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{
			LibraryClient.sendMessage(spr, "search:String:author");
			String resp = LibraryClient.recvMessage(sbr);
			System.out.println("DEBUG: sent author Request");
			System.out.println("Got: "+resp);
			if(resp != null){
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From author Request In authorSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
			}

			System.out.println("Please Enter The Name Of The Author:");
			String authorName = br.readLine();

			LibraryClient.sendMessage(spr, "search:authorString:"+authorName);
			resp = LibraryClient.recvMessage(sbr);
			LibraryClient.sendMessage(spr, "search:authorString:sendResult");//
			resp = LibraryClient.recvMessage(sbr);//
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From authorName Request In authorSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
				String[] respArray = resp.trim().split(",");
				System.out.println("Displaying Top Author Results");
				for(int i = 0; i < respArray.length; i++){
					System.out.println("Result #"+i+": "+ respArray[i]);
				}
				System.out.println("Please Note The BookID For Your Borrow");
			}

			return;
			//LibraryClient.sendMessage(spr, "OK");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DEBUG: authorSearch Not Entered!");
		return;

	}

	/**
	 * Send Title specific search request to the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void titleSearch(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{

			LibraryClient.sendMessage(spr, "search:String:title");
			String resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got: "+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From title Request In titleSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
			}

			System.out.println("Please Enter The Title Of The Book:");
			String titleName = br.readLine();

			LibraryClient.sendMessage(spr, "search:String:"+titleName);
			//resp = LibraryClient.recvMessage(sbr);
			//LibraryClient.sendMessage(spr, "search:authorString:sendResult");//
			resp = LibraryClient.recvMessage(sbr);//
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From titleName Request In titleSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
				if(resp.contains(",")){
					String[] respArray = resp.trim().split(",");
					System.out.println("Displaying Top Title Results");
					for(int i = 0; i <= respArray.length; i++){
						System.out.println("Result #"+i+": "+ respArray[i]);
					}
				} else {
					System.out.println("Displaying Top Title Results");
					System.out.println("Result #0: "+ resp);
				}
				System.out.println("Please Note The BookID For Your Borrow");
			}

			return;


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DEBUG: titleSearch Not Entered!");
		return;

	}

	/**
	 * Send Keyword specific search request to the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void keywordSearch(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){
		try{

			LibraryClient.sendMessage(spr, "search:String:keyword");
			String resp = LibraryClient.recvMessage(sbr);
			if(resp!=null){
				System.out.println("Got: "+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From title Request In titleSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
			}

			System.out.println("Please Enter A Keyword:");
			String keywordString = br.readLine();

			LibraryClient.sendMessage(spr, "search:String:"+keywordString);
			//resp = LibraryClient.recvMessage(sbr);
			//LibraryClient.sendMessage(spr, "search:authorString:sendResult");//
			resp = LibraryClient.recvMessage(sbr);//
			if(resp!=null){
				System.out.println("Got:"+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From keywordString Request In keywordSearch Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
				resp = resp.replace(":", "");
				String[] respArray = resp.trim().split(",");
				System.out.println("Displaying Top Keyword Results");
				for(int i = 0; i < respArray.length; i++){
					System.out.println("Result #"+ i +": "+ respArray[i]);
				}
				System.out.println("Please Note The BookID For Your Borrow");
			}

			return;


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("DEBUG: keywordSearch Not Entered!");
		return;

	}

	/**
	 * Prompt user to select specific Search function.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void search(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		try{
			boolean back = false; 

			LibraryClient.sendMessage(spr, "search");
			String resp = LibraryClient.recvMessage(sbr);
			if (resp!= null) {
				System.out.println("Got: "+resp);
				if(resp.equals("ERR")){
					System.out.println("SYSTEM LOG: Error Received From search Request In search Method");
					System.out.println("Returning to Main Menu"); 
					return;
				}
			}
			//resp = LibraryClient.recvMessage(sbr);//new
			while(!back){
				System.out.println("Enter 1-4 To Select Your Search (Case-Sensitive):" +'\n' + "1) Author" + '\n' 
						+ "2) Title" + '\n' + "3) Keyword" + '\n' + "4) Back to Main Menu");
				String userChoice = br.readLine();

				switch(userChoice) {
				case "1":
					System.out.println("DEBUG: Entering authorSearch Method");
					authorSearch( s, sbr, spr, br);
					return;
				case "2":
					System.out.println("DEBUG: Entering titleSearch Method");
					titleSearch(s, sbr, spr, br);
					return;
				case "3":
					System.out.println("DEBUG: Entering keywordSearch Method");
					keywordSearch(s, sbr, spr, br);
					return;
				case "4":
					back = true;
					resp = recvMessage(sbr);
					return;
				default:
					System.out.println("Please Select a Valid Option(1-4). Note: Search is Case-Sensitive");
					break;
				}
			}


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return;
	}

	/**
	 * Send Borrow request to the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void borrow(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		Boolean tokenActive = false;
		tokenActive = isLoggedIn(sbr, spr);
		System.out.println(tokenActive);
		if(tokenActive){

			try{

				LibraryClient.sendMessage(spr, "borrow");
				String resp = LibraryClient.recvMessage(sbr);
				if(resp!=null){
					System.out.println("Got: "+resp);
					if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: Error Received From borrow Request In borrow Method");
						System.out.println("Returning to Main Menu"); 
						return;
					}
				}


				System.out.println("Please Enter The BookID Of Your Book Request(Only Five Books Can Be Out At Once!) :");
				String bookID = br.readLine();

				LibraryClient.sendMessage(spr, "borrow:String:"+bookID);
				resp = LibraryClient.recvMessage(sbr);
				if(resp!=null){
					System.out.println("Got: "+resp);
					if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: Error Received From bookID Request In borrow Method");
						System.out.println("Your bookList might be full. Please Limit to Five Borrows"); 
						System.out.println("Returning to Main Menu"); 
						return;
					}
					System.out.println("Here Is Your Request: "+resp);
					System.out.println("Please Enjoy Your Selection!");
				}
				return;	

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("Returning to the Main Menu");
			return;
		} 
	}

	/**
	 * Send Return book request to the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void returnBook(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){

		boolean isLoggedin = isLoggedIn(sbr, spr);

		if(isLoggedin){

			try{

				LibraryClient.sendMessage(spr, "returnBook");
				String resp = LibraryClient.recvMessage(sbr);
				if(resp!=null){
					System.out.println("Got: "+resp);
					if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: Error Received From returnBook Request In returnBook Method");
						System.out.println("Returning to Main Menu"); 
						return;
					}
				}

				System.out.println("Please Enter The BookID Of\nThe Book You Wish to Return :");
				String bookID = br.readLine();

				LibraryClient.sendMessage(spr, "returnBook:String:"+bookID);
				resp = LibraryClient.recvMessage(sbr);
				if(resp!=null){
					System.out.println(resp);
					if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: Error Received From bookID Request In returnBook Method");
						System.out.println("Returning to Main Menu"); 
						return;
					}
					if(resp.equals("OK")){
						System.out.println("Your Book Has Been Returned!\nPlease Search For Your Next Selection!");
					}
					//Just in case something else comes over the wire besides OK or ERR
					//System.out.println("Error Received, Exiting Program");
					//System.exit(0);
				}
				return;	

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//System.out.println("DEBUG: returnBook Not Entered!");


		} else if(!isLoggedin){
			System.out.println("Returning to the Main Menu");
			return;
		} else {
			System.out.println("SYSTEM LOG: Unknown Error Received, Exiting Program");
			System.exit(0);
		}
	}


	/**
	 * List currently checked out books from the server.
	 *
	 * @param s the s
	 * @param sbr the sbr
	 * @param spr the spr
	 * @param br the br
	 */
	public static void listCurrentBooks(Socket s, BufferedReader sbr, PrintWriter spr, BufferedReader br){


		//try{

		LibraryClient.sendMessage(spr, "listCurrentBooks");
		String resp = LibraryClient.recvMessage(sbr);
		if(resp!=null){
			System.out.println(resp);
			if(resp.equals("ERR")){
				System.out.println("You Do Not Currently Have Any Books Checked Out");
				System.out.println("Please Search For A Selection Or Borrow Once You Have Found One");
				System.out.println("Returning to Main Menu");
				return;
			}
			String[] respArray = resp.trim().split(",");
			System.out.println("Displaying Your Currently Checked Out Books:");
			for(int i = 0; i < respArray.length; i++){
				System.out.println("Result #"+i+": "+ respArray[i]);
			}
			System.out.println("Please Note The BookID For Your Borrow or Return");
		}


		return;	

		//System.out.println("DEBUG: listCurrentBooks Not Entered!");

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Boolean exit = false;


		try {
			Socket s = new Socket("localhost", 45000);

			BufferedReader sbr = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter spr = new PrintWriter(s.getOutputStream());

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			while(!exit){
				sendMessage(spr, "reset");
				String resp = recvMessage(sbr);
				System.out.println("Please Select 1-7 As An Option:");
				System.out.println("1) Login"
						+ '\n' + "2) Logout/exit" + '\n' + "3) Search (Case-Sensitive)" + '\n' + "4) Borrow" + '\n' + "5) Return"
						+ '\n' + "6) List Checked Out Books\nEnter a Number: ");
				String userChoice = br.readLine();
				//String resp;
				switch(userChoice) {
				case "1":
					login( s, sbr, spr, br);
					break;
				case "2":
					sendMessage(spr, "logout");
					logout(s, sbr, spr, br);
					exit = true;
					sbr.close();
					spr.close();
					br.close();
					s.close();
					System.exit(0);
				case "3":
					search(s, sbr, spr, br);
					
					break;
				case "4":
					sendMessage(spr, "borrow");
					resp = recvMessage(sbr);
					if(resp.equals("OK")){
						search(s, sbr, spr, br);
						borrow(s, sbr, spr, br);
					} else if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: ERROR RECEIVED");
					}
					break;
				case "5":
					sendMessage( spr, "return");
					resp = recvMessage(sbr);
					listCurrentBooks(s, sbr, spr, br);
					sendMessage(spr, "return");
					resp = recvMessage(sbr);
					System.out.println(resp);
					if(resp.equals("OK")){
						returnBook(s, sbr, spr, br);
					}  else if(resp.equals("ERR")){
						System.out.println("SYSTEM LOG: ERROR RECEIVED");
					}
					break;
				case "6":
					listCurrentBooks(s, sbr, spr, br);
					break; 
				default:
					System.out.println("Please Select A Valid Option(1-7)");
					break;
				}
			}


		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
