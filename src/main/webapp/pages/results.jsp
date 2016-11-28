<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:import url="template/header.jsp" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
$( function() {
  $( "#tabs" ).tabs();
} );
</script>
  
<pre><a href="/">Home</a>   &gt;   <a href="/searchAd/">Search</a>   &gt;   Results</pre>

<script>
$(document).ready(function(){
	$(".slider").mousemove(function(){
		var newValue = $(this).find(".distance-slider").val();
		if(newValue == "5100")
			$(this).find(".range").html(">5km");
		else
			$(this).find(".range").html(newValue + "m");
	});
});
function validateType(form)
{
	var house = document.getElementById('house');
	var flat = document.getElementById('flat');
	var both = document.getElementById('both');
	var type = document.getElementById('type');
	var filtered = document.getElementById('filtered');
	
	if(house.checked && flat.checked) {
		both.checked = true;
	}
	else if(!house.checked && !flat.checked) {
		both.checked = true;
	}
	else {
		both.checked = false;
		neither.checked = false;
		type.checked = flat.checked;
	}
	
	filtered.checked = true;
}
</script>

<script>
/*
 * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
 * No arguments need to be passed, since the function simply looks up the dropdown selection.
 */
function sort_div_attribute() {
    //determine sort modus (by which attribute, asc/desc)
    var sortmode = $('#modus').find(":selected").val();   
    
    //only start the process if a modus has been selected
    if(sortmode.length > 0) {
    	var attname;
		
    	//determine which variable we pass to the sort function
		if(sortmode == "price_asc" || sortmode == "price_desc")
			attname = 'data-price';
	    else if(sortmode == "moveIn_asc" || sortmode == "moveIn_desc")	
			attname = 'data-moveIn';
	    else
			attname = 'data-age';
    	
		//copying divs into an array which we're going to sort
	    var divsbucket = new Array();
	    var divslist = $('div.resultAd');
	    var divlength = divslist.length;
	    for (a = 0; a < divlength; a++) {
			divsbucket[a] = new Array();
			divsbucket[a][0] = divslist[a].getAttribute(attname);
			divsbucket[a][1] = divslist[a];
			divslist[a].remove();
	    }
		
	    //sort the array
		divsbucket.sort(function(a, b) {
	    if (a[0] == b[0])
			return 0;
	    else if (a[0] > b[0])
			return 1;
        else
			return -1;
		});

	    //invert sorted array for certain sort options
		if(sortmode == "price_desc" || sortmode == "moveIn_asc" || sortmode == "dateAge_asc")
			divsbucket.reverse();
        
	    //insert sorted divs into document again
		for(a = 0; a < divlength; a++)
        	$("#resultsDiv").append($(divsbucket[a][1]));
	}
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
		
		$("#field-earliestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
	});
</script>

<!--  This script is for the map -->
<script>
var map;

function initMap() {
  var longitude = parseFloat(document.getElementById("longitude").value);
  var latitude = parseFloat(document.getElementById("latitude").value);
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: latitude, lng: longitude},
    zoom: 13,
  });
  
  function makeInfoWindowEvent(infowindow, marker){
	  marker.addListener('click', function() {
  	    infowindow.open(map, marker);
  	  });
  }
  
  function makeMarkersAndInfoWindows(object){
	  var title = object.querySelector("#adTitle").innerHTML;
	  var link = object.querySelector("#adTitle").href;
	  var address = object.querySelector(".adAddress").innerHTML;
	  var prize = object.querySelector(".adPrize").innerHTML;
	  var moveInDate = object.querySelector(".adMoveInDate").innerHTML;
	  var source = object.querySelector(".adImage").src;
	  
	  var content = "<style> img{ width: 200px; height: auto;} .prize{ font-size: 20px;}</style>"+
	  		"<img src='" + source +"'></img>"+
		  	"<a href='"+link+"'><h2>"+title+"</h2></a>"+
		  	"<p>"+address+"</p>"+
		  	"<p class='prize'><b>"+ prize +"</b></p>"+
		  	"<span>"+moveInDate+"</span>"
	  				
	  
  
	  var geocoder = new google.maps.Geocoder();
	
	  geocoder.geocode( { 'address' : address }, function( results, status ) {
	  	if( status == google.maps.GeocoderStatus.OK ) {
	  		
	  	  var infowindow = new google.maps.InfoWindow({
	  	    content: content
	  	  });
	
	      marker = new google.maps.Marker( {
	    	title: title,
	        map     : map,
	        position: results[0].geometry.location,
	      } );
	      
	      makeInfoWindowEvent(infowindow, marker);
	      
	    }
	  } );	  
  }

  var objects = document.getElementsByClassName("resultAd");
  
  for(var i = 0; i < objects.length; i++) {
	  var object = objects.item(i);
	  makeMarkersAndInfoWindows(object);
  }
}

$(document).ready(function(){
	$('a[href="#result-map"]').on('click', function() {
	    setTimeout(function(){
	    	var currCenter = map.getCenter();
	        google.maps.event.trigger(map, 'resize');
	        map.setCenter(currCenter);
	    }, 50);
	});
});
</script>

<h1>Search results:</h1>

<hr />

