package projects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The Class User.
 *
 * @author j.callado
 */
public class User implements Serializable{

	/** The user id. */
	private int userID;
	
	/** The user name. */
	private String userName;
	
	/** The password. */
	private String password;
	
	/** The login token. */
	private boolean loginToken;
	
	/** The checked out books. */
	private List<Book> checkedOutBooks = new ArrayList<Book>();
	
	/**
	 * Instantiates a new user.
	 *
	 * @param userID the user id
	 * @param userName the user name
	 * @param password the password
	 */
	public User(int userID, String userName,
			String password){
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.loginToken = true;
		checkedOutBooks.clear();
	}

	/**
	 * Gets the book id.
	 *
	 * @return the book id
	 */
	public int getUserID(){
		return userID;
	}
	
	/**
	 * Gets the userName.
	 *
	 * @return the userName
	 */
	public String getUserName(){
		return userName;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public boolean checkLoginToken(){
		return loginToken;
	}
	
	
	/**
	 * Check out book.
	 *
	 * @param book the book
	 */
	public void checkOutBook(Book book){
		this.checkedOutBooks.add(book);
	}
	
	/**
	 * Check in book.
	 *
	 * @param book the book
	 */
	public void checkInBook(Book book){
		this.checkedOutBooks.remove(book);
	}
	
	/**
	 * Gets the checked out books.
	 *
	 * @return the checked out books
	 */
	public List<Book> getCheckedOutBooks(){
		return checkedOutBooks;
	}
	
	/**
	 * Sets the login token.
	 *
	 * @param action the new login token
	 */
	public void setLoginToken(boolean action){
		this.loginToken = action;
	}
	
	
	/**
	 * Prints the elements.
	 */
	public void printElements(){
		
		System.out.println("userID: " + userID);
		System.out.println("userName: " + userName);
		System.out.println("loginToken: " + loginToken);

		
	}
	
}

