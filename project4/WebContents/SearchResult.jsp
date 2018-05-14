<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Keywords Search</title>
    <link rel="stylesheet" href="css/searchmain.css">
    <link rel="stylesheet" href="css/suggestion.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<a class = "head1" href="index.html">eBay Search Web Site</a>
        <h1>Keywords Search</h1>
        <div Style="width: 100%; text-align: center;">
	        <form action="/eBay/search" method="GET">
	            Please enter your keywords:
	            <br>
	            <br>
	            <input type="text" name="q" id="searchContent"/>
	            <input type="submit" value="submit"/>
	            <div class="suggestions"></div>
	            <input name="numResultsToSkip" type="hidden" value="0"/>
	            <input name="numResultsToReturn" type="hidden" value="10"/>
	        </form>
        </div>

        <div style="width: 100%; text-align: center;">
        	<span>Showing Search Results for <b>${q}</b> <span>	
				<c:choose>
					<c:when test="${empty results}">
						<div class="col-md-12 text-center">
							<span> No results found for <b>${q}</b> </span>
						</div>
					</c:when>
					<c:otherwise>
						<ul>
						<table style="width:960px"  "text-align:center" "margin:0 auto">
							<tr>
						    	<td><b>Item</b></td>
						    	<td><b>ItemID</b></td> 
						  	</tr>
							<c:forEach var="result" items="${results}">
								<tr>
									<td>${result.name}</td>
									<td><a id="${result.itemId}" href="item?id=${result.itemId}">${result.itemId}</a></td>
								</tr>
							</c:forEach>
						</table>
						</ul>
						<br/>
						<a id="prev" href="search?q=${q}&
						numResultsToSkip=${numResultsToSkip-numResultsToReturn}&numResultsToReturn=${numResultsToReturn}">
						Previous</a>
						<a id="next" href="search?q=${q}&
						numResultsToSkip=${numResultsToSkip+numResultsToReturn}&numResultsToReturn=${numResultsToReturn}">
						   &nbsp;Next</a>
						<script type="text/javascript">
				            var numResultsToSkip = parseInt("${numResultsToSkip}");
				            var prev = document.getElementById("prev");
				            if (numResultsToSkip <= 0) {
				                prev.innerHTML = "";
				            }
				            var next = document.getElementById("next");
				            if (!${requestMore}) {
				                next.innerHTML = "";
				            }
				        </script>
				    </c:otherwise>
				</c:choose>
		</div>

		<script type="text/javascript" src="autosuggestion.js"></script>
        <script type="text/javascript" src="suggestion.js"></script>
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("searchContent"), new StateSuggestions());
                }
        </script>
</body>
</html>
