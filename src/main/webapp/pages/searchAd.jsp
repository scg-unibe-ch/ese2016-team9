<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Search</pre>

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
		
		var price = document.getElementById('prizeInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>


<script>
function validateType(form)
{
	var house = document.getElementById('house');
	var flat = document.getElementById('flat');
	var neither = document.getElementById('neither');
	var both = document.getElementById('both');
	var type = document.getElementById('type');
	var filtered = document.getElementById('filtered');
	
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
		type.checked = flat.checked;
	}
	
	var sale = document.getElementById('sale');
	var rent = document.getElementById('rent');
	var neitherS = document.getElementById('neitherS');
	var bothS = document.getElementById('bothS');
	var typeS = document.getElementById('typeS');
	
	if(sale.checked && rent.checked) {
		bothS.checked = true;
		neitherS.checked = false;
	}
	else if(!sale.checked && !rent.checked) {
		bothS.checked = false;
		neitherS.checked = true;
	}
	else {
		bothS.checked = false;
		neitherS.checked = false;
		typeS.checked = sale.checked;
	}
	filtered.checked = false;
}
</script>

<h1>Search for an ad</h1>
<hr />

<form:form method="post" modelAttribute="searchForm" action="/results"
	id="searchForm" autocomplete="off">

	<fieldset>
		<form:checkbox name="house" id="house" path="houseHelper" /><label>House</label>
		<form:checkbox name="flat" id="flat" path="flatHelper" /><label>Flat</label>
		
		<form:checkbox style="display:none" name="neither" id="neither" path="noHouseNoFlat" />
		<form:checkbox style="display:none" name="both" id="both" path="bothHouseAndFlat" />
		<form:checkbox style="display:none" name="type" id="type" path="flat" />
		<form:checkbox style="display:none" name="filtered" id="filtered" path="filtered" />
		<form:errors path="noHouseNoFlat" cssClass="validationErrorText" />
		
		<br />
		
		<form:checkbox name="sale" id="sale" path="saleHelper" /><label>Buy</label>
		<form:checkbox name="rent" id="rent" path="rentHelper" /><label>Rent</label>
		
		<form:checkbox style="display:none" name="neitherS" id="neitherS" path="noSellNoRent" />
		<form:checkbox style="display:none" name="bothS" id="bothS" path="bothSellAndRent" />
		<form:checkbox style="display:none" name="typeS" id="typeS" path="forSale" />
		<form:errors path="noSellNoRent" cssClass="validationErrorText" />
		
		<br />
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" />
		

		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" />
		
		<br />
		<form:checkbox id="field-includeRunningCosts" path="includeRunningCosts" value="1" /><label>Running Costs included?</label>
		<br />

		<button type="submit" tabindex="7" onClick="validateType(this.form)">Search</button>
		<button type="reset" tabindex="8">Cancel</button>
	</fieldset>

</form:form>

<c:import url="template/footer.jsp" />