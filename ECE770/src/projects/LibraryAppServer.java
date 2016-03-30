package projects;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * The Class LibraryAppServer.
 */

/**
 * @author j.callado
 */

public class LibraryAppServer extends AppServer {

	/** The login token. */
	private static User user = null;

	/** The book list. */
	private static List<Book> bookList = new ArrayList<Book>();

	/** The cs. */
	private Socket cs;

	/**
	 * Instantiates a new library app server.
	 *
	 * @param s the s
	 * @param bookCatalog the book catalog
	 */
	public LibraryAppServer(Socket s, List<Book> bookCatalog) {
		cs = s;
		LibraryAppServer.bookList = bookCatalog;

	}

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

	/**
	 * New user creation.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void newUserCreation(BufferedReader cbr, PrintWriter cpr){

		int newUserID = LibraryServer.totalUsers.getAndIncrement();
		String newPassword = null;
		String newUserName = null;


		sendMessage(cpr, "OK");
		String req = recvMessage(cbr);//Receive new userName
		if(req != null){
			//System.out.println(req);
			String[] tokens = req.split(":");
			newUserName = tokens[2];
			sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

		req = recvMessage(cbr);
		if(req != null){
			//System.out.println(req);
			String[] tokens2 = req.split(":");
			//System.out.println(tokens2[2]);
			newPassword = tokens2[2];
			sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;

		}
		user = new User(newUserID, newUserName,newPassword);
		synchronized (user) {
			LibraryServer.userList.putIfAbsent(user.getUserName(), user);
			user = LibraryServer.userList.get(user.getUserName());
		}

		
		return;
	}

	/**
	 * Returning user login.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void returningUserLogin(BufferedReader cbr, PrintWriter cpr){

		String nameField = null;
		String passwordField = null;
		User userLookup =null;




		sendMessage(cpr, "OK");
		String req = recvMessage(cbr);//Receive  Returning Username
		if(req != null){
			//System.out.println(req);
			String[] tokens = req.split(":");
			nameField = tokens[2];
			sendMessage(cpr,"OK");
				userLookup = LibraryServer.userList.get(nameField);
		} else {
			sendMessage(cpr, "ERR");
			return;
		}


		req = recvMessage(cbr); // recieve password
		if(req != null){
			//System.out.println(req);
			String[] tokens2 = req.split(":");
			System.out.println(tokens2[2]);
			passwordField = tokens2[2];
			if((passwordField.equals(userLookup.getPassword())) && (!userLookup.checkLoginToken())){
				synchronized (user) {
					user = userLookup;
				}
				user.setLoginToken(true);
				sendMessage(cpr,"OK");
			}
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

		return;
	}

	/**
	 * Login server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void loginServer(BufferedReader cbr, PrintWriter cpr){

		System.out.println("SERVERLOG: In Login Method");
		String req = null;


			sendMessage(cpr, "OK");
			req = recvMessage(cbr);//Receive New User or Returning User
			if(req != null){
				//System.out.println(req);
				//String[] tokens = req.split(":");
				//String userID = tokens[2];
				sendMessage(cpr,"OK");
			} else {
				sendMessage(cpr, "ERR");
				return;
			}



		switch(req) {
		case "newUser":
			System.out.println("SERVERLOG: Entering newUserCreation method");
			newUserCreation( cbr, cpr);
			return;
		case "returningUser":
			returningUserLogin(cbr, cpr);
			return;
		default:
			break;
		}


		return;

		//System.out.println("SERVERLOG: DEBUG: loginServer Not Entered");

	}

	/**
	 * Checks if the user is logged in server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 * @return the boolean
	 */
	public static boolean isLoggedInServer( BufferedReader cbr, PrintWriter cpr){

		String resp = recvMessage(cbr);
		boolean tempToken;
		synchronized (user) {
			tempToken = user.checkLoginToken();
		}
		System.out.println(tempToken);
		if(resp != null){
			if(!tempToken){
				sendMessage(cpr, "ERR");
			} else if (tempToken){
				System.out.println("SERVERLOG: in isLoggedInServer Method");
				sendMessage(cpr, "OK");
			}
		}
		return tempToken;

	}

