package PastExamples;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import projects.Book;


/**
 * @author j.callado
 */

/**
 * The Class LibraryServer.
 */
public class LibraryServer {

	/** The login token. */
	private static Boolean loginToken = false;
	
	/** The book list. */
	private static List<Book> bookList = new ArrayList<Book>();
	
	/** The checked out books. */
	private static List<Book> checkedOutBooks = new ArrayList<Book>();

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
	 * Login server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void loginServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		System.out.println("SERVERLOG: In Login Method");
		
		sendMessage(cpr, "OK");
		String req = recvMessage(cbr);
		if(req != null){
			System.out.println(req);
			String[] tokens = req.split(":");
			//String userID = tokens[2];
			sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");
		}

		//userID = null

		req = recvMessage(cbr);
		if(req != null){
			System.out.println(req);
			String[] tokens2 = req.split(":");
			System.out.println(tokens2[2]);
			//String password = tokens2[2];
			loginToken = true;
			sendMessage(cpr,"OK");
		} else {
			sendMessage(cpr, "ERR");

		}
		
		return;

		//System.out.println("SERVERLOG: DEBUG: loginServer Not Entered");

	}

	/**
	 * Checks if the user is logged in server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 * @return the boolean
	 */
	public static Boolean isLoggedInServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		String resp = recvMessage(cbr);
	
		if(resp != null){
		
		if(!loginToken){
			sendMessage(cpr, "ERR");
		} else if (loginToken){
			System.out.println("SERVERLOG: in isLoggedInServer Method");
			sendMessage(cpr, "OK");
		}
		}
		return loginToken;
		
	}

	/**
	 * Logout of the server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void logoutServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		loginToken = false;
		sendMessage(cpr,"OK");

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
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void authorSearchServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		System.out.println("SERVERLOG: In authorSearchServer Method");
		
		String foundID = null;
		String foundBookIDs = null;

		sendMessage(cpr,"OK");
		String req = recvMessage(cbr);
		String targetAuthorName = "";
		if(req != null){
			System.out.println(req);
			String[] tokens = req.split(":");
			targetAuthorName = tokens[2];
			System.out.println("SERVERLOG: In authorSearchServer Method, Received request to look for " + targetAuthorName );
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
			sendMessage(cpr, foundBookIDs);
		} else {
			sendMessage(cpr, "ERR");
			return;
		}

	}

	/**
	 * Title specific search of the book list.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void titleSearchServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		String foundID = null;
		String foundBookIDs = null;

		System.out.println("SERVERLOG: In titleSearchServer Method");
		
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
		}

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
		}

	}

	/**
	 * Keyword specific search of the book list.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void keywordSearchServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		String foundID = null;
		String foundBookIDs = null;
		
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
		}
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
		}

	}

	/**
	 * Pick specific search for the book list.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void searchServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		System.out.println("SERVERLOG: In searchServer method");
		
		sendMessage(cpr,"OK");
		String req = recvMessage(cbr);
		String searchChoice = null;
		if(req != null) {
			System.out.println(req);
			String[] tokens = req.split(":");
			searchChoice = tokens[2];
		} else {
			sendMessage(cpr, "ERR");
		}

		switch(searchChoice) {
		case "author":
			System.out.println("SERVERLOG: Entering authorSearchServer method");
			authorSearchServer( ss, cs, cbr, cpr);
			break;
		case "title":
			titleSearchServer(ss, cs, cbr, cpr);
			break;
		case "keyword":
			keywordSearchServer(ss, cs, cbr, cpr);
			break;
		default:
			break;
		}

		//System.out.println("SERVERLOG: DEBUG: loginServer Not Entered");

	}

	/**
	 * Check out the user requested book.
	 *
	 * @param book the book
	 */
	public static void checkOutBook(Book book){
		checkedOutBooks.add(book);

	}

	/**
	 * Perform Borrow on the server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void borrowServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		String resp = recvMessage(cbr);
		System.out.println(resp);
		sendMessage(cpr, "OK");

		String req = recvMessage(cbr);
		String targetBookID = "";
		if(req != null) {
			System.out.println(req);
			String[] tokens = req.split(":");
			targetBookID = tokens[2];
			Book book = bookIDSearchServer(targetBookID);
			checkOutBook(book);
			book.printElements();
			String bookTitle = book.getTitle();
			System.out.println(bookTitle);
			sendMessage(cpr, bookTitle);
		} else{
			sendMessage(cpr, "ERR");
		}
	}
	
	/**
	 * Check in the user requested book.
	 *
	 * @param book the book
	 */
	public static void checkInBook(Book book){
		
		checkedOutBooks.remove(book);
		System.out.println("SERVERLOG: Checking Back In: " + book.getBookID());
	}

	/**
	 * Return the book to the server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void returnBookServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

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
		}
		
	}

	/**
	 * List the currently checked out books from the server.
	 *
	 * @param ss the ss
	 * @param cs the cs
	 * @param cbr the cbr
	 * @param cpr the cpr
	 */
	public static void listCurrentBooksServer(ServerSocket ss, Socket cs, BufferedReader cbr, PrintWriter cpr){

		String booksOut = null;
		if(!checkedOutBooks.isEmpty()){
		for(Book book : checkedOutBooks){
			booksOut+= book.getTitle() + " - " + book.getBookID() + ",";
		}
		System.out.println("SERVERLOG: In listCurrentBook method");
		System.out.println(booksOut);
		sendMessage(cpr,booksOut);
		} else if(checkedOutBooks.isEmpty()){
			sendMessage(cpr, "ERR");
		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Boolean stop = false;



		Scanner bookFile;
		try {
			bookFile = new Scanner(new File("BookList.txt")).useDelimiter(",\\s*");


			int tempBookID = 0;
			String bookIDString;
			String tempBookAuthor;
			String tempBookTitle;
			String tempBookLanguage;
			String tempBookWritten;
			while(bookFile.hasNextLine()){
				if(tempBookID < 10){
					bookIDString = ("0" + tempBookID);
					tempBookTitle = bookFile.nextLine();
					//System.out.println("tempBookAuthor value: " +tempBookAuthor);
					tempBookAuthor = bookFile.nextLine();
					tempBookLanguage = bookFile.nextLine();
					tempBookWritten = bookFile.nextLine();
				} else {
					bookIDString = new Integer(tempBookID).toString();
					tempBookTitle = bookFile.nextLine();
					tempBookAuthor = bookFile.nextLine();
					tempBookLanguage = bookFile.nextLine();
					tempBookWritten = bookFile.nextLine();
				}
				Book book = new Book(bookIDString, tempBookAuthor, tempBookTitle, tempBookLanguage, tempBookWritten);
				bookList.add(book);
				tempBookID++;
			}
			int i = 1;
			for(Book book : bookList){
				System.out.println("Book #" + i);
				i++;
				book.printElements();
			}
			bookFile.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println("Starting Server");
			ServerSocket ss = new ServerSocket(9093, 0, InetAddress.getByName(null));//new ServerSocket(45000);

			Socket cs = ss.accept();

			System.out.println("Received connection from:"+cs.getInetAddress().getHostAddress()+":"+cs.getPort());

			BufferedReader cbr = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			PrintWriter cpr = new PrintWriter(cs.getOutputStream());


			while(!stop){
				String req = recvMessage(cbr);
				if(req == null){
					sendMessage(cpr, "ERR");
				}
				System.out.println(req);
				System.out.println("SERVERLOG: Checking Function Request");
				/*System.out.println("1) Login"
						+ '\n' + "2)Logout" + '\n' + "3) Search" + '\n' + "4) Borrow" + '\n' + "5) Return"
						+ '\n' + "6) List Checkedout Books" + '\n' + "7) Exit");*/


				switch(req) {
				case "login":
					System.out.println("SERVERLOG: Entering Login Method");
					loginServer( ss, cs, cbr, cpr);
					break;
				case "logout":
					if(isLoggedInServer(ss, cs, cbr, cpr)){
						logoutServer(ss, cs, cbr, cpr);
					}
					break;
				case "search":
					searchServer(ss, cs, cbr, cpr);
					break;
				case "borrow":
					sendMessage(cpr, "OK");
					req = recvMessage(cbr);
					searchServer(ss, cs, cbr, cpr);
					if(isLoggedInServer(ss, cs, cbr, cpr)){
						borrowServer(ss, cs, cbr, cpr);
					}
					break;
				case "return":
					sendMessage(cpr, "OK");
					req = recvMessage(cbr);
					listCurrentBooksServer(ss, cs, cbr, cpr);
					req = recvMessage(cbr);
					sendMessage(cpr, "OK");
					if(isLoggedInServer(ss, cs, cbr, cpr)){
					returnBookServer(ss, cs, cbr, cpr);
					}
					break;
				case "listCurrentBooks":
					listCurrentBooksServer(ss, cs, cbr, cpr);
					break;
				case "isLoggedIn"://Should never be called from this point of execution, will be checked before others
					isLoggedInServer(ss, cs, cbr, cpr);
				case "7": 
					stop = true;
					cbr.close();
					cpr.close();
					cs.close();
				default:
					//System.out.println("Please Select A Valid Option(1-7)");
					break;
				}
			}

			/*
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
			 */
			cs.close();	


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
