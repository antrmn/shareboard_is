<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="sbfn" uri="/WEB-INF/tlds/tagUtils.tld" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="loggedUser" value="${requestScope.loggedUser}" />
<navbar>
    <div id="left">

        <span id="nav-logo" >
            <a href = "${context}/home" style="display:inline-flex">
                <i class="fas fa-share-alt-square" style = "color: #ff4500; font-size: 25px;"></i>
                <h3>Shareboard</h3>
            </a>
        </span>
        <span id="nav-crt-sctn" class="interactable" onclick="toggleDropdown('toggle', 'section-dropdown')">
            <i class="fas fa-map-marker-alt" style = "color:#0079D3"></i>
            <span>
                <span>${param.currentSection}</span>
                <i class="fas fa-sort-down" ></i>
            </span>
            <div id="section-dropdown" class="dropdown-content greyContainer">
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Home Feeds</div>
                    <a class = "section-element" href="${context}/home">Home</a>
                    <a class = "section-element" href="${context}/popular">All</a>
                    <a class = "section-element" href="${context}/feed">Feed</a>
                    <a class = "section-element" href="${context}/home?sort=new">New</a>
                <div style = "padding: 12px 16px; color: #77797a; font-size: 10px; font-weight: 500; line-height: 16px;text-transform: uppercase; ">Sections</div>

                <div id = "section-container">
                    <c:forEach items="${applicationScope.sections}" var="section">
                        <div class = "section-element" style="display: flex; ">
                             <c:choose>
                                 <c:when test="${empty section.value.picture }">
                                     <img class = "small-round-image-borderless" src="${pageContext.request.contextPath}/images/default-logo.png">
                                 </c:when>
                                 <c:otherwise>
                                     <img class = "small-round-image-borderless" src= "${applicationScope.picsLocation}/${ section.value.picture}">
                                 </c:otherwise>
                             </c:choose>
                            <a class = "dropdown-section-link" href="${context}/s/${section.value.name}">${section.value.name}</a>
                            <input type = "hidden" name = "sectionId" value = "${section.value.id}">

                            <%-- NOTA - userFollows in session scope: follow da guest           --%>
                            <%--        userFollows in request scope: follow da logged user     --%>
                            <i class="${userFollows.contains(section.value.id) ? 'fas fa-star star favorite-star follow-button follow-button-isfollowing' : 'far fa-star star follow-button'}"
                               onclick="toggleFollow(this)" data-section-id = "${section.value.id}">
                            </i>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </span>

        <div id="nav-search" >
            <form action="${pageContext.request.contextPath}/search">
                 <button type="submit" class="fabutton"><i id = "search-icon" class="fas fa-search"></i></button>
                <input type="text" placeholder="Search" name="content">
            </form>
        </div>
    </div>
    <div id="nav-profile">
        <c:choose>
            <c:when test="${not empty loggedUser}">
                <a href = "${context}/newpost" style="margin-right: 20px;"><i class="fas fa-edit"></i></a>
                <span id = "profile-container" class = "interactable" href = "${context}/me" onclick="toggleDropdown('toggle', 'profile-dropdown')" >
                    <c:choose>
                        <c:when test="${empty loggedUser.picture}">
                            <i id = "nav-profile-photo" class="fas fa-user-circle"></i>
                        </c:when>
                        <c:otherwise>
                            <img id="nav-profile-photo-logged" src="${context}/pics/${loggedUser.picture}">
                        </c:otherwise>
                    </c:choose>


                    <div id="nav-profile-data" >
                        <p style="display: block; margin-bottom:0px; ">${loggedUser.username}</p>
<%--                        <div style="display: block; font-size: 12px; margin-top:0px;">--%>
<%--                            <i class="far fa-arrow-alt-circle-up" style="color: orangered; display: inline; margin-top:0px;"></i>--%>
<%--                        </div>--%>
                    </div>
                    <i class="fas fa-sort-down" style="display: inline-block;"></i>
                    <div id="profile-dropdown" class="dropdown-content greyContainer">
                        <a class="dropdown-link" href="${context}/me">
                            <i class="fas fa-address-card"></i>
                            Profile
                        </a>
                        <a class="dropdown-link" href="${context}/edituser?id=${loggedUser.id}">
                            <i class="fas fa-sliders-h"></i>
                            Edit profile
                        </a>

                    <c:if test="${loggedUser.admin.booleanValue() == true}">
                        <a class="dropdown-link" href="${context}/admin">
                            <i class="fas fa-user-shield"></i>
                            Pannello Admin
                        </a>
                    </c:if>

                        <a class="dropdown-link" href="${context}/logout">
                            <i class="fas fa-sign-out-alt"></i>
                            Log Out
                        </a>
                    </div>
                </span>
            </c:when>
            <c:otherwise>
                <div id = "button-container">
                    <a id = "login-button" href= "${context}/login" class = "roundButton darkGreyButton">Log In</a>
                    <a id = "register-button" href= "${context}/register" class = "roundButton lightGreyButton">Sign Up</a>
                </div>

                <span id = "profile-container" class = "interactable hide" onclick="toggleDropdown('toggle', 'right-dropdown')" >
                    <i class="fas fa-user-circle nav-right-dropdown"></i>
                    <div id="right-dropdown" class="dropdown-content greyContainer">
                        <a class="dropdown-link" href="${context}/login">
                           <i class="fas fa-sign-in-alt"></i>
                            Login
                        </a>

                        <a class="dropdown-link" href="${context}/register">
                            <i class="fas fa-user-plus"></i>
                            Register
                        </a>
                    </div>
                </span>
            </c:otherwise>
        </c:choose>
        <div id="container-switcher">☰</div>
    </div>
</navbar>