	/**
	 * Logout of the server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public void logoutServer(BufferedReader cbr, PrintWriter cpr){
		
		if(user != null){
		synchronized (user) {
			user.setLoginToken(false); 
			LibraryServer.userList.replace(user.getUserName(), LibraryAppServer.user);
			}
			sendMessage(cpr, "OK");
			return;
		}else{
			sendMessage(cpr, "ERR");
			user.setLoginToken(false); 
			return;
		}

		//System.out.println("SERVERLOG: DEBUG: loginServer Not Entered");

	}

	/**
	 * Get Book id from search and then provide user book from server.
	 *
	 * @param targetBookIDString the target book id string
	 * @return the book
	 */
	public static Book bookIDSearchServer(String targetBookIDString){

		for(Book book : bookList){
			if(book.getBookID().equals(targetBookIDString)){
				return book;
			}
		}

		return null;
	}


	/**
	 * Author specific search of book list.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void authorSearchServer(BufferedReader cbr, PrintWriter cpr){

		//System.out.println("SERVERLOG: In authorSearchServer Method");

		String foundID = "";
		String foundBookIDs = "";

		sendMessage(cpr,"OK");//comment this out to fix
		String req = recvMessage(cbr);
		String targetAuthorName = "";
		if(req != null){
			//System.out.println(req);
			String[] tokens = req.split(":");
			targetAuthorName = tokens[2];
			//System.out.println("SERVERLOG: In authorSearchServer Method, Received request to look for " + targetAuthorName );
			//sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;
		}
		 req = recvMessage(cbr);
		//String token = "";
		if(req != null){
			//System.out.println(req);
			//String[] tokens = req.split(":");
			//token = tokens[2];
			//System.out.println("SERVERLOG: In authorSearchServer Method, Received request to look for " + targetAuthorName );
			//sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;
		}
		

		int i = 0;
		for(Book book : bookList){
			String author = book.getAuthor();
			//System.out.println("SERVERLOG: in authorSearchServer Method, current author: "+ author);
			if(author.equals(targetAuthorName)){
				foundID = book.getBookID();
				System.out.println("SERVERLOG: In authorSearchServer Method, found bookID: " + foundID);				
				i++;
				if(i <= 1){
					foundBookIDs = foundID;

				} else if (i >= 2){
					foundBookIDs+= "," + foundID;
				}
			}
		}

		System.out.println(foundBookIDs);
		if(foundBookIDs != null){
			System.out.println("Sending: " + foundBookIDs);
			sendMessage(cpr, foundBookIDs);
			return;
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

	}

	/**
	 * Title specific search of the book list.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void titleSearchServer(BufferedReader cbr, PrintWriter cpr){

		String foundID = "";
		String foundBookIDs = "";

		//System.out.println("SERVERLOG: In titleSearchServer Method");

		sendMessage(cpr, "OK");//TESTING THIS NOW
		String req = recvMessage(cbr);
		String targetTitle = "";
		if(req != null){
			System.out.println(req);
			String[] tokens = req.split(":");
			targetTitle = tokens[2];
			//sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;
		}
		
		 /*req = recvMessage(cbr);
			if(req != null){
				System.out.println(req);
				sendMessage(cpr,"OK");
			} else {
				sendMessage(cpr, "ERR");
				return;
			} */
			
		int i = 0;
		for(Book book : bookList){
			String title = book.getTitle();
			//System.out.println("SERVERLOG: in authorSearchServer Method, current author: "+ author);
			if(title.equals(targetTitle)){
				foundID = book.getBookID();
				System.out.println("SERVERLOG: In titleSearchServer Method, found bookID: " + foundID);				
				i++;
				if(i <= 1){
					foundBookIDs = foundID;
					System.out.println("SERVERLOG: In titleSearchServer Method, found 1 book");

				} else if (i >= 2){
					foundBookIDs+= "," + foundID;
				}
			}
		}
		System.out.println(foundBookIDs);
		if(foundBookIDs != null){
			sendMessage(cpr, foundBookIDs);
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

	}

