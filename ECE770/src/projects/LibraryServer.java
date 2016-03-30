package projects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

// TODO: Auto-generated Javadoc
/**
 * The Class LibraryServer.
 *
 * @author j.callado
 */

/**
 * The Class LibraryServer
 */


public class LibraryServer {

	/** The book mastercatalog. */
	private static List<Book> bookCatalog = new ArrayList<Book>();
	
	/** The user list. */
	public static ConcurrentMap<String, User> userList = new ConcurrentHashMap<String, User>();
	
	/** The total number of users with accounts on the server */
	public static AtomicInteger totalUsers = new AtomicInteger(0);
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
				bookCatalog.add(book);
				tempBookID++;
			}
			int i = 1;
			for(Book book : bookCatalog){
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
			
			
			ServerSocket ss = new ServerSocket(45000);
			while (true) {
				System.out.println("Listening on...45000");
				Socket cs = ss.accept();
			
				System.out.println("Received connection from:"+cs.getInetAddress().getHostAddress()+":"+cs.getPort());
			
				LibraryAppServer app = new LibraryAppServer(cs, bookCatalog);
				
				// iterative
				//app.run();
				
				// concurrent
				System.out.println("Server Log: Creating new thread");
				Thread t = new Thread(app);
				t.start();
			
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
