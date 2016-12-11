<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    
<div class="container-fluid resultAdBorder">
    <div class="row">
        <div class="col-sm-6">
            <div class="resultImage">
                <a href="<c:url value='/ad?id=${ad.id}' />">
                    <c:choose>
                        <c:when test="${ad.pictures.size() == 0}">

                            <img src="/img/house-placeholder.png" />
                        </c:when>
                        <c:otherwise>
                            <img src="${ad.pictures[0].filePath}" />
                        </c:otherwise>
                    </c:choose>            
                    <c:choose>
                        <c:when test="${ad.isAuction()}">
                            <img src="/img/auction-icon.png" class="imtip" />
                        </c:when>
                        <c:otherwise>
                        <c:choose>
                            <c:when test="${ad.getForSale()}">
                            <img src="/img/forsale-icon.png" class="imtip" />
                            </c:when>    
                        </c:choose>
                        </c:otherwise>
                    </c:choose>

                </a>



            </div>

        </div>
        <div class="col-sm-6">
            <fmt:formatNumber type="number" minFractionDigits="2" value="${ad.price}" var="formattedPrice" />
            <div class="container-fluid">
                    <h2><small>CHF</small> ${formattedPrice}</h2>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="resultBottom">
            <h2>
                <a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
            </h2>
            <p class="resultAddress">${ad.street}, ${ad.zipcode} ${ad.city}</p>     
            <c:choose>
                <c:when test="${ad.isAuction()}">
                    <c:choose>
                        <c:when test="${!ad.isAuctionEnded()}">
                            <fmt:formatDate value="${ad.auctionEndingDate}" var="formattedAuctionEndingDate" type="date" pattern="MM/dd/yyyy HH:mm:ss" />
                            <div style="display: inline">Auction ends in</div>
                            <div style="display: inline" class="auctionTimerDisplay">${formattedAuctionEndingDate}</div>
                            <c:choose>
                                <c:when test="${not empty user}">
                                    <fmt:formatNumber type="number" minFractionDigits="2" value="${ad.getHighestBet()}" var="formattedBet" />
                                    <c:choose>
                                        <c:when test="${ad.getLastBiddingUser().equals(user)}">
                                            <p style="color:green">You are winning with CHF ${formattedBet}</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p style="color:red">You are losing, highest bet is at CHF ${formattedBet}</p>
                                        </c:otherwise>
                                    </c:choose>


                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatDate value="${ad.auctionEndingDate}" var="formattedAuctionEndingDate" type="date" pattern="MM/dd/yyyy HH:mm:ss" />
                            <p>Auction has ended!</p>
                            <c:choose>
                                <c:when test="${not empty user}">
                                    <fmt:formatNumber type="number" minFractionDigits="2" value="${ad.getHighestBet()}" var="formattedBet" />
                                    <c:choose>
                                        <c:when test="${ad.getLastBiddingUser().equals(user)}">
                                            <p style="color:green">You have won! Please pay CHF ${formattedBet}</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p style="color:red">You have lost</p>
                                        </c:otherwise>
                                    </c:choose>


                                </c:when>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    
                </c:when>
            </c:choose>

            <fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate" type="date" pattern="dd.MM.yyyy" />

            <p>Available From: ${formattedMoveInDate }</p>
            <p>
                <i>
                    <c:choose>
                        <c:when test="${ad.flat}">Flat</c:when>
                        <c:otherwise>House</c:otherwise>
                    </c:choose>
                </i>
            </p>
        </div>
    
    </div>
    
</div>    