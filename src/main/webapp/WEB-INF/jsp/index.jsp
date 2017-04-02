<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" %>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Публикации</title>
    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script language="javascript" src="/js/jquery.js" type="text/javascript"></script>
    <script language="javascript" src="/js/login.js" type="text/javascript"></script>
</head>
<body>
<div class="container">
    <form id="getCitations" action="/citations/" method="get">
        <h3>Экспорт списка публикаций из <br><a href="http://elibrary.ru" target="_blank"><img src="/img/elibrary_logo.svg"/></a>
        </h3>
        <label for="inputURL" class="sr-only">Ссылка на работы автора</label>
        <input type="text" id="inputURL" name="inputURL" class="form-control width-500"
               placeholder="Ссылка, например http://elibrary.ru/author_items.asp?authorid=692326" required autofocus>
        <br>
        <input type="submit" class="btn btn-lg btn-warning" value="Получить список">
        <br>
        <c:if test="${! empty requestScope.customError}">
            <div class="error-div alert-error">
                <h5>Сервер вернул ошибку</h5>
                <h5>${requestScope.customError}</h5>
            </div>
        </c:if>
    </form>
</div>
</body>