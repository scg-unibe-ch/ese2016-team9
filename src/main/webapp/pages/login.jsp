<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<sec:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />
<meta name="google-signin-client_id" content="601046239237-r1etvc978bl3evkdlp9k4blmtpp210i0.apps.googleusercontent.com">
<script src="https://apis.google.com/js/platform.js" async defer></script>


<script>
function onSignIn(googleUser) {
	  var profile = googleUser.getBasicProfile();
	  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
	  console.log('Name: ' + profile.getName());
	  console.log('Image URL: ' + profile.getImageUrl());
	  console.log('Email: ' + profile.getEmail());
	  console.log("Id-Token: " + googleUser.getAuthResponse().id_token);
	  
	  if(document.getElementById("googleFlag").checked){
		  document.getElementById("field-password").value = profile.getId();
		  document.getElementById("field-email").value = profile.getEmail();
		  document.getElementById("login-form").submit();
	  } else {
		  document.getElementById("form-firstName").value = profile.getName().split(" ")[0];
		  document.getElementById("form-lastName").value = profile.getName().split(" ")[1];
		  document.getElementById("form-password").value = profile.getId();
		  document.getElementById("form-email").value = profile.getEmail();
		  document.getElementById("gender").selectedIndex = "0"; 
		  
		  document.getElementById("signupForm").submit();
	  }
	}
</script>

<pre><a href="/">Home</a> &gt; Login</pre>

<h1>Login</h1>

<c:choose>
	<c:when test="${loggedIn}">
		<p>You are already logged in!</p>
	</c:when>
	<c:otherwise>
		<c:if test="${!empty param.error}">
			<p>Incorrect email or password. Please retry using correct email and password.</p>
			<br />
		</c:if>
		
		<form id="login-form" name="f" method="post" action="/login" class="form-inline">
            <div class="form-group">
                <label class="sr-only" for="field-email">Email:</label> <input name="username" id="field-email" placeholder="Email" class="form-control" /> 
                <label class="sr-only" for="field-password">Password:</label> <input name="password" id="field-password" type="password" placeholder="Password" class="form-control" />
                <button type="submit" class="btn btn-default">Login</button>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </div>
		</form>
		<br />
		<div class="g-signin2" data-onsuccess="onSignIn"></div>
		<br />
		<h2>Test users</h2>

		<ul class="test-users">
			<li>Email: <i>ese@unibe.ch</i>, password: <i>ese</i></li>
			<li>Email: <i>jane@doe.com</i>, password: <i>password</i></li>
			<li>Email: <i>user@bern.com</i>, password: <i>password</i></li>
			<li>Email: <i>oprah@winfrey.com</i>, password: <i>password</i></li>
		</ul>
		<br />

		<h2>Dudes for AdBern</h2>
		<ul class="test-users">
			<li>Email: <i>hans@unibe.ch</i>, password: <i>password</i></li>
			<li>Email: <i>mathilda@unibe.ch</i>, password: <i>password</i></li>
		</ul>
		<br />
		
			Or <a class="link" href="<c:url value="/signup" />">sign up</a> as a new user.
		
	</c:otherwise>
</c:choose>

<form:form id="signupForm" method="post" modelAttribute="signupForm" action="google-signup" class="form-horizontal" style="display: none">
				<form:checkbox path="googleFlag" id="googleFlag" />
                <form:input path="firstName" id="form-firstName" class="form-control"/>
                <form:input path="lastName" id="form-lastName" class="form-control"/>
                <form:input path="password" id="form-password" class="form-control"/>
                <form:input path="email" id="form-email" class="form-control"/>
                <form:select path="gender" class="form-control">
                    <form:option value="FEMALE" label="Female" />
                    <form:option value="MALE" label="Male" />
                </form:select>
</form:form>

<c:import url="template/footer.jsp" />