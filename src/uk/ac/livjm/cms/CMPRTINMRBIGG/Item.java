package uk.ac.livjm.cms.CMPRTINMRBIGG;

public class Item
{
//	Attributes:
// ***********************************************************************************************
	
//	From UML
// ----------------------------------
	private String description;
// ----------------------------------
	
//	Constructors:
// ***********************************************************************************************
	public Item(String description)
	{
		this.description = description;
	}

//	Added methods:
// ***********************************************************************************************
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
