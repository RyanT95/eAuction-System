package uk.ac.livjm.cms.CMPRTINMRBIGG;

public abstract class User
{
//	Attributes:
// ***********************************************************************************************
	
//	From UML
// ----------------------------------
	protected String username;
	protected String password;
// ----------------------------------

//	Constructors:
// ***********************************************************************************************
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
//	Methods (From UML):
// ***********************************************************************************************
	public boolean checkPassword(String password)
	{
		if (password.equals(this.password))
			return true;
		
		return false;
	}
	
//	Added methods:
// ***********************************************************************************************
	public String getUsername()
	{
		return this.username;
	}
}
