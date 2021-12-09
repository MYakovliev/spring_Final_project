<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: nicki
  Date: 3/17/2021
  Time: 12:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}"/>
    <fmt:setBundle basename="locale.locale"/>
    <title><fmt:message key="ban.title"/></title>
    <link rel="stylesheet" href="<c:url value="/css/ban.css"/>" type="text/css"/>
</head>
<body>
<h3><fmt:message key="ban.sorry"/></h3>
<a href="/logout"><fmt:message key="header.sign_out"/></a>
</body>
<style>
    body {
        margin-top: 25%;
        alignment: center;
        background-color: blanchedalmond;
        text-align: center;
    }
</style>
</html>
