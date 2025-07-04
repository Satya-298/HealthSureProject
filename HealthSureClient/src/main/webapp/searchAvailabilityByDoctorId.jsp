<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Availability Search</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #333;
            padding: 8px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<f:view>
<h:form>

    <h2>Doctor Availability Search</h2>

    <!-- Doctor ID Input -->
    <h:outputLabel for="doctorId" value="Doctor ID:" />
    <h:inputText id="doctorId" value="#{availabilityController.doctorId}" /> &nbsp;&nbsp;&nbsp;&nbsp;
    <h:commandButton value="Search" action="#{availabilityController.fetchAvailability}" /> 
    <br/><br/>

    <!-- Render table only if list is not empty -->
    <h:dataTable value="#{availabilityController.availabilityList}" var="avail"
                 border="1" rendered="#{not empty availabilityController.availabilityList}">

        <h:column>
            <f:facet name="header">
                <h:outputText value="Availability ID" />
            </f:facet>
            <h:outputText value="#{avail.availabilityId}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Available Date" />
            </f:facet>
            <h:outputText value="#{avail.availableDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:outputText>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Start Time" />
            </f:facet>
            <h:outputText value="#{avail.startTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:outputText>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="End Time" />
            </f:facet>
            <h:outputText value="#{avail.endTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:outputText>
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Slot Type" />
            </f:facet>
            <h:outputText value="#{avail.slotType}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Recurring" />
            </f:facet>
            <h:outputText value="#{avail.recurring ? 'Yes' : 'No'}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Total Slots" />
            </f:facet>
            <h:outputText value="#{avail.totalSlots}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Notes" />
            </f:facet>
            <h:outputText value="#{avail.notes}" />
        </h:column>

    </h:dataTable>

    <br/>
    <h:outputText value="#{availabilityController.message}" style="color:red;" />

</h:form>
</f:view>
</body>
</html>
