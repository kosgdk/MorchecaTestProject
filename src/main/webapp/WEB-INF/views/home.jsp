<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
    <jsp:include page="fragments/styles-and-scripts.jsp"/>
    <title>Test project</title>
</head>
<body>


<div class="container">
    <legend>
        Welcome, <sec:authentication property="name"/> <a href="#" id="logoutLink" title="Logout"><i
            class="fa fa-sign-out" aria-hidden="true" style="margin-left: 10px"></i></a>
    </legend>
    <%-- Logout hidden form --%>
    <c:url value="/logout" var="logoutUrl"/>
    <form id="logoutForm" action="${logoutUrl}" method="POST">
        <sec:csrfInput/>
    </form>

    <%-- Message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-dismissible alert-danger">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
                ${message}
        </div>
    </c:if>

    <%-- Tabs --%>
    <ul class="nav nav-tabs margin-bottom-20">
        <li class="active"><a href="#allUsersTab" id="allUsersTabLink" data-toggle="tab">All users</a></li>
        <li><a href="#createUserTab" id="createUserTabLink" data-toggle="tab">Create user</a></li>
        <c:if test="${not empty editUser}">
            <li><a href="#editUserTab" id="editUserTabLink" data-toggle="tab">Edit user <b>${editUser.name}</b></a></li>
        </c:if>
    </ul>
    <div id="myTabContent" class="tab-content">

        <%-- All users tab --%>
        <div class="tab-pane active in" id="allUsersTab">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th style="width: 60px"></th>
                    <th>Name</th>
                    <th>E-mail</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="user" items="${users}" varStatus="counter">

                    <c:choose>
                        <c:when test="${user.id == loggedInUser.id}">
                            <c:set value="boldRow" var="loggedInUserRow"/>
                            <c:set value='id="loggedInUserDeletionLink"' var="loggedInUserDeletionLinkId"/>
                        </c:when>
                        <c:otherwise>
                            <c:set value="" var="loggedInUserRow"/>
                            <c:set value="" var="loggedInUserDeletionLinkId"/>
                        </c:otherwise>
                    </c:choose>

                    <tr class="userRow ${loggedInUserRow}">

                            <%-- Edit/Delete buttons --%>
                        <td class="text-nowrap">
                            <spring:url value="/edit?id=${user.id}" var="editUrl"/>
                            <spring:url value="/delete?id=${user.id}" var="deleteUrl"/>
                            <div class="editDeleteButtons">
                                <a title="Edit" href='${editUrl}' class="inline-block"><i
                                        class="fa fa-pencil-square fa-lg" aria-hidden="true"></i></a>&nbsp;|
                                <a title="Delete" href='${deleteUrl}' ${loggedInUserDeletionLinkId} class="inline-block"
                                   style="color:red"><i
                                        class="fa fa-trash fa-lg" aria-hidden="true"></i></a>
                            </div>
                        </td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <%-- Create user tab --%>
        <div class="tab-pane" id="createUserTab">
            <%-- Register form --%>
            <spring:url value="/create_user" var="formUrl"/>
            <form:form action="${formUrl}" method="POST" modelAttribute="newUser"
                       cssClass="form-horizontal">
                <fieldset>
                    <c:set var="widthClass" value="col-lg-6"/>

                        <%-- Name --%>
                    <c:set var="field" value="name"/>
                    <spring:message var="restrictiona" code="user.name.restrictions"/>
                    <spring:bind path="${field}">
                        <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                            <label for="${field}" class="col-lg-3 control-label">Name</label>
                            <div class="${widthClass}">
                                <form:input path="${field}" id="${field}RegisterField" cssClass="form-control"
                                            minlength="3" maxlength="20" data-toggle="tooltip" data-placement="right"
                                            title="${restrictiona}" data-html="true"/>
                                <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                </c:if>

                            </div>
                        </div>
                    </spring:bind>

                        <%-- Email --%>
                    <c:set var="field" value="email"/>
                    <spring:message var="restrictiona" code="user.email.restrictions"/>
                    <spring:bind path="${field}">
                        <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                            <label for="${field}" class="col-lg-3 control-label">Email</label>
                            <div class="${widthClass}">
                                <form:input path="${field}" id="${field}RegisterField" cssClass="form-control"
                                            minlength="5" maxlength="50" data-toggle="tooltip" data-placement="right"
                                            title="${restrictiona}" data-html="true"/>
                                <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                </c:if>
                            </div>
                        </div>
                    </spring:bind>

                        <%-- Password --%>
                    <c:set var="field" value="password"/>
                    <spring:message var="restrictiona" code="user.password.restrictions"/>
                    <spring:bind path="${field}">
                        <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                            <label for="${field}" class="col-lg-3 control-label">Password</label>
                            <div class="${widthClass}">
                                <form:password path="${field}" id="${field}RegisterField" cssClass="form-control"
                                               minlength="8" maxlength="20" data-toggle="tooltip" data-placement="right"
                                               title="${restrictiona}" data-html="true"/>
                                <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                </c:if>
                            </div>
                        </div>
                    </spring:bind>

                        <%-- Password confirm--%>
                    <c:set var="field" value="passwordConfirm"/>
                    <spring:bind path="${field}">
                        <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                            <label for="${field}" class="col-lg-3 control-label">Confirm pass.</label>
                            <div class="${widthClass}">
                                <form:password path="${field}" id="${field}RegisterField" cssClass="form-control"
                                               minlength="8" maxlength="20"/>
                                <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                </c:if>
                            </div>
                        </div>
                    </spring:bind>

                        <%-- Register button--%>
                    <div class="form-group">
                        <div class="${widthClass} col-lg-offset-3">
                            <button type="submit" class="btn btn-primary login-btn" id="registerButton"
                                    title="Register">Create user
                            </button>
                        </div>
                    </div>

                </fieldset>
            </form:form>
        </div>

        <%-- Edit user tab --%>
        <c:if test="${not empty editUser}">
            <div class="tab-pane" id="editUserTab">
                    <%-- Edit user form --%>
                <spring:url value="/edit?id=${editUser.id}" var="formUrl"/>
                <form:form action="${formUrl}" method="POST" modelAttribute="editUser" cssClass="form-horizontal">
                    <fieldset>
                        <c:set var="widthClass" value="col-lg-6"/>

                            <%-- Name --%>
                        <c:set var="field" value="name"/>
                        <spring:message var="restrictiona" code="user.name.restrictions"/>
                        <spring:bind path="${field}">
                            <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                <label for="${field}EditField" class="col-lg-3 control-label">Name</label>
                                <div class="${widthClass}">
                                    <form:input path="${field}" id="${field}EditField" cssClass="form-control"
                                                minlength="3" maxlength="20" data-toggle="tooltip" data-placement="right"
                                                title="${restrictiona}" data-html="true"/>
                                    <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                    </c:if>

                                </div>
                            </div>
                        </spring:bind>

                            <%-- Email --%>
                        <c:set var="field" value="email"/>
                        <spring:message var="restrictiona" code="user.email.restrictions"/>
                        <spring:bind path="${field}">
                            <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                <label for="${field}" class="col-lg-3 control-label">Email</label>
                                <div class="${widthClass}">
                                    <form:input path="${field}" id="${field}EditField" cssClass="form-control"
                                                minlength="5" maxlength="50" data-toggle="tooltip" data-placement="right"
                                                title="${restrictiona}" data-html="true"/>
                                    <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                    </c:if>
                                </div>
                            </div>
                        </spring:bind>

                            <%-- Password --%>
                        <c:set var="field" value="password"/>
                        <spring:message var="restrictiona" code="user.password.restrictions"/>
                        <spring:bind path="${field}">
                            <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                <label for="${field}" class="col-lg-3 control-label">New password</label>
                                <div class="${widthClass}">
                                    <form:password path="${field}" id="${field}EditField" cssClass="form-control"
                                                   minlength="8" maxlength="20" data-toggle="tooltip" data-placement="right"
                                                   title="${restrictiona}" data-html="true"/>
                                    <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                    </c:if>
                                    <span class="help-block">Leave blank to keep old password</span>
                                </div>
                            </div>
                        </spring:bind>

                            <%-- Password confirm--%>
                        <c:set var="field" value="passwordConfirm"/>
                        <spring:bind path="${field}">
                            <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                                <label for="${field}" class="col-lg-3 control-label">Confirm new pass.</label>
                                <div class="${widthClass}">
                                    <form:password path="${field}" id="${field}EditField" cssClass="form-control"
                                                   minlength="8" maxlength="20"/>
                                    <c:if test='${status.errors.hasFieldErrors(field)}'>
                                                <span class="help-block">
                                                    <form:errors path="${field}"/>
                                                </span>
                                    </c:if>
                                </div>
                            </div>
                        </spring:bind>

                            <%-- Save/Cancel buttons--%>
                        <div class="form-group">
                            <div class="${widthClass} col-lg-offset-3">
                                <button type="submit" id="editButton" class="btn btn-primary login-btn"
                                        title="Register">Save changes
                                </button>

                                <spring:url value="/" var="homeLink"/>
                                <button type="button" id="cancelEditButton" class="btn btn-default"
                                        title="Register" onclick="location.href = '${homeLink}'">Cancel
                                </button>
                            </div>
                        </div>

                    </fieldset>
                </form:form>
            </div>
        </c:if>

    </div>


    <%-- Modal confirmation of deleting current account --%>
    <div class="modal" id="warningModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Attention!</h4>
                </div>
                <div class="modal-body">
                    <p>You are trying to delete your account. You will be logged out after confirming deletion.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" id="loggedInUserConfirmDeletionButton" class="btn btn-danger btn-sm">Confirm
                        deletion
                    </button>
                    <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>

