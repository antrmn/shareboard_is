<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="sbfn" uri="/WEB-INF/tlds/tagUtils.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="maxLengthBan"
       value="${requestScope.loggedUserBans.stream()
                             .filter(ban -> ban.section.id == param.sectionId
                                            || ban.global.booleanValue() == true)
                             .max((ban1,ban2) -> ban1.endTime.compareTo(ban2.endTime))
                             .orElse(null)}"
       scope="page"/>
<div class="greyContainer grid-y-nw  align-center" style=" word-break: break-word;">
  <h3>About Community</h3>
  <h4 style=" word-break: break-word; text-align: center;"> ${empty param.description ? "Nessuna descrizione." : param.description}</h4>
  <h4> ${param.nFollowers} Membri</h4>
  <c:choose>
    <c:when test="${not empty maxLengthBan}">
      <h4 style = "text-align: center;">Non puoi postare in questa sezione fino al <fmt:formatDate value="${sbfn:getDate(maxLengthBan.endTime)}" pattern="dd-MM-YYYY"/></h4>
    </c:when>
    <c:otherwise>
      <c:if test="${not empty requestScope.loggedUser}">
        <a class = "lightGreyButton roundButton" href = "${context}/newpost?section=${param.sectionId}" style = "margin-bottom: 15px;">Invia Contenuto</a>
      </c:if>
    </c:otherwise>
  </c:choose>

</div>