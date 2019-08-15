package uk.ac.livjm.cms.CMPRTINMRBIGG;

import java.time.LocalDateTime;

public class Bid
{
//	Attributes:
// ***********************************************************************************************
	
//	From UML
// ----------------------------------
	private double amount;
	private Buyer who;
	private LocalDateTime when;
// ----------------------------------

//	Constructors:
// ***********************************************************************************************
	public Bid(double amount, Buyer who, LocalDateTime when)
	{
		this.amount = amount;
		this.who = who;
		this.when = when;
	}

	
//	Added methods:
// ***********************************************************************************************
	public double getAmount()
	{
		return this.amount;
	}

	public User getName()
	{
		return who;
	}

	public LocalDateTime getDate()
	{
		return when;
	}
}
