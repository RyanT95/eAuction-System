# eAuction-System
Simple eAuction console application made as part of a University group project

## Specification
*"The eAuction system allows multiple users to conduct auctions electronically. Once a user has setup an account, they are able to act as a seller and start an auction by listing an item to be sold. They may also act as a buyer by browsing auctions that are in progress and bidding on any item whose auction has not closed. A user does not need to setup an account to simply browse auctions in progress.

An auction is started when a seller inputs data including, a description of the item, the starting price, the reserve price and a date when the auction will close. The system enforces an upper and lower bidding increment of 20% and 10% of the starting price respectively. The system also enforces a closing date ≤ 7 days from the current date. Once the data has been input, the auction automatically becomes pending and the seller must verify the auction before it starts.

When a user wants to make a bid against an item, they must first locate the auction in question by browsing the auctions that are in progress. They may then make a bid that the system must check is within the upper and lower bidding increment amounts.

Each auction keeps track of every bid made against the item. Once the auction closes the system checks to see if the item’s reserve price has been met and if so, informs the buyer with the highest bid of their victory. If the item’s reserve price has not been met, the system informs every buyer who made a bid and the auction is closed.

Both sellers and auctions may be temporarily blocked which prevents the sellers from logging on or the auctions from being browsed or bid upon respectively."*

### Prerequisites

```
1. [JRE](https://www.java.com/en/download/)
2. [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html)
3. Java IDE (Eclipse, NetBeans etc.)
```

### Running The Code

* Make sure all prerequisites are installed.
* Import the project into your favourite Java IDE
* Upon running the code, you are greeted with the following in the console:
```
RyanT has made a bid of £3806.51 on the item "1972 Datsun 100A" - 15/08/2019 15:03

RyanB has made a bid of £3814.99 on the item "1972 Datsun 100A" - 15/08/2019 15:03

RyanT has made a bid of £52.99 on the item "Commodore 64 (Boxed, Like-new)" - 15/08/2019 15:03

*************************************************************************
*			eAuction System					*
*************************************************************************
1 : Browse Avaliable Auctions						-
2 : Setup account							-
3 : Log in to existing account						-
4 : Exit								-
-------------------------------------------------------------------------
```
The first three lines are simulated users bidding on items. The main block is the menu of the auction system, presenting the various actions the user can perform.

If you see the following below the menu:
```
Option : 
Auction: "1972 Datsun 100A" not sold. The auction is now closed.

Auction: "Do Androids Dream of Electric Sheep? Philip K. Dick" not sold. The auction is now closed.

Auction: "Queen Elizabeth II's Hair" not sold. The auction is now closed.

Auction: "Commodore 64 (Boxed, Like-new)" sold! The auction is now closed.
```
It means that the end date for all the auctions has been reached, to open the auctions, you will have to change the dates on the four hardcoded auctions in the "Test data:" section of "Auction System.java"

* Option 1 shows all current auctions, and displays information about each.
* Option 2 allows users to set up an account for bidding or selling.
* Option 3 allows existing users to log in.
  * There are four existing users:
   * "RyanT", pass - "password" (Buyer Account)
   * "RyanB", pass - "password" (Buyer Account)
   * "User3", pass - "password" (Seller Account)
   * "User4", pass - "password" (Seller Account)
* Option 4 exits the application
* The menus are navigated by typing the number of the option you want, and pressing ENTER.

## Built With

* [Eclipse IDE](https://www.eclipse.org/downloads/) - IDE

## Authors

* **Ryan Tinman** - [RyanT95](https://github.com/RyanT95)
* **Ryan Biggs** - [RyanBiggs](https://github.com/RyanBiggs)

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE - see the [LICENSE.md](LICENSE.md) file for details
