<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUploadEditAd.js"></script>

<script src="/js/editAd.js"></script>


<script>
	$(document).ready(function() {		
		
		$(".slider").mousemove(function(){
			var newValue = $(this).find(".distance-slider").val();
			if(newValue == "5100")
				$(this).find(".range").html(">5km");
			else
				$(this).find(".range").html(newValue + "m");
		});
		
		$("#field-city").autocomplete({
			minLength : 2
		});
		$("#field-city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#field-city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		$("#field-moveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		$("#field-visitDay").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		$("#field-lastRenovationDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		
		
		$("#addVisitButton").click(function() {
			var date = $("#field-visitDay").val();
			if(date == ""){
				return;
			}
			
			var startHour = $("#startHour").val();
			var startMinutes = $("#startMinutes").val();
			var endHour = $("#endHour").val();
			var endMinutes = $("#endMinutes").val();
			
			if (startHour > endHour) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			} else if (startHour == endHour && startMinutes >= endMinutes) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			}
			
			var newVisit = date + ";" + startHour + ":" + startMinutes + 
				";" + endHour + ":" + endMinutes; 
			var newVisitLabel = date + " " + startHour + ":" + startMinutes + 
			" to " + endHour + ":" + endMinutes; 
			
			var index = $("#addedVisits input").length;
			
			var label = "<p>" + newVisitLabel + "</p>";
			var input = "<input type='hidden' value='" + newVisit + "' name='visits[" + index + "]' />";
			
			$("#addedVisits").append(label + input);
		});
		
		
	});
</script>

<!-- format the dates -->
<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd-MM-yyyy" />
<fmt:formatDate value="${ad.lastRenovation}" var="formattedRenovationDate"
	type="date" pattern="dd-MM-yyyy" />
	
<pre><a href="/">Home</a>   &gt;   <a href="/profile/myHouses">My Houses</a>   &gt;   <a href="/ad?id=${ad.id}">Ad Description</a>   &gt;   Edit Ad</pre>


<h1>Edit Ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/editAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

