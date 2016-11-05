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

<h1>Welcome to a-Bec!</h1>

    <div class="container-fluid">
<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv row">	
			<h2>Our newest ads:</h2>		
			<c:forEach var="ad" items="${newest}">
				<div class="resultAd col-md-3">
					<div class="resultLeft">
                        <div class="resultImage">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
                        
                        </div>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.prize }</h2>
						<br /> <br />

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<p>Move-in date: ${formattedMoveInDate }</p>
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