</div>

<%-- Logout form submit script --%>
<script type="text/javascript">
    $(document).ready(function () {
        $("#logoutLink").click(function (e) {
            e.preventDefault();
            $("#logoutForm").submit();
        })
    })
</script>

<%-- Edit/Delete buttons visibility script --%>
<script type="text/javascript">
    $(document).ready(function () {
        $(".editDeleteButtons:first").show();
        $(".userRow").hover(function () {
            $(".editDeleteButtons").hide();
            $(this).find(".editDeleteButtons").show();
        });
    });
</script>

<%-- Current user deletion popup script --%>
<spring:url value="/delete?id=${loggedInUser.id}" var="loggedInUserConfirmDeletionLink"/>
<script type="text/javascript">
    $(document).ready(function () {
        $("#loggedInUserDeletionLink").click(function (e) {
            e.preventDefault();
            $("#warningModal").modal("show");
        });

        $("#loggedInUserConfirmDeletionButton").click(function () {
            location.href = "${loggedInUserConfirmDeletionLink}"
        })
    })
</script>

<%-- Edit user tab script --%>
<script type="text/javascript">
    $(document).ready(function () {
        <c:choose>
            <c:when test="${not empty editUser}">
                $('.nav-tabs a[href="#editUserTab"]').tab('show');
            </c:when>
            <c:when test="${not empty creationInProcess}">
                $('.nav-tabs a[href="#createUserTab"]').tab('show');
            </c:when>
        </c:choose>
    })
</script>

<%-- Create user tab control --%>
<script type="text/javascript">
    $(document).ready(function () {
        $("#createUserTabLink").click(function () {
            setTimeout(function () {
                $("#nameRegisterField").focus();
            }, 20)
        });
    })
</script>

</body>
</html>