<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>


<pre><a href="/">Home</a>   &gt;   My Bookmarks</pre>

<c:choose>
	<c:when test="${empty bookmarkedAdvertisements}">
		<h1>My Bookmarks</h1>
		<hr />
		<p>You have not bookmarked anything yet.</p><br /><br />
	</c:when>
	<c:otherwise>
		
		<div id="resultsDiv" class="resultsDiv row">
		<h1>My Bookmarks</h1>
		<hr />			
			<c:forEach var="ad" items="${bookmarkedAdvertisements}">
				<div class="resultAd col-md-3" data-price="${ad.price}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i><c:choose>
									<c:when test="${ad.flat}">Flat</c:when>
									<c:otherwise>House</c:otherwise>
								</c:choose></i>
						</p>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.price }</h2>
						<br /> <br />
						<p>Available From: ${ad.moveInDate }</p>
					</div>
				</div>
			</c:forEach>
		</div>		
	</c:otherwise>
</c:choose>

<c:import url="template/footer.jsp" />