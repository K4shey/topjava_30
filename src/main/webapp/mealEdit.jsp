<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Meal</title>

</head>
<body>

<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal edit</h2>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<form method="POST" action='meals' name="frmAddMeal">
    <dl>
        <input type="hidden" name="id" value="<c:out value="${meal.id}" />"/>
        <dt>DateTime :</dt>
        <dd><input required
                   type="datetime-local" name="dateTime"
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"/>
                <fmt:formatDate value="${parsedDateTime}" pattern="yyyy-MM-dd HH:mm" var="formattedDateTime"/>
                   value="<c:out value="${formattedDateTime}" />"/></dd>
        <dt>Description :</dt>
        <dd><input required type="text" name="description" value="<c:out value="${meal.description}" />"/></dd>
        </dt>Calories :</dt>
        <dd><input required type="number" name="calories" value="<c:out value="${meal.calories}" />"/></dd>
        <br/>
        <input type="submit" value="Submit"/>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </dl>
</form>

</body>
</html>