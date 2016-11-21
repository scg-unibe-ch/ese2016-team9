<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
	
    
<!-- check if user is logged in -->
<c:set var="loggedIn" value="${pageContext.request.userPrincipal.authenticated}" />
    
<c:import url="template/header.jsp" />
    
<pre><a href="/">Home</a> &gt; <a href="/profile/myHouses">My Houses</a> &gt; Ad Description</pre>

<script src="/js/image_slider.js"></script>
<script src="/js/adDescription.js"></script>

<script>
	var shownAdvertisementID = "${shownAd.id}";
	var shownAdvertisement = "${shownAd}";
	
	function attachBookmarkClickHandler(){
		$("#bookmarkButton").click(function() {
			
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: false}, function(data) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 3:
					$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");	
				}
				
				attachBookmarkedClickHandler();
			});
		});
	}
	
	function attachBookmarkedClickHandler(){
		$("#bookmarkedButton").click(function() {
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: true}, function(data) {
				$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 2:
					$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");
					
				}			
				attachBookmarkClickHandler();
			});
		});
	}

	$(document).ready(function() {
		attachBookmarkClickHandler();
		attachBookmarkedClickHandler();
		
		$.post("/bookmark", {id: shownAdvertisementID, screening: true, bookmarked: true}, function(data) {
			if(data == 3) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				attachBookmarkedClickHandler();
			}
			if(data == 4) {
				$('#shownAdTitle').replaceWith($('<h1>' + "${shownAd.title}" + '</h1>'));
			}
		});
		
		$("#newMsg").click(function(){
			$("#content").children().animate({opacity: 0.4}, 300, function(){
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});
		
		$("#messageCancel").click(function(){
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({opacity: 1}, 300);
		});
		
		$("#messageSend").click(function (){
			if($("#msgSubject").val() != "" && $("#msgTextarea").val() != ""){
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${shownAd.user.username}";
				$.post("profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					$("#msgDiv").css("display", "none");
					$("#msgDiv").css("opacity", "0");
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$("#content").children().animate({opacity: 1}, 300);
				})
			}
		});
	});
		
</script>


<!-- format the dates -->
<fmt:formatDate value="${shownAd.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${shownAd.creationDate}" var="formattedCreationDate"
	type="date" pattern="dd.MM.yyyy" />

<h1 id="shownAdTitle">${shownAd.title}
	<c:choose>
		<c:when test="${loggedIn}">
			<a class="right" id="bookmarkButton">Bookmark Ad</a>
		</c:when>
	</c:choose>
</h1>


<hr />

<section>
	<c:choose>
		<c:when test="${loggedIn}">
			<c:if test="${loggedInUserEmail == shownAd.user.username }">
				<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
					<button type="button" class="btn btn-default">Edit Ad</button>
				</a>
			</c:if>
		</c:when>
	</c:choose>
	<br>
	<br>

    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6">
                <table class="table">
                    <tr>
                        <th>Type</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.flat}">Flat</c:when>
                                <c:otherwise>House</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Address</th>
                        <td>
                            <a class="link" href="http://maps.google.com/?q=${shownAd.street}, ${shownAd.zipcode}, ${shownAd.city}">${shownAd.street},
                                    ${shownAd.zipcode} ${shownAd.city}</a>
                        </td>
                    </tr>

                    <tr>
                        <th>${shownAd.forSale ? "Prize" : "Monthly Rent"}</th>
                        <td>${shownAd.prize}&#32;CHF</td>

                    <tr>
                        <th>Available from</th>
                        <td>${formattedMoveInDate}</td>
                    </tr>

                    <c:choose>
                        <c:when test="shownAd.forSale">
                            <tr>
                                <th>Running Costs</th>
                                <td>${shownAd.runningCosts}&#32;CHF</td>
                            </tr>
                        </c:when>
                    </c:choose>

                    <tr>
                        <th>Square Meters</th>
                        <td>${shownAd.squareFootage}&#32;m²</td>
                    </tr>
                    <tr>
                        <th>Ad created on</th>
                        <td>${formattedCreationDate}</td>
                    </tr>
                </table>
            </div>
        
            <div class="col-md-6">
                <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <c:forEach items="${shownAd.pictures}" var="picture" varStatus = "status">
                            <li data-target="#carousel-example-generic" data-slide-to="${status.index}"${status.first ? ' class="active"' : ''}></li>
                        </c:forEach>
                    </ol>

                    <!-- Wrapper for slides -->
                    <div class="carousel-inner" role="listbox">
                        <c:forEach items="${shownAd.pictures}" var="picture" varStatus = "status">
                            <div class="item${status.first ? ' active' : ''}">
                                <img src="${picture.filePath}" style="margin: auto;" />
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Controls -->
                    <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
            
            </div>
        </div>
    
    </div>
    
