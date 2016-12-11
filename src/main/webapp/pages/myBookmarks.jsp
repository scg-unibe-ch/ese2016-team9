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


<pre><a href="/">Home</a> &gt; My Bookmarks</pre>
<h1>My Bookmarks</h1>
<c:choose>
	<c:when test="${empty bookmarkedAdvertisements}">
		<p>You have not bookmarked anything yet.</p>
	</c:when>
	<c:otherwise>
	
        <div class="container-fluid">
            <div id="resultsDiv" class="resultsDiv row">
            
                <c:forEach var="ad" items="${bookmarkedAdvertisements}">
                    <div class="resultAd col-sm-4 col-xs-12 col-md-4">
                        <c:set var="ad" value="${ad}" scope="request" />
                        <jsp:include page="adDisplay.jsp" />
                    </div>
                </c:forEach>
                <br /> <br />
            </div>		
        </div>
	</c:otherwise>
</c:choose>

<c:import url="template/footer.jsp" />