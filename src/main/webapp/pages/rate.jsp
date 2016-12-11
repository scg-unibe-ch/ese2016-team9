<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>a-Bec</title>
    
    
</head>
<body>
<script>
	$(document).ready(function() {
	});
</script>
<pre>Rate</pre>
    
    <form class="form-horizontal" method="post">
        <div class="row">
            <div class="col-sm-5">
                <button type="submit" name="rating" value="1" class="btn btn-default">
                    <img src="/img/thumbs-up.png" style="width:100%" />
                </button>
            </div>
            <div class="col-sm-5">
                <button type="submit" name="rating" value="0" class="btn btn-default">
                    <img src="/img/thumbs-down.png" style="width:100%" />
                </button>
            </div>
        </div>
    </form>
<c:import url="template/footer.jsp" /><br />