</section>

<hr class="clearBoth" />

<section>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6">
                <div class="">
                    <h2>House Description</h2>
                    <p>${shownAd.houseDescription}</p>
                </div>
            </div>
            
            <div class="col-md-6">
                <c:choose>
                    <c:when test="${shownAd.isAuction()}">
                        <h2>Auction</h2>
                        <table class="table">
                            <tr>
                                <th>User</th>
                                <th>Bidden price</th>
                                <th>Date</th>
                            </tr>
                            <c:choose>
                                <c:when test="${loggedIn}">
                                    <form:form method="post" modelAttribute="betForm" action="/makeBet?id=${shownAd.id}" id="betForm" autocomplete="off" class="form-horizontal" enctype="multipart/form-data">
                                    <tr>
                                        <td>
                                            <span class="auctionUsername">
                                                ${loggedInUserEmail}
                                            </span>
                                        </td>
                                        <td>
                                            <form:input id="field-Price" type="number" path="price" placeholder="Bid" step="50" class="form-control" value="${shownAd.getHighestBet()}" min="${shownAd.getHighestBet()}" />   
                                            <form:errors path="price" cssClass="validationErrorText" />
                                        </td>
                                        <td>
                                            <button type="Submit" class="btn btn-default">Submit</button>
                                        </td>
                                    </tr>
                                    </form:form>
                                </c:when>
                            </c:choose>
                            <c:forEach items="${shownAd.bets}" var="bet" varStatus="status">

                                <fmt:formatNumber value="${bet.price}" type="currency" currencySymbol="" var="formattedPrice"/>
                                <fmt:formatDate value="${bet.creationDate}" var="formattedBetDate" type="date" pattern="dd.MM.yyyy" />
                                <tr>
                                    <td>${bet.user.username}</td>
                                    <td>${formattedPrice}&#32;CHF</td>
                                    <td>${formattedBetDate}</td>
                                </tr>
                            </c:forEach>
                            
                                
                        </table>
                    </c:when>
                </c:choose>
            
                <h2>Advertiser</h2>
                <table id="advertiserTable" class="table">
                    <tr>
                        <td><c:choose>
                                <c:when test="${shownAd.user.picture.filePath != null}">
                                    <img src="${shownAd.user.picture.filePath}">
                                </c:when>
                                <c:otherwise>
                                    <img src="/img/avatar.png">
                                </c:otherwise>
                            </c:choose></td>

                        <td>${shownAd.user.username}</td>

                        <td id="advertiserEmail">
                        <c:choose>
                            <c:when test="${loggedIn}">
                                <a href="/user?id=${shownAd.user.id}"><button class="btn btn-default" type="button">Visit profile</button></a>
                            </c:when>
                            <c:otherwise>
                                <a href="/login"><button class="btn btn-default" type="button">Login to visit profile</button></a>
                            </c:otherwise>
                        </c:choose>

                        <td>
                            <form>
                                <c:choose>
                                    <c:when test="${loggedIn}">
                                        <c:if test="${loggedInUserEmail != shownAd.user.username }">
                                            <button id="newMsg" class="btn btn-default" type="button">Contact Advertiser</button>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="/login"><button class="btn btn-default" type="button">Login to contact advertiser</button></a>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </table>
            </div>
            
            
        </div>
        
        <div class="row">
            <div class="col-md-6">
                <table id="checkBoxTable" class="table">
                    <tr>
                        <th>Furnished House</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.furnished}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <c:choose>
                        <c:when test="${shownAd.forSale}">

                        </c:when>
                        <c:otherwise>
                                    <tr>
                                    <th>Animals allowed</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${shownAd.animals}"><img src="/img/check-mark.png"></c:when>
                                            <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>

                                <tr>
                                    <th>Smoking inside allowed</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${shownAd.smokers}"><img src="/img/check-mark.png"></c:when>
                                            <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                        </c:otherwise>
                    </c:choose>

                    <tr>
                        <th>Cable TV</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.cable}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Garage</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.garage}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Cellar</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.cellar}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Balcony</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.balcony}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Garden</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.garden}"><img src="/img/check-mark.png"></c:when>
                                <c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                </table>
            </div>
            
            <div class="col-md-6">
                <table id="" class="table">
                    <tr>
                        <th>Floor</th>
                        <td>${shownAd.floor}</td>
                    </tr>

                    <tr>
                        <th>Number of Rooms</th>
                        <td>${shownAd.numberOfRooms}</td>
                    </tr>

                    <tr>
                        <th>Last Renovation</th>
                        <td>
                            <c:choose>
                                <c:when test="${shownAd.lastRenovation!=null}">${shownAd.lastRenovation }</c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>

                    <tr>
                        <th>Nearest Super Market</th>
                        <td>${shownAd.distanceToNearestSuperMarket}m</td>
                    </tr>

                    <tr>
                        <th>Public Transport</th>
                        <td>${shownAd.distanceToNearestPublicTransport}m</td>
                    </tr>

                    <tr>
                        <th>Nearest School</th>
                        <td>${shownAd.distanceToNearestSchool}m</td>
                    </tr>
                </table>
            </div>    
        </div>
        <div class="row">
            <div class="col-md-6">
                <div id="visitList" class="">
                    <h2>Visiting times</h2>
                    <table class="table">
                        <c:forEach items="${visits }" var="visit">
                            <tr>
                                <td>
                                    <fmt:formatDate value="${visit.startTimestamp}" pattern="dd-MM-yyyy " />
                                    &nbsp; from
                                    <fmt:formatDate value="${visit.startTimestamp}" pattern=" HH:mm " />
                                    until
                                    <fmt:formatDate value="${visit.endTimestamp}" pattern=" HH:mm" />
                                </td>
                                <td><c:choose>
                                        <c:when test="${loggedIn}">
                                            <c:if test="${loggedInUserEmail != shownAd.user.username}">
                                                <button class="btn btn-default thinButton" type="button" data-id="${visit.id}">Send
                                                    enquiry to advertiser</button>
                                            </c:if>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/login"><button class="btn btn-default thinInactiveButton" disabled="disabled" type="button"
                                                data-id="${visit.id}">Login to send enquiries</button></a>
                                        </c:otherwise>
                                    </c:choose></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                
            </div>
        </div>
    </div>
</section>

<div class="clearBoth"></div>
<br>

<div id="msgDiv">
<form class="msgForm">
	<h2>Contact the advertiser</h2>
	<br>
	<br>
	<label>Subject: <span>*</span></label>
	<input  class="msgInput form-control" type="text" id="msgSubject" placeholder="Subject" />
	<br><br>
	<label>Message: </label>
	<textarea id="msgTextarea" class="textarea" placeholder="Message" ></textarea>
	<br/>
	<button type="button" class="btn btn-default" id="messageSend">Send</button>
	<button type="button" class="btn btn-default" id="messageCancel">Cancel</button>
	</form>
</div>

<div id="confirmationDialog">
	<form>
	<p>Send enquiry to advertiser?</p>
	<button type="button" class="btn btn-default" id="confirmationDialogSend">Send</button>
	<button type="button" class="btn btn-default" id="confirmationDialogCancel">Cancel</button>
	</form>
</div>


<c:import url="template/footer.jsp" />