<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<head>
<link rel="stylesheet" type="text/css" media="screen"
	href="/css/main.css">
<link rel="stylesheet" type="text/css"
	media="only screen and (max-device-width: 480px)"
	href="/css/smartphone.css" />

<Title>FlatFindr</Title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script
	src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<link rel="stylesheet"
	href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />

<script src="/js/unreadMessages.js"></script>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
/* ensure that autocomplete lists are not too long and have a scrollbar */
.ui-autocomplete {
	max-height: 200px;
	overflow-y: auto;
	overflow-x: hidden;
}
</style>

</head>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<!-- check if user has a profile picture -->

<header>
    <nav class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">a-Bec</a>
            </div>


            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        
                <ul class="nav nav-pills">
                    <c:choose>
                        <c:when test="${loggedIn}">
                        <script>
                            $(document).ready(unreadMessages("header"));
                        </script>

                        <!-- include user details -->
                        <%@include file='/pages/getUserPicture.jsp' %>
                            <li role="presentation" class="dropdown" id="profile_picture"><a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                            <% 
                                
                                out.print("" + realUser.getFirstName() + " "
                                    + realUser.getLastName() + ""); 
                            %><span class="caret"></span>
                            </a>
                                <ul class="dropdown-menu">
                                    <li><a href="/profile/placeAd">Place an ad</a></li>
									<li><a href="/profile/placeAd?rent=1">Place a renting ad</a></li>
                                    <li><a href="/profile/myHouses">My houses</a></li>
                                    <li><a id="messageLink" href="/profile/messages"></a></li>
                                    <li><a href="/profile/enquiries">Enquiries</a></li>
                                    <li><a href="/profile/schedule">Schedule</a></li>
                                    <li><a href="/profile/alerts">Alerts</a></li>
                                    <li>
                                    <% out.print("<a href=\"/user?id=" + realUser.getId() + "\">Public Profile</a>"); %>
                                    </li>
                                    <li role="presentation"><a href="/logout">Logout</a></li>
                                </ul></li>
                        </c:when>
                        <c:otherwise>
                            <li role="presentation"><a href="/login">Login</a></li>
                        </c:otherwise>
                    </c:choose>
				    <li><a href="<c:url value='/searchAd' />">Search</a></li>
                </ul>
            </div>
		
	   </div>
        </nav>
</header>

<body>
	<!-- will be closed in footer-->
	<div id="content">

		<c:if test="${not empty confirmationMessage }">
			<div class="confirmation-message">
				<img src="/img/check-mark.png" />
				<p>${confirmationMessage }</p>
			</div>
		</c:if>