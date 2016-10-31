<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>

<script src="/js/pictureUpload.js"></script>


<script>
	$(document).ready(function() {
		
		// Go to controller take what you need from user
		// save it to a hidden field
		// iterate through it
		// if there is id == x then make "Bookmark Me" to "bookmarked"
		
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
		$("#field-moveOutDate").datepicker({
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

<pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

	<fieldset>
		<legend>General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-house">Type:</label></td>
			</tr>

			<tr>
				<td><form:input id="field-title" path="title"
						placeholder="Ad Title" /></td>
				<td><form:radiobutton id="type-house" path="flat" value="0"
						checked="checked" />House <form:radiobutton id="type-flat"
						path="flat" value="1" />Flat</td>
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="Street" /></td>
				<td><form:input id="field-city" path="city" placeholder="City" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
			</tr>

			<tr style="display:${isRentingAd ? "" : "none"}">
				<td><label for="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate">Move-out date (optional)</label></td>
			</tr>
			<tr style="display:${isRentingAd ? "" : "none"}">
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /></td>
			</tr>

			<tr>
                
				<td><label for="field-Prize">${isRentingAd ? "Prize per Month" : "Prize"}</label></td>
				<td><label for="field-RunningCosts">Running Costs per month</label>
				<td><label for="field-SquareFootage">Square Meters</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Prize" type="number" path="prize"
						placeholder="e.g. 500" step="50" /> <form:errors
						path="prize" cssClass="validationErrorText" />CHF</td>
				<td><form:input id="field-RunningCosts" type="number" path="runningCosts"
						placeholder="e.g. 140" step="10" /> <form:errors
						path="runningCosts" cssClass="validationErrorText" />CHF</td>		
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" placeholder="e.g. 50" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" />m²</td>
			</tr>
		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>House Description</legend>

		<table class="placeAdTable">
			<tr>
				<td><label for="lastRenovation">Last Renovation (Optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-lastRenovationDate"
						path="lastRenovation" /></td>
			</tr>
			
			<tr>
				<td><label for="field-Floor">Floor</label>
				<td><label for="field-NumberOfRooms">Number of Rooms</label></td>
			</tr>
			<tr>
				<td><form:input id="field-Floor" type="number" path="floor"
						placeholder="e.g. -1" step="1" /> <form:errors
						path="floor" cssClass="validationErrorText" /></td>		
				<td><form:input id="field-NumberOfRooms" type="number"
						path="numberOfRooms" placeholder="e.g. 2" step="1" /> <form:errors
						path="numberOfRooms" cssClass="validationErrorText" /></td>
			</tr>
			
			<tr>
				<td><label for="field-DistanceToNearestSuperMarket">Distance to nearest Super Market</label>
				<td><label for="field-DistanceToNearestPublicTransport">Distance to nearest Public Transport</label>
				<td><label for="field-DistanceToNearestSchool">Distance to nearest School</label>
			</tr>
			<tr>
				<td><form:input id="field-DistanceToNearestSuperMarket" type="number" path="distanceToNearestSuperMarket"
						placeholder="e.g. 0.5" step="1" /> <form:errors
						path="DistanceToNearestSuperMarket" cssClass="validationErrorText" />meters</td>		
				<td><form:input id="field-DistanceToNearestPublicTransport" type="number" path="distanceToNearestPublicTransport"
						placeholder="e.g. 0.5" step="1" /> <form:errors
						path="DistanceToNearestPublicTransport" cssClass="validationErrorText" />meters</td>	
				<td><form:input id="field-DistanceToNearestSchool" type="number" path="distanceToNearestSchool"
						placeholder="e.g. 0.5" step="1" /> <form:errors
						path="DistanceToNearestSchool" cssClass="validationErrorText" />meters</td>	
			</tr>
			
			<tr style="display:${isRentingAd ? "" : "none"}">
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Animals
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Smoking
						inside allowed</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-garden" path="garden" value="1" /><label>Garden
						(co-use)</label></td>
				<td><form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony
						or Patio</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cellar" path="cellar" value="1" /><label>Cellar
						or Attic</label></td>
				<td><form:checkbox id="field-furnished" path="furnished"
						value="1" /><label>Furnished</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cable" path="cable" value="1" /><label>Cable
						TV</label></td>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
				</td>
			</tr>
			<tr style="display:${isRentingAd ? "" : "none"}">
				<td><form:checkbox id="field-internet" path="internet"
						value="1" /><label>WiFi available</label></td>
			</tr>

		</table>
		<br />
		<form:textarea path="houseDescription" rows="10" cols="100"
			placeholder="House Description" />
		<form:errors path="houseDescription" cssClass="validationErrorText" />
	</fieldset>

	
	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			placeholder="Preferences"></form:textarea>
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
		<br /> <label for="field-pictures">Pictures</label> <input
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

	<fieldset>
		<legend>Visiting times (optional)</legend>

		<table>
			<tr>
				<td><input type="text" id="field-visitDay" /> <select
					id="startHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="startMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select> <span>to&thinsp; </span> <select id="endHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="endMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>



					<div id="addVisitButton" class="smallPlusButton">+</div>

					<div id="addedVisits"></div></td>

			</tr>

		</table>
		<br>
	</fieldset>



	<br />
	<div>
		<button type="submit">Submit</button>
		<a href="/"><button type="button">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
