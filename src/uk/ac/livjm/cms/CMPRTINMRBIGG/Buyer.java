package uk.ac.livjm.cms.CMPRTINMRBIGG;

public class Buyer extends User
{		
//	Constructors:
// ***********************************************************************************************
	public Buyer(String username, String password)
	{
		super(username, password);
	}
	
//	Methods (From UML):
// ***********************************************************************************************
	public void victory()
	{
		System.out.println("You have won the auction!");
	}
}
