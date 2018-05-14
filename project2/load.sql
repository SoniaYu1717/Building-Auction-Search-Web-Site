LOAD DATA LOCAL INFILE 'bidder.dat'
INTO TABLE Bidder
FIELDS TERMINATED BY '|*|'
LINES TERMINATED BY "\n"
(UserID, Rating, @location, @country) SET Location = nullif(@location, ''),
											Country = nullif(@country, '');

LOAD DATA LOCAL INFILE 'seller.dat'
INTO TABLE Seller
FIELDS TERMINATED BY '|*|'
LINES TERMINATED BY "\n";

LOAD DATA LOCAL INFILE 'bid.dat'
INTO TABLE Bid
FIELDS TERMINATED BY '|*|'
LINES TERMINATED BY "\n"
(ItemID, UserID, @time, Amount) SET Time = STR_TO_DATE(@time, "%Y-%m-%d %H:%i:%s");

LOAD DATA LOCAL INFILE 'item.dat'
INTO TABLE Item
FIELDS TERMINATED BY '|*|'
LINES TERMINATED BY "\n"
(ItemID, Name, Currently, @buy_price, First_Bid, Number_Of_Bids, Location, @latitude,
@longitude, Country, @started, @ends, SellerID, Description)
SET Buy_Price = nullif(@buy_price, ''),
	Latitude = nullif(@latitude, ''),
	Longitude = nullif(@longitude, ''),
	Started = STR_TO_DATE(@started, "%Y-%m-%d %H:%i:%s"),
	Ends = STR_TO_DATE(@ends, "%Y-%m-%d %H:%i:%s");

LOAD DATA LOCAL INFILE 'category.dat'
INTO TABLE ItemCategory
FIELDS TERMINATED BY '|*|'
LINES TERMINATED BY "\n";