<input type="hidden" name="adId" value="${ad.id }" />

	<fieldset>
		<legend>Change General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-house">Type:</label></td>
			</tr>

			<tr>
				<td><form:input id="field-title" path="title" value="${ad.title}" /></td>
				<td>
					<c:choose>
						<c:when test="${ad.flat == 'true'}">
							<form:radiobutton id="type-house" path="flat" value="0"
								/>House <form:radiobutton id="type-flat"
								path="flat" value="1" checked="checked"/>Flat
						</c:when>
						<c:otherwise>
							<form:radiobutton id="type-house" path="flat" value="0"
								checked="checked" />House <form:radiobutton id="type-flat"
								path="flat" value="1" />Flat
						</c:otherwise>
					</c:choose>
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						value="${ad.street}" /></td>
				<td>
					<form:input id="field-city" path="city" value="${ad.zipcode} - ${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" />
				</td>
			</tr>

			<tr>
				<td><label for="moveInDate">Available From</label></td>
			</tr>
			<tr>
				<td>
					<form:input type="text" id="field-moveInDate"
						path="moveInDate" value="${formattedMoveInDate }"/>
				</td>
			</tr>

			<tr>
				<td><label for="field-Price">Price per month</label></td>
				<td><label for="field-RunningCosts">Running Costs per month</label></td>
				<td><label for="field-SquareFootage">Square Meters</label></td>
			</tr>
			<tr>
				<td>
					<form:input id="field-Price" type="number" path="price"
						placeholder="e.g. 500" step="0.05" value="${ad.price }"/> <form:errors
						path="price" cssClass="validationErrorText" />CHF
				</td>
				<td>
					<form:input id="field-RunningCosts" type="number" path="runningCosts"
						placeholder="e.g. 140" step="0.05" value="${ad.runningCosts }"/> <form:errors
						path="runningCosts" cssClass="validationErrorText" />CHF
				</td>
				<td>
					<form:input id="field-SquareFootage" type="number"
						path="squareFootage" placeholder="e.g. 50" step="0.5" 
						value="${ad.squareFootage }"/> <form:errors
						path="squareFootage" cssClass="validationErrorText" />m²
				</td>
			</tr>
		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Change House Description</legend>

		<table class="placeAdTable">
			<tr>
				<td><label for="lastRenovation">Last Renovation (Optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-lastRenovationDate"
						path="lastRenovation" value="${formattedRenovationDate }" /></td>
			</tr>
			
			<tr>
				<td><label for="field-Floor">Floor</label>
				<td><label for="field-NumberOfRooms">Number of Rooms</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Floor" type="number" path="floor"
						placeholder="e.g. -1" step="1" value="${ad.floor }"/> <form:errors
						path="floor" cssClass="validationErrorText" /></td>		
				<td><form:input id="field-NumberOfRooms" type="number"
						path="numberOfRooms" placeholder="e.g. 2" step="0.5" value="${ad.numberOfRooms }" /> <form:errors
						path="numberOfRooms" cssClass="validationErrorText" /></td>
			</tr>
			
			<tr>
				<td><label for="field-DistanceToNearestSuperMarket">Distance to nearest Super Market</label>
				<td><label for="field-DistanceToNearestPublicTransport">Distance to nearest Public Transport</label>
				<td><label for="field-DistanceToNearestSchool">Distance to nearest School</label>
			</tr>
			<tr>
				<td class="slider"><form:input id="field-DistanceToNearestSuperMarket" class="distance-slider" type="range" path="distanceToNearestSuperMarket"
						min="100" max="5100" step="100" value="${ad.distanceToNearestSuperMarket}" />
						<span class="range">${ad.distanceToNearestSuperMarket}m</span></td>		
				<td class="slider"><form:input id="field-DistanceToNearestPublicTransport" class="distance-slider" type="range" path="distanceToNearestPublicTransport"
						min="100" max="5100" step="100" value="${ad.distanceToNearestPublicTransport}" />
						<span class="range">${ad.distanceToNearestPublicTransport}m</span></td>	
				<td class="slider"><form:input id="field-DistanceToNearestSchool" class="distance-slider" type="range" path="distanceToNearestSchool"
						min="100" max="5100" step="100" value="${ad.distanceToNearestSchool}"/>
						<span class="range">${ad.distanceToNearestSchool}m</span></td>	
			</tr>
			
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.smokers}">
							<form:checkbox id="field-smoker" path="smokers" checked="checked" /><label>Smoking
							inside allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-smoker" path="smokers" /><label>Smoking
							inside allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.animals}">
							<form:checkbox id="field-animals" path="animals"  checked="checked" /><label>Animals
						allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-animals" path="animals" /><label>Animals
						allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.garden}">
							<form:checkbox id="field-garden" path="garden" checked="checked" /><label>Garden
							(co-use)</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garden" path="garden" /><label>Garden
							(co-use)</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.balcony}">
							<form:checkbox id="field-balcony" path="balcony"  checked="checked" /><label>Balcony
						or Patio</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-balcony" path="balcony" /><label>Balcony
						or Patio</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cellar}">
							<form:checkbox id="field-cellar" path="cellar" checked="checked" /><label>Cellar
						or Attic</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cellar" path="cellar" /><label>Cellar
						or Atticd</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.furnished}">
							<form:checkbox id="field-furnished" path="furnished"  checked="checked" /><label>Furnished
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-furnished" path="furnished" /><label>Furnished</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cable}">
							<form:checkbox id="field-cable" path="cable" checked="checked" /><label>Cable TV</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cable" path="cable" /><label>Cable TV</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.garage}">
							<form:checkbox id="field-garage" path="garage"  checked="checked" /><label>Garage
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garage" path="garage" /><label>Garage</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>

		</table>
		<br />
		<form:textarea path="houseDescription" rows="10" cols="100" value="${ad.houseDescription}" />
		<form:errors path="houseDescription" cssClass="validationErrorText" />
	</fieldset>
	
	<fieldset>
		<legend>Add visiting times</legend>
		
		<table>
			<tr>
				<td>
					<input type="text" id="field-visitDay" />
					
					<select id="startHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="startMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
					
					<span>to&thinsp; </span>
					
					<select id="endHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="endMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
			

					<div id="addVisitButton" class="smallPlusButton">+</div>
					
					<div id="addedVisits"></div>
				</td>
				
			</tr>
			
		</table>
		<br>
	</fieldset>

	<br />

	<fieldset>
		<legend>Change pictures</legend>
		<h3>Delete existing pictures</h3>
		<br />
		<div>
			<c:forEach items="${ad.pictures }" var="picture">
				<div class="pictureThumbnail">
					<div>
					<img src="${picture.filePath}" />
					</div>
					<button type="button" data-ad-id="${ad.id }" data-picture-id="${picture.id }">Delete</button>
				</div>
			</c:forEach>
		</div>
		<p class="clearBoth"></p>
		<br /><br />
		<hr />
		<h3>Add new pictures</h3>
		<br />
		<label for="field-pictures">Pictures</label> <input
			type="file" id="field-pictures" accept="image/*" multiple="multiple" />
		<table id="uploaded-pictures" class="styledTable">
			<tr>
				<th id="name-column">Uploaded picture</th>
				<th>Size</th>
				<th>Delete</th>
			</tr>
		</table>
		<br>
	</fieldset>

	<div>
		<button type="submit">Submit</button>
		<a href="<c:url value='/ad?id=${ad.id}' />"> 
			<button type="button">Cancel</button>
		</a>
	</div>

</form:form>


<c:import url="template/footer.jsp" />
