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
					<div class="resultLeft">
                        <div class="resultImage">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
                        
                        </div>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.price }</h2>
						<br /> <br />

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<p>Available From: ${formattedMoveInDate }</p>
					</div>
					<div class="resultBottom">
						<h2>
							<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
						</h2>
						<p class="resultAddress">${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<p>
							<i><c:choose>
									<c:when test="${ad.flat}">Flat</c:when>
									<c:otherwise>House</c:otherwise>
								</c:choose></i>
						</p>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
</div>
<c:import url="template/footer.jsp" /><br />