	/**
	 * Keyword specific search of the book list.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void keywordSearchServer( BufferedReader cbr, PrintWriter cpr){

		String foundID = "";
		String foundBookIDs = "";

		System.out.println("SERVERLOG: In keywordSearchServer Method");

		sendMessage(cpr, "OK");
		String req = recvMessage(cbr);
		String targetKeyword = "";
		if(req != null){
			System.out.println(req);
			String[] tokens = req.split(":");
			targetKeyword = tokens[2];
			//sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
			return;
		}
		/*
		 req = recvMessage(cbr);
			if(req != null){
				System.out.println(req);
				sendMessage(cpr,"OK");
			} else {
				sendMessage(cpr, "ERR");
				return;
			} */
		System.out.println("SERVERLOG: Searhing for " + targetKeyword);
		int i =0;
		for(Book book : bookList){
			System.out.println(book.getDateWritten());
			if(((book.getTitle()).toLowerCase()).contains(targetKeyword.toLowerCase()) || 
					((book.getAuthor()).toLowerCase()).contains(targetKeyword.toLowerCase()) ||
					((book.getLanguage()).toLowerCase()).contains(targetKeyword.toLowerCase()) ||
					((book.getDateWritten()).equals(targetKeyword))){
				foundID = book.getBookID();
				System.out.println("SERVERLOG: Middle of keywordSearchServer Method, found bookID: " + foundID);				
				i++;
				if(i <= 1 ){
					foundBookIDs = foundID;

				} else if (i >= 2  ){
					foundBookIDs+= "," + foundID;
				}
			}
		}


