<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a> &gt; Alerts</pre>

<script>
function deleteAlert(button) {
	var id = $(button).attr("data-id");
	$.get("/profile/alerts/deleteAlert?id=" + id, function(){
		$("#alertsDiv").load(document.URL + " #alertsDiv");
	});
}
</script>

<script>
function validateType(form)
{
	var house = document.getElementById('house');
	var flat = document.getElementById('flat');
	var neither = document.getElementById('neither');
	var both = document.getElementById('both');
	
	if(house.checked && flat.checked) {
		both.checked = true;
		neither.checked = false;
	}
	else if(!house.checked && !flat.checked) {
		both.checked = false;
		neither.checked = true;
	}
	else {
		both.checked = false;
		neither.checked = false;
	}
}
</script>

<script>
function typeOfAlert(alert) {
	if(alert.getBothHouseAndFlat())
		return "Both"
	else if(alert.getFlat())
		return "Flat"
	else
		return "House"
}	
</script>
	
<script>
	$(document).ready(function() {
		$("#city").autocomplete({
			minLength : 2
		});
		$("#city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		
		var price = document.getElementById('priceInput');
		var radius = document.getElementById('radiusInput');
		//var numberOfRooms = document.getElementById('numberOfRoomsInput');
		//var squareFootage = document.getElementById('squareFootageInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
		//if(numberOfRooms.value == null || numberOfRooms.value == "")
		//	numberOfRooms.value = "3";
		//if(squareFootage.value == null || squareFootage.value == "")
		//	squareFootage.value = "50";
	});
</script>

<h1>Create and manage alerts</h1>
<hr />

<h2>Create new alert</h2><br />
<form:form method="post" modelAttribute="alertForm" action="/profile/alerts"
	id="alertForm" autocomplete="off" class="form">

	<fieldset>
		<form:checkbox name="house" id="house" path="house" /><label>House</label>
		<form:checkbox name="flat" id="flat" path="flat" /><label>Flat</label>
		
		<form:checkbox style="display:none" name="neither" id="neither" path="noHouseNoFlat" />
		<form:checkbox style="display:none" name="both" id="both" path="bothHouseAndFlat" />
		<form:errors path="noHouseNoFlat" cssClass="validationErrorText" /><br />
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" class="form-control" />
		<form:errors path="city" cssClass="validationErrorText" />
		
		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" class="form-control" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		
		<br /> <label for="price">Price (max.):</label>
		<form:input id="priceInput" type="number" path="price"
			placeholder="e.g. 5" step="50" class="form-control" />
		CHF
		<form:errors path="price" cssClass="validationErrorText" />
		
		<br /> <label for="numberOfRooms">Number of Rooms (min.):</label>
		<form:input id="numberOfRoomsInput" type="number" path="numberOfRooms"
			placeholder="e.g. 3" step="1" />
		<form:errors path="numberOfRooms" cssClass="validationErrorText" />
		
		<br /> <label for="squareFootage">Square Meters (min.):</label>
		<form:input id="squareFootageInput" type="number" path="squareFootage"
			placeholder="e.g. 50" step="10" />
		m²
		<form:errors path="price" cssClass="validationErrorText" />
		<br />

		<button type="submit" class="btn btn-default" tabindex="7" onClick="validateType(this.form)">Subscribe</button>
		<button type="reset" class="btn btn-default" tabindex="8">Cancel</button>
	</fieldset>

</form:form> <br />
<h2>Your active alerts</h2>

<div id="alertsDiv" class="alertsDiv">			
<c:choose>
	<c:when test="${empty alerts}">
		<p>You currently aren't subscribed to any alerts.
	</c:when>
	<c:otherwise>
		<table class="styledTable table" id="alerts">
			<thead>
			<tr>
				<th>Type</th>
				<th>City</th>
				<th>Radius</th>
				<th>max. Price</th>
				<th>Action</th>
			</tr>
			</thead>
		<c:forEach var="alert" items="${alerts}">
			<tr>
				<td>
				<c:choose>
					<c:when test="${alert.bothHouseAndFlat}">
						Both
					</c:when>
					<c:when test="${alert.flat}">
						Flat
					</c:when>
					<c:otherwise>
						House
					</c:otherwise>
				</c:choose>
				</td>
				<td>${alert.city}</td>
				<td>${alert.radius} km</td>
				<td>${alert.price} Chf</td>
				<td><button class="deleteButton btn btn-default" data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button></td>
			</tr>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>

<c:import url="template/footer.jsp" />