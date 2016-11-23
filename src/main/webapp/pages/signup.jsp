<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script>
	// Validate the email field
	$(document).ready(function() {
		$("#field-email").focusout(function() {
			var text = $(this).val();
			$.post("/signup/doesEmailExist", {email: text}, function(data){
				if(data){
					alert("This username is taken. Please choose another one!");
					$("#field-email").val("");
				}
			});
		});
	});
</script>

<pre><a href="/">Home</a> &gt; Sign up</pre>

<h1>Sign up</h1>
<form:form id="signupForm" method="post" modelAttribute="signupForm" action="signup" class="form-horizontal">
	<fieldset>
		<legend>Enter Your Information</legend>

        
        <div class="form-group">
            <label for="field-firstName" class="col-sm-2 control-label">First Name</label>
            <div class="col-sm-10">
                <form:input path="firstName" id="field-firstName" class="form-control"/>
                <form:errors path="firstName" cssClass="validationErrorText" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-lastName" class="col-sm-2 control-label">Last Name</label>
            <div class="col-sm-10">
                <form:input path="lastName" id="field-lastName" class="form-control"/>
                <form:errors path="lastName" cssClass="validationErrorText" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-password" class="col-sm-2 control-label">Password</label>
            <div class="col-sm-10">

                <form:input path="password" id="field-password" type="password" class="form-control"/>
                <form:errors path="password" cssClass="validationErrorText" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-email" class="col-sm-2 control-label">Email</label>
            <div class="col-sm-10">
                <form:input path="email" id="field-email" class="form-control"/>
                <form:errors path="email" cssClass="validationErrorText" />
            </div>
        </div>
        
        <div class="form-group">
            <label for="field-gender" class="col-sm-2 control-label">Gender</label>
            <div class="col-sm-10">
                <form:select path="gender" class="form-control">
                    <form:option value="FEMALE" label="Female" />
                    <form:option value="MALE" label="Male" />
                </form:select>
            </div>
        </div>
        
		<button type="submit" class="btn btn-default">Sign up</button>
	</fieldset>
</form:form>

<c:import url="template/footer.jsp" />