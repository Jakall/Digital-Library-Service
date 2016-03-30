# School-Projects

Digital Library Service

James Georgiades-Callado
========

Introduction
------------

The Digital Library Service aims to revolutionize and simplify how readers access their favorite classics. By providing a concurrent digital catalog, readers no longer have to visit a physical location to Search, Borrow, or Return their choices, but can do so anywhere there is access to the Internet. Simply, Login and browse the collection for anything that peeks your interest. Once the BookID has been located through Search, the book will be served digitally to you over the network upon a Borrow request. When finished, submit the book back to the Library to return it. 

Application Design
==================

Features
--------
- Concurrent TCP Server Model
	a) Allows for Multiple, Simultaneous User Accounts
- User Authentication (Login, Logout)
	a) Check for Authentication Token
- Search (Case-Sensitive)
	a) By Author
	b) By Title
	c) By Keyword (Incl. Language and Date Written)
- Borrow
	a) User Can Borrow At Most Five Books.
	b) Maintain A List of Currently Checked out Books
- Return
	a) Update List of Currently Checked out Books

Tools
-----

- Java 7
- Eclipse
- VirtualBox
- WireShark (for Testing Purposes)

Support
-------

If you are having issues, please let me know.
Our email is located at: j.callado (at) umiami.edu

