Part B
1. List your relations.

Item(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Latitude, Longitude, Country, Started, Ends, SellerID, Description)  Key: Item ID

Bidder(UserID, Rating, Location, Country)  Key: UserID

Seller(UserID, Rating)  Key: UserID 

Bid(Item ID, UserID, Time, Amount)  Key: (Item ID, UserID, Time)

Category(ItemID, Category)  Key: (ItemID, Category)

2. List all completely nontrivial functional dependencies that hold on each relation, excluding those that effectively specify keys.

The functional dependencies that hold on each relation are all for keys.

3.Are all of your relations in Boyce-Codd Normal Form (BCNF)? 

All the relations are in BCNF because for every nontrivial functional dependency X -> Y, X is a super key.

4.Are all of your relations in Fourth Normal Form (4NF)? 

All the relations have no nontrivial multivalued dependencies. Combined with the fact that all relations are in BCNF, all of the relations are in 4NF.