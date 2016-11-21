<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("#about-me").val("${currentUser.aboutMe}")
		});		
</script>

<pre><a href="/">Home</a> &gt; <a href="/user?id=${currentUser.id}">Public Profile</a> &gt; Edit profile</pre>

<h1>Edit your Profile</h1>
<hr />

<!-- check if user is logged in -->
<c:set var="loggedIn" value="${pageContext.request.userPrincipal.authenticated}" />
    

<c:choose>
	<c:when test="${loggedIn}">
		<a id="profile_picture_editPage"> <c:import
					url="/pages/getUserPicture.jsp" />
		</a>
	</c:when>
	<c:otherwise>
		<a href="/login">Login</a>
	</c:otherwise>
</c:choose>

<form:form method="post" modelAttribute="editProfileForm" action="/profile/editProfile" id="editProfileForm" autocomplete="off" enctype="multipart/form-data" class="form-horizontal">
    <form:input type="hidden" path="id" value="${currentUser.id}" />
    
    
    <div class="form-group">
        <label for="user-name" class="col-sm-2 control-label">Username:</label>
        <div class="col-sm-10">
            <form:input class="form-control" id="user-name" path="username" value="${currentUser.username}" />
            <form:errors path="username" cssClass="validationErrorText" />
        </div>
    </div>
    
    <div class="form-group">
        <label for="first-name" class="col-sm-2 control-label">First name:</label>
        <div class="col-sm-10">
            <form:input class="form-control" id="first-name" path="firstName" value="${currentUser.firstName}" />
            <form:errors path="firstName" cssClass="validationErrorText" />
        </div>
    </div>
    
    <div class="form-group">
        <label for="last-name" class="col-sm-2 control-label">Last name:</label>
        <div class="col-sm-10">
            <form:input class="form-control" id="last-name" path="lastName" value="${currentUser.lastName}" />
            <form:errors path="lastName" cssClass="validationErrorText" />
        </div>
    </div>
    
    <div class="form-group">
        <label for="password" class="col-sm-2 control-label">Password:</label>
        <div class="col-sm-10">
            <form:input class="form-control" type="password" id="password" path="password" value="${currentUser.password}" />
            <form:errors path="password" cssClass="validationErrorText" />
        </div>
    </div>
    
    <div class="form-group">
        <label for="about-me" class="col-sm-2 control-label">About me:</label>
        <div class="col-sm-10">
            <form:textarea id="about-me" rows="12" path="aboutMe" class="form-control" />
        </div>
    </div>
    
<div>
		<button class="btn btn-default" type="submit">Update</button>
</div>

</form:form>


<c:import url="template/footer.jsp" />

