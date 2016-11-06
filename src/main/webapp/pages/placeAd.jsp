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

<pre><a href="/">Home</a> &gt; Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form class="form-horizontal" method="post" modelAttribute="placeAdForm" action="/profile/placeAd" id="placeAdForm" autocomplete="off" enctype="multipart/form-data">

	<fieldset>
		<legend>General info</legend>
        
        <div class="form-group">
            <label for="field-title" class="col-sm-2 control-label">Ad Title</label>
            <div class="col-sm-10">
                <form:input id="field-title" path="title" placeholder="Ad Title" class="form-control"/>
            </div>
        </div>
        
        <div class="form-group">
            <label for="type-house" class="col-sm-2 control-label">Type:</label>
            <div class="col-sm-10">
                <form:radiobutton id="type-house" path="flat" value="0" checked="checked" />House 
                <form:radiobutton id="type-flat" path="flat" value="1" />Flat
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-street" class="col-sm-2 control-label">Street</label>
            <div class="col-sm-10">
                <form:input id="field-street" path="street" placeholder="Street" class="form-control" />
            </div>
        </div>
			
        <div class="form-group">
            <label for="field-city" class="col-sm-2 control-label">City / Zip code</label>
            <div class="col-sm-10">
                <form:input id="field-city" path="city" placeholder="City" class="form-control" />
                <form:errors path="city" cssClass="validationErrorText" />
            </div>
        </div>
				
        <div class="form-group">
            <label for="moveInDate" class="col-sm-2 control-label">Available from</label>
            <div class="col-sm-10">
                <form:input type="text" id="field-moveInDate" path="moveInDate" class="form-control" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-Prize" class="col-sm-2 control-label">${isRentingAd ? "Prize per Month" : "Prize"}</label>
            <div class="col-sm-10">
                <form:input id="field-Prize" type="number" path="prize" placeholder="Prize per month" step="50" class="form-control" />   
                <form:errors path="prize" cssClass="validationErrorText" /> 
            </div>
        </div>
        
        
        <c:choose>
            <c:when test="${isRentingAd}"></c:when>
            <c:otherwise>
                <div class="form-group">
                    <label for="field-RunningCosts" class="col-sm-2 control-label">Running costs</label>
                    <div class="col-sm-10">
                        <form:input id="field-RunningCosts" type="number" path="runningCosts" placeholder="Running costs" step="50" class="form-control" />   
                        <form:errors path="runningCosts" cssClass="validationErrorText" /> 
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="form-group">
            <label for="field-SquareFootage" class="col-sm-2 control-label">Square Meters</label>
            <div class="col-sm-10">
                <form:input id="field-SquareFootage" type="number" path="squareFootage" placeholder="Prize per month" step="5" class="form-control"  /> 
                <form:errors path="squareFootage" cssClass="validationErrorText" />
            </div>
        </div>
	</fieldset>

	<fieldset>
		<legend>House Description</legend>

        <div class="form-group">
            <label for="lastRenovation" class="col-sm-2 control-label">Last Renovation (Optional)</label>
            <div class="col-sm-10">
                <form:input id="field-lastRenovationDate" path="lastRenovation" class="form-control"  /> 
                <form:errors path="lastRenovation" cssClass="validationErrorText" />
            </div>
        </div>
    
        
        <div class="form-group">
            <label for="field-Floor" class="col-sm-2 control-label">Floor</label>
            <div class="col-sm-10">
                <form:input id="field-Floor" class="form-control" type="number" path="floor" placeholder="e.g. -1" step="1" /> 
                <form:errors path="floor" cssClass="validationErrorText" />
            </div>
        </div>
    
        
        <div class="form-group">
            <label for="field-NumberOfRooms" class="col-sm-2 control-label">Number of Rooms</label>
            <div class="col-sm-10">
                <form:input id="field-NumberOfRooms" class="form-control" type="number" path="numberOfRooms" placeholder="e.g. 2" step="1" /> 
                <form:errors path="numberOfRooms" cssClass="validationErrorText" />
            </div>
        </div>
        
        <div class="form-group slider">
            <label for="field-DistanceToNearestSuperMarket" class="col-sm-2 control-label">Distance to nearest Super Market</label>
            <div class="col-sm-10">
                <form:input id="field-DistanceToNearestSuperMarket" type="range" path="distanceToNearestSuperMarket" class="form-control distance-slider"
                min="100" max="5100" step="100" value="500" />
						<span class="range">500m</span>
            </div>
        </div>
        
        
        <div class="form-group slider">
            <label for="field-DistanceToNearestPublicTransport" class="col-sm-2 control-label">Distance to nearest Public Transport</label>
            <div class="col-sm-10">
                <form:input id="field-DistanceToNearestPublicTransport" type="range" path="distanceToNearestPublicTransport" class="form-control distance-slider"
                min="100" max="5100" step="100" value="500" />
						<span class="range">500m</span> 
            </div>
        </div>
        
        
        <div class="form-group slider">
            <label for="field-DistanceToNearestSchool" class="col-sm-2 control-label">Distance to nearest School</label>
            <div class="col-sm-10">
                <form:input id="field-DistanceToNearestSchool" type="range" path="DistanceToNearestSchool" class="form-control distance-slider"
                min="100" max="5100" step="100" value="500" />
						<span class="range">500m</span> 
            </div>
        </div>
    
        <div class="form-group">
            <label class="checkbox-inline col-sm-3" style="display:${isRentingAd ? "" : "none"}">
                <div class="">
                    <form:checkbox id="field-smoker" path="smokers" value="1" />Animals allowed
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3" style="display:${isRentingAd ? "" : "none"}">
                <div class="">
                    <form:checkbox id="field-animals" path="animals" value="1" />Smoking inside allowed
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-garden" path="garden" value="1" />Garden (co-use)
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-balcony" path="balcony" value="1" />Balcony or Patio
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-cellar" path="cellar" value="1" />Cellar or Attic
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-furnished" path="furnished" value="1" />Furnished
                </div>
            </label>
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-cable" path="cable" value="1" />Cable TV
                </div>
            </label> 
            
            <label class="checkbox-inline col-sm-3">
                <div class="">
                    <form:checkbox id="field-garage" path="garage" value="1" />Garage
                </div>
            </label>
        </div>
        <div class="form-group">
            <form:textarea path="houseDescription" rows="10" cols="100" placeholder="House Description" class="form-control" />
            <form:errors path="houseDescription" cssClass="validationErrorText" />
        </div>
                <form:errors path="distanceToNearestSuperMarket" cssClass="validationErrorText" />
                <form:input id="field-DistanceToNearestSuperMarket" type="number" path="distanceToNearestSuperMarket" step="1" class="form-control"  /> 
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
        <div class="form-group">
            <label class="col-sm-2 control-label" for="field-pictures">Pictures</label> 
            <div class="col-sm-10">
                <input type="file" id="field-pictures" accept="image/*" multiple="multiple" />
                <table id="uploaded-pictures" class="table">
                    <tr>
                        <th id="name-column">Uploaded picture</th>
                        <th>Size</th>
                        <th>Delete</th>
                    </tr>
                </table>
            </div>
        </div>
	</fieldset>
	
	<fieldset>
		<legend>Visiting times (optional)</legend>
        <div class="form-group">
            <label for="field-title" class="col-sm-2 control-label">Date</label>
            <div class="col-sm-10">
                <input type="text" id="field-visitDay" class="form-control"/>
            </div>
        </div>

        <div class="form-group">
            <label for="startHour" class="col-sm-2 control-label">From</label>
            <div class="col-sm-10">
                <select id="startHour">
                    <%
                        for (int i = 0; i < 24; i++) {
                                String hour = String.format("%02d", i);
                                out.print("<option value=\"" + hour + "\">" + hour
                                        + "</option>");
                            }
                    %>
				</select>
                <select id="startMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>
            </div>
        </div>
        
        
        <div class="form-group">
            <label for="endHour" class="col-sm-2 control-label">To</label>
            <div class="col-sm-10">
                <select id="endHour">
                    <%
                        for (int i = 0; i < 24; i++) {
                                String hour = String.format("%02d", i);
                                out.print("<option value=\"" + hour + "\">" + hour
                                        + "</option>");
                            }
                    %>
				</select>
                <select id="endMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>
            </div>


        </div>
        
        <div class="form-group">
            <label for="addVisitButtonButton" class="col-sm-2 control-label">Add timeslot</label>
            <div class="col-sm-10">
                <button type="button" id="addVisitButton" class="btn btn-default">+</button>
            </div>
        </div>
        <div id="addedVisits"></div>
        
	</fieldset>


	
	<div>
		<button type="submit" class="btn btn-default">Submit</button>
		<a href="/"><button type="button" class="btn btn-default">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
