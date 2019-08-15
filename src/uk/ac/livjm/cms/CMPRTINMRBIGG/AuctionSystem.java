package uk.ac.livjm.cms.CMPRTINMRBIGG;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class AuctionSystem
{
//	Attributes:
// ***********************************************************************************************
	private static LinkedList<User> users = new LinkedList<User>();
	private static LinkedList<Auction> auctions = new LinkedList<Auction>();
	
	private static boolean isLoggedIn = false;
	private static boolean exit = false;
	
	private static User loggedIn;
	
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private static Scanner S = new Scanner(System.in);
	
//	Constructors:
// ***********************************************************************************************
	public AuctionSystem()
	{
// Test data:
// ========================================================================================
		
		// Users:
		// ------------------------------------------------------
		Buyer user1 = new Buyer("RyanT", "password");
		Buyer user2 = new Buyer("RyanB", "password");
		Seller user3 = new Seller("User3", "password");
		Seller user4 = new Seller("User4", "password");
		
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		// ------------------------------------------------------

		// Auctions:
		// ------------------------------------------------------
		Auction auction1 = new Auction(new Item("Queen Elizabeth II's Hair"), 10.15, 15.00, LocalDateTime.parse("26/03/2017 10:34", dateFormatter), 'A');
		
		Auction auction2 = new Auction(new Item("Do Androids Dream of Electric Sheep? Philip K. Dick"), 2.51, 5.00, LocalDateTime.parse("10/03/2017 19:00", dateFormatter), 'A');

		Auction auction3 = new Auction(new Item("1972 Datsun 100A"), 3800.86, 3842, LocalDateTime.parse("24/03/2017 15:00", dateFormatter), 'A');
		
		Auction auction4 = new Auction(new Item("Commodore 64 (Boxed, Like-new)"), 45.50, 50.00, LocalDateTime.parse("29/03/2017 10:34", dateFormatter), 'A');
		
		user3.addAuction(auction1);
		user4.addAuction(auction2);
		user3.addAuction(auction3);
		user4.addAuction(auction4);
		
		auctions.add(auction1);
		auctions.add(auction2);
		auctions.add(auction3);
		auctions.add(auction4);
		
//		Each auction has its own thread so it can run in the background without interrupting the menu system
		Thread auction1Thread = new Thread(auction1);
		Thread auction2Thread = new Thread(auction2);
		Thread auction3Thread = new Thread(auction3);
		Thread auction4Thread = new Thread(auction4);
		
		auction1Thread.start();
		auction2Thread.start();
		auction3Thread.start();
		auction4Thread.start();
		
		auction1.setBlocked(true);		//	Blocking auction 1 for demonstration purposes
		
//		Automated bids
//		=======================================================
		auction3.placeBid(3806.51, user1, LocalDateTime.now());		//	User 1 bidding on Auction 3
		
		auction3.placeBid(3814.99, user2, LocalDateTime.now());		//	User 2 bidding on Auction 3
		
		auction4.placeBid(52.99, user1, LocalDateTime.now());		//	User 1 bidding on Auction 4
//		========================================================

		// ------------------------------------------------------
		
// ========================================================================================
	}
	
//	Main menu system:
	void execute()
	{
		do
		{
			while (isLoggedIn == false)
			{
				System.out.print("*************************************************************************\n*			eAuction System					*\n*************************************************************************\n");
				
				System.out.print("1 : Browse Avaliable Auctions						-\n");
				System.out.print("2 : Setup account							-\n");
				System.out.print("3 : Log in to existing account						-\n");
				System.out.print("4 : Exit								-\n"); 
				System.out.print("-------------------------------------------------------------------------\n\n");
				System.out.print("Option : ");
					
				try
				{
					switch (S.next())
					{
						case "1":
							browseAuction();
							break;
							
						case "2":
							setupAccount();
							continue;
							
						case "3":
							if (logIn() == true)
								accountMenu();
							break;
									
						case "4":
							System.out.println("Exiting Application...");
							S.close();
								
							System.out.println("Goodbye.");
							System.exit(0);
							
						default:
							throw new InputMismatchException();
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Error! Invalid input.\n");
					S.next();
					continue;
				}
			}
		}
		while (exit == false);		
	}
	
//	User menu:
	public void accountMenu()
	{
		do
		{
			System.out.print("-------------------------------------------------------------------------");
			System.out.print("\nLogged in user: " + loggedIn.getUsername() + "							-");
			System.out.print("\nAccount: ");
			
			if (loggedIn instanceof Buyer)
				System.out.print("Buyer								-\n");
			else
				System.out.print("Seller								-\n");
			
			System.out.print("-------------------------------------------------------------------------\n");
			System.out.print("-------------------------------------------------------------------------\n");
			
			if (loggedIn instanceof Buyer)
			{
				System.out.print("1 : Browse auctions							-\n");
				System.out.print("2 : View won auctions							-\n");
			}

			else
			{
				System.out.print("1 : Set up new auction							-\n");
				System.out.print("2 : View sold auctions							-\n");
			}
			
			System.out.print("3 : Logout								-\n");
			System.out.print("-------------------------------------------------------------------------\n\n");
			System.out.print("Option : ");
				
			try
			{
				switch (S.next())
				{
					case "1":
						if (loggedIn instanceof Buyer)
							browseAuction();
						else
							placeAuction();
						break;
					
					case "2":
						if (loggedIn instanceof Buyer)
							viewWonAuctions();
						else
							viewSoldAuctions();
						break;
					
					case "3":
						loggedIn = null;
						isLoggedIn = false;
						System.out.println("\nUser logged out. Returning to main menu.\n");
						break;
					
					default:
						throw new InputMismatchException();
				}
			}
			catch (InputMismatchException e)
			{
				System.out.println("Error! Invalid input.\n");
				S.next();
				continue;
			}
		}
		while (isLoggedIn == true);
		
		execute();
	}
	
//	Methods (From UML):
//	***********************************************************************************************
//	Create new auction:
	public void placeAuction()
	{
		Auction newAuction;
		boolean verified = false;
		String description = null;
		double startPrice = 0.0;
		double reservePrice = 0.0;
		String closingDate = null;
		LocalDateTime parsedClosingDate = null;
		
		boolean descriptionComplete = false;
		boolean pricingComplete = false;
		boolean closingDateComplete = false;
		
		S.nextLine();
		
		System.out.print("*************************************************************************\n*			Place Auction					*\n*************************************************************************\n");
		
		System.out.print("Please enter the appropriate details for your auction when prompted	-\n"); 
		System.out.print("-------------------------------------------------------------------------\n\n"); 
		
		while (descriptionComplete == false)	//	Input item description
		{
			try
			{
				System.out.print("\nPlease enter an appopriate description for your item: \n");
				description = S.nextLine();
				descriptionComplete = true;
			}
			catch (InputMismatchException e)
			{
				System.out.print("\nError! Invalid input.");
				continue;
			}
		}
			
		while (pricingComplete == false)	//	Input start and reserve price
		{
			try
			{
				System.out.print("\nPlease enter the starting price for your item: \n");
				startPrice = S.nextDouble();
				
				System.out.print("\nPlease enter the reserve price for your item: \n");
				reservePrice = S.nextDouble();
				
				if (reservePrice < startPrice)
				{
					System.out.println("\nError! Please enter a reserve price that is higher than the starting price.");				
					continue;
				}
				
				pricingComplete = true;
			}
			catch (InputMismatchException e)
			{
				System.out.print("\nError! Invalid input.");
		
				S.next();
				continue;
			}
		}
			
		S.nextLine();
			
		while (closingDateComplete == false)	//	Input closing date
		{
			try
			{
				System.out.println("\nPlease enter the closing date for your item. The date must be within 7 days of today and entered in DD/MM/YYYY HH:MM format: ");
				closingDate = S.nextLine();
					
				parsedClosingDate = LocalDateTime.parse(closingDate, dateFormatter);
				
				// Check if closing date time is in the past:
				if (parsedClosingDate.isBefore(LocalDateTime.now()))	//	Check if closing date is in the past
				{
					System.out.println("\nError! Please enter a date and time that is not in the past.");				
					continue;
				}
							
				// Enforce maximum of 7 days from current date:
				if (parsedClosingDate.isAfter(LocalDateTime.now().plusDays(7)))	//	Check if closing date is within 7 days of the current date
				{
					System.out.println("\nError! The closing date is not within 7 days of the specified limit.\nPlease try again.");
					continue;
				}
							
				closingDateComplete = true;
			}
			catch (InputMismatchException | DateTimeParseException e)
			{
				System.out.print("\nInvalid input\nPlease try again.");
				continue;
			}
		}
			
		newAuction = new Auction(new Item(description), startPrice, reservePrice, parsedClosingDate, 'P');
		
//		Verify auction:
		do
		{				
			System.out.print("\nAuction Details");
			System.out.print("\n************************************************");
			System.out.print("\nItem Description : " + description);
			System.out.print("\nStarting Price : " + "£" + startPrice);
			System.out.print("\nReserve Price : " + "£" + reservePrice);
			System.out.print("\nClosing Date : " + closingDate); 
			System.out.print("\n************************************************\n\n");
			System.out.print("Please select an option:");
			System.out.print("\n1 : Start auction");
			System.out.print("\n2 : Cancel and return to main menu\n");
		
			try
			{
				switch (S.next())
				{
					case "1":
						newAuction.verify();
						verified = true;
								
						// Add auction linked lists
						auctions.add(newAuction);
						Seller.class.cast(loggedIn).addAuction(newAuction);
								
						Thread auctionThread = new Thread(newAuction);
						auctionThread.start();
									
						System.out.println("\nThe auction has been started.");
						return;
								
					case "2":
						return;
									
					default:
						System.out.print("\nInvalid input\nPlease try again.");
						continue;
				}
			}
			catch (InputMismatchException e)
			{
				System.out.print("\nInvalid input\nPlease try again.");
				continue;
			}
		}
		while(verified == false);
	}

//	Browse the available auctions:
	public static void browseAuction()
	{
		boolean browsing = true;
		boolean auctionSelected = false;
		int totalActiveAuctions = auctions.size();
		int index = 1;
		
		do
		{
			auctionSelected = false;

			System.out.print("*************************************************************************\n*			Browse Available Auctions			*\n*************************************************************************\n\n");

			System.out.print(((totalActiveAuctions > 1 || totalActiveAuctions == 0) ? totalActiveAuctions + " active auctions" : totalActiveAuctions + " active auction") + " found.\n");
	
			index = 1;
				
			for (Auction auction : auctions)
			{
				System.out.println("\n" + index + ": " + auction.displayAuction());
				index++;
			}
				
			while (auctionSelected == false)
			{
				System.out.print("-------------------------------------------------------------------------\n"); 
				System.out.print("Please select an option:						-\n"); 
				System.out.print("-------------------------------------------------------------------------"); 
				System.out.print("\n1 : Select an auction to view						-");
				System.out.print("\n2 : Update auctions list						-");
				System.out.print("\n3 : Return to main menu (Log out)					-\n"); 
				System.out.print("-------------------------------------------------------------------------\n"); 
				System.out.print("\nOption: "); 
						
				try
				{
					switch (S.next())
					{
						case "1":		
							selectAuction();
							break;
						
						case "2":
							browseAuction();
						
						case "3":
							browsing = false;
							auctionSelected = true;
							isLoggedIn = false;
							continue;
										
						default:
							throw new InputMismatchException();
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("An input error occured. Please try again.");
					continue;
				}
			}	
		}
		while (browsing == true);
	}
	
//	Selects an auction and displays the option to make a bid
	public static void selectAuction()
	{
		String selectedAuctionDesc = null;
		double bidAmount = 0;
		Buyer currentBuyer = null;

		System.out.print("*************************************************************************\n*			Select Auction to View				*\n*************************************************************************\n\n");

		System.out.print("-------------------------------------------------------------------------\n"); 
		System.out.print("Please select an auction by inputting the auctions name\ni.e. the 'Item Description'.\n");
		System.out.print("-------------------------------------------------------------------------\n\n");
		
		S.nextLine();
		
		System.out.print("Auction: ");
		selectedAuctionDesc = S.nextLine().trim();
		
		for (Auction selectedAuction : auctions)
		{
			
			if (selectedAuctionDesc.equalsIgnoreCase(selectedAuction.getDescription()))
			{
				System.out.print("\nSelected Auction:");
				System.out.println(selectedAuction.displayAuction());

				System.out.print("\n-------------------------------------------------------------------------\n"); 
				System.out.print("Please select an option:\n");
				System.out.print("-------------------------------------------------------------------------\n"); 
				System.out.print("1 : Make a bid on selected auction\n"); 
				System.out.print("2 : Cancel and return to select menu\n"); 
				System.out.print("-------------------------------------------------------------------------\n\n");
				
				try
				{
					switch(S.next())
					{
						case "1":
							// If the auction is not active display error message
							if(selectedAuction.getStatus() != 'A' || selectedAuction.isBlocked() == true)
							{
								System.out.print("\nError! This auction is currently " + (selectedAuction.isBlocked() == true ? "Blocked," : selectedAuction.auctionStatus()) + " cannot make bid.\nReturning to auctions list.\n\n");
								
								// waits for 5 seconds after displaying error message so user can read it without scrolling up
								try
								{
									TimeUnit.SECONDS.sleep(5);
								} catch (InterruptedException e)
								{
									e.printStackTrace();
								}
								
								browseAuction();
							}
							
							// Prompts user to log in if trying to make a bid if not currently logged in
							while(isLoggedIn == false)
							{
								System.out.print("Please log in:\n");
								logIn();
							}
							
							//	Finds the min and max bids that a user can make
							double minBid = selectedAuction.getHighestBid() + Auction.getLowerIncrement();
							double maxBid = selectedAuction.getHighestBid() + Auction.getUpperIncrement();
							
							//	Rounds the min and max bids to wo decimal places for readability
							double minBidRound = round(minBid, 2);
							double maxBidRound = round(maxBid, 2);

							System.out.print("Minimum Bid: " + minBidRound + "\n");
							System.out.print("Maximum Bid: " + maxBidRound + "\n");
							
							System.out.print("Bid Amount: ");
							bidAmount = S.nextDouble();
							
							currentBuyer = (Buyer) loggedIn;
							
							selectedAuction.placeBid(bidAmount, currentBuyer, LocalDateTime.now());
							
							selectedAuction.displayAuction();
							break;
						case "2":
							return;
						default:
							throw new InputMismatchException();
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("An input error occured. Please try again.");
					continue;
				}
			}
		}
	}
	
//	Method for rounding doubles
	public static double round(double value, int places)
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
//	Create new account:
	public static void setupAccount() 
	{
		String username = null;
		String password = null;
		String confirmPassword = null;
		
		boolean setupAccountDone = false;

		System.out.print("*************************************************************************\n*			Create Account					*\n*************************************************************************\n");
		
		System.out.print("Please enter your desired username and a password for your account	-\n"); 
		System.out.print("-------------------------------------------------------------------------\n"); 
		
		do
		{						
			try
			{
				System.out.print("\nDesired username: ");
				username = S.next();
				
				for (User user : users)
				{
					if (username.equals(user.getUsername()))	//	Check if username already exists
					{
						System.out.print("\nThis username is not available.\nPlease try again.");			
						break;
					}
				}
						

				System.out.print("\nDesired password: ");
				password = S.next();
					
				System.out.print("\nPlease confirm your password: ");
				confirmPassword = S.next();
				
				System.out.println(""); // UI Spacing
				
				// Compare two passwords to make sure they match, 
				// added this feature to make sure the user didn't make a mistake when entering their desired password
				if (password.equals(confirmPassword))
				{
					System.out.print("-------------------------------------------------------------------------\n"); 
					System.out.print("What account type do you wish to have?					-\n"); 
					System.out.print("-------------------------------------------------------------------------\n"); 
					System.out.print("1 : Buyer								-\n"); 
					System.out.print("2 : Seller								-\n"); 
					System.out.print("-------------------------------------------------------------------------\n"); 
					System.out.print("Press any key to cancel.\n\n"); 
					System.out.print("Option : "); 
						
					switch (S.next())
					{
						case "1":	//	Assign user as buyer
							users.addFirst(new Buyer(username, password));
							break;
							
						case "2":	//	Assign user as seller
							users.addFirst(new Seller(username, password));
							break;
							
						default:
							return;
					}
				
					System.out.print("\n\nThe account has been created sucessfully\n\n");
					setupAccountDone = true;
				}
				else
					System.out.print("\n\nThe entered passwords do not match. Try again.\n\n");
			}
			catch (InputMismatchException e)
			{
				System.out.print("\nAn input error occured. Please try again.\n");
				S.next();
				continue;
			}
		}
		while (setupAccountDone == false);
	}
	
//	Added methods
//	***********************************************************************************************
//	Displays the auctions successfully won by a 'Buyer':
	public static void viewWonAuctions()
	{
		Buyer buyer = Buyer.class.cast(loggedIn);
		
		int auctionsWon = 0;
		
		for (Auction auction : auctions)	//	Counts the number of auctions found
		{
			if(auction.getIsSold() && auction.getWinner().getUsername() == buyer.getUsername())
				auctionsWon++;
		}
		
		
		System.out.print("*************************************************************************\n*			Auctions Won					*\n*************************************************************************\n\n");

		System.out.print(((auctionsWon > 1 || auctionsWon == 0) ? auctionsWon + " won auctions" : auctionsWon + " won auction") + " found.\n");

		int index = 1;
			
		for (Auction auction : auctions)	//	Displays the found auctions
		{
			if(auction.getIsSold() && auction.getWinner().getUsername() == buyer.getUsername())
			{
				System.out.println("\n" + index + ": " + auction.displayAuction());
				index++;
			}
		}
	}
	
//	Displays the auctions successfully sold by a 'Seller':
	public static void viewSoldAuctions()
	{
		Seller seller = Seller.class.cast(loggedIn);
		
		int soldAuctions = 0;
		
		for (Auction auction : seller.getCurrentAuctions())	//	Counts the number of auctions found
		{
			if(auction.getIsSold())
				soldAuctions++;
		}
		
		
		System.out.print("*************************************************************************\n*			Auctions Sold					*\n*************************************************************************\n\n");

		System.out.print(((soldAuctions > 1 || soldAuctions == 0) ? soldAuctions + " sold auctions" : soldAuctions + " sold auction") + " found.\n");

		int index = 1;
			
		for (Auction auction : seller.getCurrentAuctions())	//	Displays the found auctions
		{
			if(auction.getIsSold())
			{
				System.out.println("\n" + index + ": " + auction.displayAuction());
				index++;
			}
		}
	}

	// Log in to existing account
	private static boolean logIn()
	{
		String username = null;
		String password = null;

		while (isLoggedIn == false)
		{
			System.out.print(
					"*************************************************************************\n*			Log in to Account				*\n*************************************************************************\n");

			System.out.print("Please enter the username and password for your account			-\n");
			System.out.print("-------------------------------------------------------------------------\n");

			System.out.print("\nUsername: ");
			username = S.next();

			System.out.print("\nPassword: ");
			password = S.next();

			for (User accounts : users) // Check if username and password exists and are correct
			{
				if (username.equals(accounts.getUsername()))
				{
					if (accounts.checkPassword(password) == true)
					{
						System.out.print("\nLogin sucessful.\n\n");
						loggedIn = accounts;
						isLoggedIn = true;
						return true;
					}
				}
			}
			System.out.println("Username or password is incorrect. Please try again.\n");
		}
		return false;
	}
}