<div>
<select id="modus">
    <option value="">Sort by:</option>
    <option value="price_asc">Price (ascending)</option>
    <option value="price_desc">Price (descending)</option>
    <option value="moveIn_desc">Move-in date (earliest to latest)</option>
    <option value="moveIn_asc">Move-in date (latest to earliest)</option>
    <option value="dateAge_asc">Date created (youngest to oldest)</option>
    <option value="dateAge_desc">Date created (oldest to youngest)</option>
</select>

<button onClick="sort_div_attribute()">Sort</button>	
</div>

<form:form method="post" modelAttribute="searchForm" action="/results" id="filterForm" autocomplete="off" class="form-horizontal">

	<div id="filterDiv">
		<h2>Filter results:</h2>
		
		<form:radiobutton name="sale" path="forSale" value="false" checked="checked" /><label>Rent</label>
		<form:radiobutton name="sale" path="forSale" value="true" /><label>Buy</label> <br/>
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" /><br />
		
		<form:input style="display:none" type="number" id="latitude" path="latitude" step="any"/>
		<form:input style="display:none" type="number" id="longitude" path="longitude" step="any"/>
			
		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" /><br />
		
		<br /><form:checkbox id="field-includeRunningCosts" path="includeRunningCosts" value="1" /><label>Running Costs included?</label>
		
		<hr class="slim">		
		
		<table style="width: 80%">
			<tr>
				<td><form:checkbox name="house" id="house" path="houseHelper" checked="checked"/><label>House</label></td>
				<td><form:checkbox name="flat" id="flat" path="flatHelper" checked="checked"/><label>Flat</label></td>
	
				<form:checkbox style="display:none" name="both" id="both" path="bothHouseAndFlat" />
				<form:checkbox style="display:none" name="type" id="type" path="flat" />
				<form:checkbox style="display:none" name="filtered" id="filtered" path="filtered" />
				<form:errors path="noHouseNoFlat" cssClass="validationErrorText" /> <br />
			</tr>
			<tr>
				<td><label for="numberOfRooms">Number Of Rooms (min.):</label></td>
				<td><form:input id="numberOfRoomsInput" type="number" path="numberOfRooms"
					placeholder="e.g. 5" step="1" />
				</td>
			</tr>
			<tr>
				<td><label for="squareFootage">Square Meters (min.):</label></td>
				<td><form:input id="squareFootageInput" type="number" path="squareFootage"
					placeholder="e.g. 5" step="5" />m²
				</td>
			</tr>
			<tr>
				<td><label for="distanceToNearestPublicTransport">Distance to nearest public transport (max.):</label></td>
				<td class="slider"><form:input id="distanceToNearestPublicTransportInput" class ="distance-slider" type="range" path="distanceToNearestPublicTransport"
					min="100" max="5100" value="5100" step="100" /><span class="range">>5km</span>
				</td>
			</tr>
			<tr>
				<td><label for="distanceToNearestSuperMarket">Distance to nearest super market (max.):</label></td>
				<td class="slider"><form:input id="distanceToNearestSuperMarketInput" class ="distance-slider" type="range" path="distanceToNearestSuperMarket"
					min="100" max="5100" value="5100" step="100" /><span class="range">>5km</span>
				</td>
			</tr>
			<tr>
				<td><label for="distanceToNearestSchool">Distance to nearest school (max.):</label></td>
				<td class="slider"><form:input id="distanceToNearestSchoolInput" class ="distance-slider" type="range" path="distanceToNearestSchool"
					min="100" max="5100" value="5100" step="100" /><span class="range">>5km</span>
				</td>
			</tr>
			<tr>
				<td><label for="earliestMoveInDate">Earliest move-in date</label></td>
				<td><label for="latestMoveInDate">Latest move-in date</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-earliestMoveInDate"
						path="earliestMoveInDate" /></td>
				<td><form:input type="text" id="field-latestMoveInDate"
						path="latestMoveInDate" /></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking inside
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Animals
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
		</table>
			
		
		<button type="submit" class="btn btn-default" onClick="validateType(this.form)">Filter</button>	
		<button type="reset" class="btn btn-default">Cancel</button>
	</div>
</form:form>

<div class="searchResultsDiv" id="tabs">
<ul>
  <li><a href="#result-list">List</a></li>
  <li><a href="#result-map">Map</a></li>
</ul>
<div id="result-list">
<c:choose>
	<c:when test="${empty results}">
		<p>No results found!
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">			
			<c:forEach var="ad" items="${results}">
				<div class="resultAd" data-price="${ad.prize}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" class="adImage"/></a>
						<h2>
							<a class="link adTitle" id="adTitle" href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p class="adAddress">${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i><c:choose>
									<c:when test="${ad.flat}">Flat</c:when>
									<c:otherwise>House</c:otherwise>
								</c:choose></i>
						</p>
					</div>
					<div class="resultRight">
						<h2 class="adPrize">CHF ${ad.prize }</h2>
						<br /> 
						<p>
							<i><c:choose>
								<c:when test="${ad.forSale}">for Sale</c:when>
								<c:otherwise>for Rent</c:otherwise>
							   </c:choose></i>
						</p>
						

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<p class="adMoveInDate">Move-in date: ${formattedMoveInDate }</p>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
</div>

<div id="result-map">
<div id="map"></div>
</div>
</div>

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyB5Hs4TYmXUiMKPetPokSZrUSMnev58Fm8&callback=initMap"
  type="text/javascript"></script>
<c:import url="template/footer.jsp" />