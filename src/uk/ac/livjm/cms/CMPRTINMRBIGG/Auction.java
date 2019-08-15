package uk.ac.livjm.cms.CMPRTINMRBIGG;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Auction implements Blockable, Runnable
{
//	Attributes:
// ***********************************************************************************************
	private LinkedList<Bid> bids = new LinkedList<Bid>();

//	From UML
// ----------------------------------
	private double startPrice;
	private double reservePrice;
	private LocalDateTime closeDate;
	private char status;
// ----------------------------------

	private Item item;
	private boolean isBlocked;
	private boolean isSold;
	private static double upperIncrement;
	private static double lowerIncrement;
	
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

//	Constructors:
// ***********************************************************************************************
	public Auction(Item item, double startPrice, double reservePrice, LocalDateTime closingDate, char status)
	{				
		this.item = item;
		this.startPrice = startPrice;
		this.reservePrice = reservePrice;
		this.closeDate = closingDate;
		this.status = status;		
		this.isBlocked = false;
		this.isSold = false;
				
		upperIncrement = (startPrice * 0.20);
		lowerIncrement = (startPrice * 0.10);
	}

//	Methods (From UML):
// ***********************************************************************************************
	public void placeBid(double amount, Buyer who, LocalDateTime when)
	{
		double currentHighestBid = getHighestBid();
		
		if (amount < (currentHighestBid + lowerIncrement) || amount > (currentHighestBid + getUpperIncrement()))
			System.out.println("Error! Bid not within allowed increment");
		else
		{
			bids.addFirst(new Bid(amount, who, when));
			System.out.println(who.getUsername() + " has made a bid of " + "£" + amount + " on the item " + "\"" + getDescription() + "\" - " + dateFormatter.format(when) + "\n");
		}
	}

	public synchronized void verify()
	{
		this.status = 'A';
		System.out.println("\nAuction Successfully verified.");
	}

	public void close()
	{
		// Check if the reserve price is met...
		if (!this.bids.isEmpty())
		{
			if ((getHighestBid()) >= (this.reservePrice))
			{
				
				isSold = true;
				
				System.out.println("\nAuction: " + "\"" + getDescription() + "\"" + " sold! The auction is now closed.");
			}
			else
				System.out.println("\nAuction: " + "\"" + getDescription() + "\"" + " not sold. The auction is now closed.");
		}
		else
			System.out.println("\nAuction: " + "\"" + getDescription() + "\"" + " not sold. The auction is now closed.");
		
		this.status = 'C';
	}
	
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
	public void run()
	{
		while (this.status != 'C')
		{
			try
			{
				// Check if closing date has been reached every second:
				Thread.sleep(1000);
					
				// If it has, close the auction and terminate this thread:
				if (LocalDateTime.now().isAfter(this.closeDate) || getHighestBid() >= (this.reservePrice))
				{
					close();
					break;
				}
			}
			catch (Exception E)
			{
				E.printStackTrace();
			}
		}
	}
	
	public String auctionStatus()
	{
		String auctionStatus = null;
		
		if(isBlocked() == true)
			auctionStatus = "Blocked!";
		else if(this.status == 'A')
			auctionStatus = "Active";
		else if(this.status == 'P')
			auctionStatus = "Pending";
		else if(this.status == 'C')
			auctionStatus = "Closed";
		else
			auctionStatus = "Error! Cannot return auction status";
		
		return auctionStatus;
	}
	
	public String displayAuction()
	{
		String displayAuction = "\n************************************************" +
								"\nItem Description: " + getDescription() +
								"\nCurrent Bid: " + "£" + getHighestBid() +
								"\nClosing Date: " + this.dateFormatter.format(closeDate) +
								"\nAuction Status: " + this.auctionStatus() +
								"\n************************************************\n";
		
		return displayAuction;
	}
	
	public boolean getIsSold()
	{
		return isSold;
	}
	
	public String getDescription()
	{
		return item.getDescription();
	}

	public double getHighestBid()
	{
		if (this.bids.isEmpty())
			return this.startPrice;
		else
			return this.bids.getFirst().getAmount();
	}
	
	public Buyer getWinner()
	{
		if (this.bids.isEmpty())
			return null;
		else
			return (Buyer)this.bids.getFirst().getName();
	}

	public char getStatus()
	{
		return status;
	}
	
	public void setStatus(char status)
	{
		this.status = status;
	}

	public static double getUpperIncrement()
	{
		return upperIncrement;
	}
	
	public static double getLowerIncrement()
	{
		return lowerIncrement;
	}

}
