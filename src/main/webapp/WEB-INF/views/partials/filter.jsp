<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Questo partial jsp accetta 2 parametri forniti dall'utente:
     1. view ('popular' o 'following') [ha effetto solo nella home]
     2. sort ('new' o 'top')
--%>

<c:set var="isViewSet" value="${param.view == 'popular' || param.view == 'following'}"/>
<c:set var="isSortSet" value="${param.sort == 'top' || param.sort == 'new'}"/>

<div id="filter" class="greyContainer grid-x-nw align-center" ${isViewSet ? "filterset" : ""} ${isSortSet ? "orderbyset" : ""} style = "height:60px">
  <c:if test="${param.isHome == 'true'}">
    <a id="popular-button" href="javascript:void(0)" class="${param.view == 'popular' ? 'selected' : ''}"><i class="fas fa-rocket"></i>All</a>
  </c:if>
  <a id="top-button" href="javascript:void(0)" class="${param.sort == 'top' ? 'selected' : ''}"><i class="fas fa-burn"></i>Top</a>
  <a id="new-button" href="javascript:void(0)" class="${param.sort == 'new' ? 'selected' : ''}"><i class="fas fa-certificate"></i>New</a>
  <i class="fas fa-ellipsis-h" id = "filter-icon" style = "margin-left: auto;" onclick="toggleDropdown('toggle', 'filter-dropdown')">
    <div id="filter-dropdown" class="dropdown-content greyContainer filter-dropdown-content" style = "overflow-y: hidden; width: 150px;">
      <a class="dropdown-link" id="set-default-button" href="javascript:void(0)" style = "font-family: Verdana; margin:0px; padding: 12px 0px 12px 20px;">
        Set Default
      </a>
    </div>
  </i>
</div>