		if(foundBookIDs != null){
			System.out.println("SERVERLOG: Sending " +foundBookIDs);
			sendMessage(cpr, foundBookIDs);
		} else {
			System.out.println("SERVERLOG: Did Not Find Anything");
			sendMessage(cpr, "ERR");
			return;
		}

	}

	/**
	 * Pick specific search for the book list.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void searchServer(BufferedReader cbr, PrintWriter cpr){

		//System.out.println("SERVERLOG: In searchServer method");

		
		String req = recvMessage(cbr);
		sendMessage(cpr, "OK");
		req = recvMessage(cbr);
		String searchChoice = null;
		if(req != null) {
			//System.out.println(req);
			String[] tokens = req.split(":");
			searchChoice = tokens[2];
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

		switch(searchChoice) {
		case "author":
			//System.out.println("SERVERLOG: Entering authorSearchServer method");
			authorSearchServer( cbr, cpr);
			return;
		case "title":
			titleSearchServer( cbr, cpr);
			return;
		case "keyword":
			keywordSearchServer( cbr, cpr);
			return;
		default:
			sendMessage(cpr, "ERR");
			return;
		}

		//System.out.println("SERVERLOG: DEBUG: loginServer Not Entered");

	}

	/**
	 * Check out the user requested book.
	 *
	 * @param book the book
	 */
	public static boolean checkOutBook(Book book){
		List<Book> tempUserBookList = new ArrayList<Book>();
		synchronized (user) {
			tempUserBookList = user.getCheckedOutBooks();
			if(tempUserBookList.size() <= 5){
			user.checkOutBook(book);
			return true;
		} else {
			return false;
		}
		}
	}

	/**
	 * Perform Borrow on the server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void borrowServer(BufferedReader cbr, PrintWriter cpr){

		boolean userListFull = false;
		
		String resp = recvMessage(cbr);
		System.out.println(resp);
		sendMessage(cpr, "OK");

		String req = recvMessage(cbr);
		if(req.equals("borrow")) {
		 sendMessage(cpr, "OK");
		 req = recvMessage(cbr);
		} 
		String targetBookID = "";
		if(req != null) {
			System.out.println(req);
			String[] tokens = req.split(":");
			if(tokens == null){
				sendMessage(cpr, "ERR");
				return;
			}
			targetBookID = tokens[2];
			if(targetBookID == null){
				sendMessage(cpr, "ERR");
				return;
			}
			Book book = bookIDSearchServer(targetBookID);
			userListFull = checkOutBook(book);
			if(userListFull){
			book.printElements();
			String bookTitle = book.getTitle();
			System.out.println(bookTitle);
			sendMessage(cpr, bookTitle);
			} else {
				sendMessage(cpr, "ERR");
				return;
			}

		}
		}

	

	/**
	 * Check in the user requested book.
	 *
	 * @param book the book
	 */
	public static void checkInBook(Book book){

		synchronized (user) {
			user.checkInBook(book);
		}
		System.out.println("SERVERLOG: Checking Back In: " + book.getBookID());
	}

	/**
	 * Return the book to the server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void returnBookServer(BufferedReader cbr, PrintWriter cpr){

		String resp = recvMessage(cbr);
		sendMessage(cpr, "OK");
		String req = recvMessage(cbr);
		String targetBookID = "";
		if(req != null) {
			System.out.println(req);
			sendMessage(cpr, "OK");
			String[] tokens = req.split(":");
			targetBookID = tokens[2];
			Book book = bookIDSearchServer(targetBookID);
			checkInBook(book);
			//sendMessage(cpr, "OK");
		} else{
			sendMessage(cpr, "ERR");
			return;
		}

	}

	/**
	 * List the currently checked out books from the server.
	 *
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void listCurrentBooksServer( BufferedReader cbr, PrintWriter cpr){

		String booksOut = "";
		List<Book> tempUserBookList;
		synchronized (user) {
			tempUserBookList = user.getCheckedOutBooks();
		}
		if(!tempUserBookList.isEmpty()){
			for(Book book : tempUserBookList){
				booksOut+= book.getTitle() + " - " + book.getBookID() + ",";
			}	
			System.out.println("SERVERLOG: In listCurrentBook method");
			System.out.println(booksOut);
			sendMessage(cpr,booksOut);
		} else if(tempUserBookList.isEmpty()){
			sendMessage(cpr, "ERR");
			return;
		}


	}

	/* (non-Javadoc)
	 * @see AppServer#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

		boolean stop = false;
		boolean resetRecv = false;

		try {

			BufferedReader cbr = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			PrintWriter cpr = new PrintWriter(cs.getOutputStream());


			while(!stop){
				String req = recvMessage(cbr);
				while(!resetRecv){
					if(req == null){
						sendMessage(cpr, "ERR");
					}
					if(req.equals("reset")){
					//System.out.println(req);
					sendMessage(cpr, "OK");
					resetRecv = true;
				}
					req = recvMessage(cbr);
				}
				//req = recvMessage(cbr);
				//System.out.println(req);
				//System.out.println("SERVERLOG: Checking Function Request");
				/*System.out.println("1) Login"
						+ '\n' + "2)Logout" + '\n' + "3) Search" + '\n' + "4) Borrow" + '\n' + "5) Return"
						+ '\n' + "6) List Checkedout Books" + '\n' + "7) Exit");*/


				switch(req) {
				case "login":
					System.out.println("SERVERLOG: Entering Login Method");
					loginServer(cbr, cpr);
					break;
				case "logout":
					if(isLoggedInServer(cbr, cpr)){
						logoutServer(cbr, cpr);
						cbr.close();
						cpr.close();
						cs.close();	
						stop = true;
					}
					break;
				case "search":
					//sendMessage(cpr, "OK");
					//req = recvMessage(cbr);
					//sendMessage(cpr, "OK");
					searchServer(cbr, cpr);
					break;
				case "borrow":
					sendMessage(cpr, "OK");
					//req = recvMessage(cbr);
					searchServer(cbr, cpr);
					if(isLoggedInServer(cbr, cpr)){
						borrowServer(cbr, cpr);
					}
					break;
				case "return":
					sendMessage(cpr, "OK");
					req = recvMessage(cbr);
					listCurrentBooksServer(cbr, cpr);
					req = recvMessage(cbr);
					sendMessage(cpr, "OK");
					if(isLoggedInServer( cbr, cpr)){
						returnBookServer(cbr, cpr);
					}
					break;
				case "listCurrentBooks":
					listCurrentBooksServer( cbr, cpr);
					break;
				case "isLoggedIn"://Should never be called from this point of execution, will be checked before others
					isLoggedInServer( cbr, cpr);
				case "7": 
					stop = true;
					cbr.close();
					cpr.close();
					cs.close();
				default:
					//System.out.println("Please Select A Valid Option(1-7)");
					break;
				}
				resetRecv = false;
			}

			//cbr.close();
			//cpr.close();
			//cs.close();	


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

