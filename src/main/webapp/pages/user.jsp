<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>


<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("#newMsg").click(function() {
			$("#content").children().animate({
				opacity : 0.4
			}, 300, function() {
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});

		$("#messageCancel").click(function() {
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({
				opacity : 1
			}, 300);
		});

		$("#messageSend").click(function() {
			if ($("#msgSubject").val() != "" && $("#msgTextarea").val() != "") {
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${user.username}";
				$.post("profile/messages/sendMessage", {
					subject : subject,
					text : text,
					recipientEmail : recipientEmail
				}, function() {
					$("#msgDiv").css("display", "none");
					$("#msgDiv").css("opacity", "0");
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$("#content").children().animate({
						opacity : 1
					}, 300);
				})
			}
		});
	});
</script>

<pre><a href="/">Home</a> &gt; Profile</pre>

<div id="userDiv">
	<c:choose>
		<c:when test="${user.picture.filePath != null}">
			<img src="${user.picture.filePath}">
		</c:when>
		<c:otherwise>
			<img src="/img/avatar.png">
		</c:otherwise>
	</c:choose>
	<p>
        
	<h2>${user.email}</h2>
	<h2>Name</h2>
    <p>
        ${user.firstName} ${user.lastName}
	</p>
    
    
    
    
	<h2>About me</h2>
    
    <p>${user.aboutMe}</p>
	
    <c:choose>
        <c:when test="${principalID eq user.id && user.isPremium()}">
            <img src="/img/premium.png" />
            <p>You are marvelous and have bought a premium account!</p>
        </c:when>
        <c:otherwise></c:otherwise>
    </c:choose>
    
    <div>
        <table class="table">
            <tr>
                <td><img src="/img/thumbs-up.png" style="width:15px" /></td>
                <td>${user.likes}</td>
            </tr>
            <tr>
                <td><img src="/img/thumbs-down.png" style="width:15px" /></td>
                <td>${user.dislikes}</td>
            </tr>
        </table>
    </div>
    
	<form>
		<c:choose>
			<c:when test="${principalID != null}">
				<button id="newMsg" class="btn btn-default" type="button">Message</button>
				<c:choose>
					<c:when test="${principalID eq user.id}">
						<a class="btn btn-default" href="/profile/editProfile">Edit Profile</a>
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>

			</c:when>
			<c:otherwise>
				<p>Please log in to contact this person.</p>
			</c:otherwise>
		</c:choose>
	</form>
</div>
<div id="msgDiv">
	<form class="msgForm">
		<h2>Message this user</h2>
		<br> <br> <label>Subject: <span>*</span></label> <input
			class="msgInput" type="text" id="msgSubject" placeholder="Subject" />
		<br> <br> <label>Message: </label>
		<textarea id="msgTextarea" placeholder="Message"></textarea>
		<br />
		<button type="button" class="btn btn-default" id="messageSend">Send</button>
		<button type="button" class="btn btn-default" id="messageCancel">Cancel</button>
	</form>
</div>
<c:import url="template/footer.jsp" />