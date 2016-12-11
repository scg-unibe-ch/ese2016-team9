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


<pre><a href="/">Home</a> &gt; My Houses</pre>

    <h1>My Advertisements</h1>
    
<c:choose>
	<c:when test="${empty ownAdvertisements}">
		<p>You have not advertised anything yet.</p>
	</c:when>
	<c:otherwise>
		<br /><br />
	
        <div class="container-fluid">
            <div id="resultsDiv" class="resultsDiv row">
            
                <c:forEach var="ad" items="${ownAdvertisements}">
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