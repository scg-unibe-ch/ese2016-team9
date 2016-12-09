<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    
    
<div class="resultLeft">
    <div class="resultImage">
        <c:choose>
            <c:when test="${ad.isAuction()}">
                <div class="adIsAuction"></div>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>
        <a href="<c:url value='/ad?id=${ad.id}' />">
            <c:choose>
                <c:when test="${ad.pictures.size() == 0}">
                    
                    <img src="/img/house-placeholder.png" />
                </c:when>
                <c:otherwise>
                    <img src="${ad.pictures[0].filePath}" />
                </c:otherwise>
            </c:choose>            
        </a>
    </div>
</div>
<div class="resultRight">
    <fmt:formatNumber type="number" minFractionDigits="2" value="${ad.price}" var="formattedPrice" />
    <h2>CHF ${formattedPrice}</h2>
    <br /> <br />

    <fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />

    <p>Available From: ${formattedMoveInDate }</p>
</div>
<div class="resultBottom">
    <h2>
        <a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
    </h2>
    <p class="resultAddress">${ad.street}, ${ad.zipcode} ${ad.city}</p>
    <p>
        <i>
            <c:choose>
                <c:when test="${ad.flat}">Flat</c:when>
                <c:otherwise>House</c:otherwise>
            </c:choose>
        </i>
    </p>
</div>