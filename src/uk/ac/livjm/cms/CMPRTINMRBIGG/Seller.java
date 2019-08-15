package uk.ac.livjm.cms.CMPRTINMRBIGG;

import java.util.LinkedList;

public class Seller extends User implements Blockable
{
//	Attributes:
// ***********************************************************************************************
	private LinkedList<Auction> currentAuctions = new LinkedList<Auction>();
	
	private boolean isBlocked;

//	Constructors:
// ***********************************************************************************************
	public Seller(String username, String password)
	{
		super(username, password);
	}
	
//	Methods (From UML):
// ***********************************************************************************************
	public boolean isBlocked()
	{
		return this.isBlocked;
	}

	public void setBlocked(boolean isBlocked)
	{
		this.isBlocked = isBlocked;
	}
	
//	Added methods:
// ***********************************************************************************************
	public LinkedList<Auction> getCurrentAuctions()
	{
		return currentAuctions;
	}
	
	public void addAuction(Auction newAuction)
	{
		this.currentAuctions.add(newAuction);
	}
}
