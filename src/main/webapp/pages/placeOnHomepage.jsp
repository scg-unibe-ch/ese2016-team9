<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>The FlatFoundrs</title>
    
    
</head>
<body>
<script>
	$(document).ready(function() {
        $("#field-expirationDate").datepicker({
			dateFormat : 'mm-yy'
		});
	});
</script>
<pre>Place ad on homepage</pre>

    <p align="center">
        Do you wan't to place your ad to the homepage of a-Bec? It costs only <b>10.00.-</b>. Please submit your credit card credentials.
    </p>
    
    <form class="form-horizontal" method="post">
        <div class="form-group">
            <label for="field-creditCardNumber" class="col-sm-3 control-label">Credit Card Number</label>
            <div class="col-sm-9">
                <input id="field-creditCardNumber" type="text" placeholder="Credit Card Number" class="form-control" required="required" /> 
            </div>
        </div>
        <div class="form-group">
            <label for="field-expirationDate" class="col-sm-3 control-label">Expiration Date</label>
            <div class="col-sm-9">
                <input id="field-expirationDate" type="text" class="form-control" required="required" /> 
            </div>
        </div>
        <div class="form-group">
            <label for="field-ccv" class="col-sm-3 control-label">CCV</label>
            <div class="col-sm-9">
                <input id="field-ccv" type="text" placeholder="CCV" class="form-control" required="required" /> 
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-12">
                <button type="submit" class="btn btn-default">Place ad to the homepage</button>
            </div>
        </div>
    </form>
<c:import url="template/footer.jsp" /><br />