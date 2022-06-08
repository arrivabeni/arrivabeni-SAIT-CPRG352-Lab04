<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Simple Note Keeper</title>
    </head>
    <body>
        <h1>Simple Note Keeper</h1>
        <c:if test="${!isEditMode}">
            <h2>View Note</h2>
            <p><b>Title:</b> <span>${note.title}</span></p>
            <p><b>Contents:</b><br>${note.content}</p>
            <a href="note?edit">Edit</a>         
        </c:if>
        <c:if test="${isEditMode}">
            <h2>Edit Note</h2>
            <form method="POST" action="note">
                <label for="title">Title: </label>
                <input type="text" name="title" id="title" value="${note.title}">
                <br>
                <label for="content">Contents: </label>
                <textarea id="content" name="content" rows="4" cols="50">${note.content}</textarea>
                <br>
                <input type="submit" value="Save">
            </form>   
        </c:if>
    </body>
</html>
