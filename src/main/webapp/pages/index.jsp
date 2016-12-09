<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to a-Bec!</title>
</head>
<body>

<pre>Home</pre>


    
<c:choose>
	<c:when test="${empty newest}">
        <div class="container-fluid">
            <h1>Welcome to a-Bec!<small>No ads placed yet</small></h1>
		</div>
        
	</c:when>
	<c:otherwise>
        <div class="container-fluid">
            <h1>Welcome to a-Bec! <small>See our featured ads below</small></h1>
        </div>
        
        <div class="container-fluid">   
            <div id="resultsDiv" class="resultsDiv row">		
                <c:forEach var="ad" items="${newest}">
                    <div class="resultAd col-md-3">
                        <c:set var="ad" value="${ad}" scope="request" />
                        <jsp:include page="adDisplay.jsp" />
                    </div>
                </c:forEach>
            </div>
        </div>
	</c:otherwise>
</c:choose>
<c:import url="template/footer.jsp" /><br />