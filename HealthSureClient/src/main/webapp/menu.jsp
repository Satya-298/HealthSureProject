<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Doctor Availability - Menu</title>
</head>
<body>
<f:view>
    <h:form>
        <h1>Doctor Availability - Menu</h1>

        <h:panelGrid columns="1" cellpadding="10">
            <h:commandLink value="Add Doctor Availability" action="addAvailability" />
            <h:commandLink value="Search Availability by Doctor" action="searchAvailabilityByDoctorId" />
            <h:commandLink value="List Availability by Date" action="listAvailabilityByDate" />
        </h:panelGrid>
    </h:form>
</f:view>
</body>
</html>
