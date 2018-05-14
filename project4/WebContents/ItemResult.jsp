<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Keywords Search</title>
    <link rel="stylesheet" href="css/map.css">
    <link rel="stylesheet" href="css/searchmain.css">
    <link rel="stylesheet" href="css/suggestion.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body onload="initialize('${Item.location}', '${Item.latitude}', '${Item.longitude}')">
    <a class="head1" href="index.html">eBay Search Web Site</a>
    <h1>ItemID Search</h1>
    <div Style="width: 100%; text-align: center;">
	    <form action="/eBay/item" method="GET">
	        Please enter an itemID:
	        <br>
	        <br>
	        <input type="text" name="id"/>
	        <input type="submit" value="submit"/>
	        <div class="suggestions"></div>
	        <input name="numResultsToSkip" type="hidden" value="0"/>
	        <input name="numResultsToReturn" type="hidden" value="10"/>
	    </form>
    </div>

    <div style="width: 100%; text-align: center;">
    	<span>Showing Search Results for <b>${id}</b> </span>
    </div>	
		<c:choose>
			<c:when test="${empty xmlData}">
				<div class="row">
					<div class="col-md-12 text-center">
						<span> No results found for <b>${id}</b> </span>
					</div>
				</div>
        	</c:when>
				<c:otherwise>
				<div class="container">
        			<div class="row row-centered">
            			<div class="col-md-4 col-centered col-fixed">
                		<div id="map"></div>
			                <p><b>Location:</b> ${Item.location} </p>
			                <p><b>Country:</b> ${Item.country}</p>
			                <p> <b>Coordinates(latitude, longitude):</b>
			                	<c:choose>
			                    	<c:when test="${empty Item.latitude and empty Item.longitude}">N/A</c:when>
			                        <c:otherwise>(${Item.latitude}, ${Item.longitude})</c:otherwise>
			                    </c:choose>
			                </p>
		                </div>
		            <div class="col-md-4 col-centered col-fixed">
		                <p><b>Name:</b> ${Item.name}</p>
		                <p><b>ID:</b> ${id} </p>
		                <p> <b>Categories:</b> </p>
		                <ul>
		                    <c:forEach var="category" items="${Item.categories}">
		                        <li>${category}</li>
		                    </c:forEach>
		                </ul>
		                <p><b>Description:</b> ${Item.description}</p>
		            </div>
		            <div class="col-md-4 col-centered col-fixed">
		                <p><b>Currently:</b> ${Item.currently}</p>
		                <p><b>Started:</b> ${Item.started}</p>
		                <p><b>Ends:</b> ${Item.ends} </p>
		                <p><b>SellerID:</b> ${Item.sellerID} </p>
		                <p><b>Rating:</b> ${Item.rating}</p>
		                <p><b>First Bid:</b>
		                <c:choose>
		                    <c:when test="${empty Item.firstBid}">N/A
		                	</c:when>
		                    <c:otherwise>${Item.firstBid}</c:otherwise>
			            </c:choose>
			            </p>
		                <p><b>Buy Price:</b>
		                    <c:choose>
		                        <c:when test="${empty Item.buyPrice}">N/A</c:when>
		                        <c:otherwise>${Item.buyPrice}
		                    	</c:otherwise>
	                    	</c:choose>
	                	</p>
	                	<p> <b>Bids:</b>
	                    	<c:choose>
	                        	<c:when test="${empty Item.numBids}">0</c:when>
	                        	<c:otherwise>${Item.numBids}</c:otherwise>
	                    	</c:choose></p>
	                	</p>
	                	<ol>
		                    <c:forEach var="bid" items="${Item.bids}">
		                        <li>
		                            <p> <i>Time:</i> ${bid.bidTime} </p>
		                            <p> <i>Amount:</i> ${bid.bidAmount}</p>
		                            <p> <i>User:</i> ${bid.bidderID} </p>
		                            <p> <i>Rating:</i> ${bid.bidderRating} </p>
		                            <p> <i>Bidder Location:</i> ${bid.bidderLocation} </p>
		                            <p> <i>Bidder Country:</i> ${bid.bidderCountry} </p>
		                        </li>
		                    </c:forEach>
	                	</ol>
		     		</div>
		     		</div>
		     	</div>
	        </c:otherwise>
	    </c:choose>
	<script type="text/javascript"
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBu6YXfwhutONCR07kC8tMVb-7ruvmzfYU">
    </script>
<!-- 	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3"></script> -->
	<script type="text/javascript" src="map.js"></script>
</body>
</html>