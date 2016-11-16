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

<c:import url="template/footer.jsp" />