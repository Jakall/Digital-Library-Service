package projects;

import java.io.Serializable;

/**
 * @author j.callado
 */

/**
 * The Class Book.
 */
public class Book implements Serializable{

	/** The book id. */
	private String bookID;
	
	/** The author. */
	private String author;
	
	/** The title. */
	private String title;
	
	/** The language. */
	private String language;
	
	/** The date written. */
	private String dateWritten;
	
	/**
	 * Instantiates a new book.
	 *
	 * @param bookIDString the book id string
	 * @param tempBookAuthor the temp book author
	 * @param tempBookTitle the temp book title
	 * @param tempBookLanguage the temp book language
	 * @param tempBookWritten the temp book written
	 */
	public Book(String bookIDString, String tempBookAuthor,
			String tempBookTitle, String tempBookLanguage,
			String tempBookWritten){
		this.bookID = bookIDString;
		this.author = tempBookAuthor;
		this.title = tempBookTitle;
		this.language = tempBookLanguage;
		this.dateWritten = tempBookWritten.trim();
	}

	/**
	 * Gets the book id.
	 *
	 * @return the book id
	 */
	public String getBookID(){
		return bookID;
	}
	
	/**
	 * Gets the author.
	 *
	 * @return the author
	 */
	public String getAuthor(){
		return author;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage(){
		return language;
	}
	
	/**
	 * Gets the date written.
	 *
	 * @return the date written
	 */
	public String getDateWritten(){
		return dateWritten;
	}
	
	public void printElements(){
		
		System.out.println("bookID: " + bookID);
		System.out.println("Author: " + author);
		System.out.println("Title: " + title);
		System.out.println("Language: " + language);
		System.out.println("Date Written: " + dateWritten);
		
	}
	
}
