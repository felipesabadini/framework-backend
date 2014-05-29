<%@ attribute name="values" required="true" %>
<%@ attribute name="showSearch" required="false" %>
<%@ attribute name="gridColumns" required="true" fragment="true" %>
<%@ attribute name="searchFields" required="false" fragment="true" %>
<%@ attribute name="buttons" required="false" fragment="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="row">
	<div id="buttons" class="col-md-5" style="margin-bottom: 10px;">
		<jsp:invoke fragment="buttons" var="buttons"/>
		<c:if test="${not empty buttons}">${buttons}</c:if>
		<c:if test="${empty buttons}">
			<a href="#/insert" class="btn btn-primary" id="btnNovo">
				<span class="glyphicon glyphicon-plus"></span> 
				<fmt:message key="app.button.novo" />
			</a>
			<button id="btnExcluir" class="btn btn-danger" ng-click="ctrl.removeSelection()" ng-disabled="selection.length == 0">
				<span class="glyphicon glyphicon-trash"></span> 
				<fmt:message key="app.button.remove" />
			</button>
		</c:if>
	</div>

	<c:if test="${showSearch != 'false'}">
		<div class="col-md-7">
			<gumga:search on-search="ctrl.search($text, $fields)" search-text="search.text" select-fields="search.fields">
				<jsp:invoke fragment="searchFields" />
			</gumga:search>
		</div>
	</c:if>
	
	<div class="col-xs-12">
		<gumga:table values="${values}" class="table-condensed table-striped" selectable="multiple" selection="selection" on-sort="ctrl.doSort($column, $direction)" sort-by="sort.field" sort-direction="sort.direction">
			<jsp:invoke fragment="gridColumns" />
		</gumga:table>
		
		<gumga:pagination page-size="list.pageSize" total-of-elements="list.count" page="page" on-page-changed="ctrl.goToPage($page)"></gumga:pagination>
	</div>
	
</div>
