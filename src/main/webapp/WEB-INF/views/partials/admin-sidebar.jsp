<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<c:set var="context" value="${pageContext.request.contextPath}" />

<div id="mySidenav" class="sidenav">
    <div href="javascript:void(0)" class="closebtn interactable" onclick="closeNav()">&times;</div>
    <a href="${context}/admin">
        <i class="fas fa-chart-line"></i>
        Dashboard
    </a>

    <a href="${context}/admin/newsection">
        <i class="fas fa-plus-circle"></i>
        Crea Sezione
    </a>
    <a href="${context}/admin/showsections">
        <i class="fas fa-align-justify"></i>
        Gestione Sezioni
    </a>
    <a href="${context}/admin/showusers">
        <i class="fas fa-users"></i>
        Gestione Utenti
    </a>
</div>
<span style="font-size:40px;cursor:pointer; position: fixed; top:50px; left:0;" onclick="openNav()">&#9776;</span>