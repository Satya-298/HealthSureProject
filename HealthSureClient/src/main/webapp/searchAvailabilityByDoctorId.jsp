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

        .message {
            color: red;
            font-weight: bold;
        }

        .info {
            color: blue;
            font-style: italic;
        }
    </style>
</head>
<body>
<f:view>
<h:form prependId="false">

    <h2>Doctor Availability Search</h2>

    <!-- Doctor ID Input -->
    <h:outputLabel for="doctorId" value="Doctor ID:" />
    <h:inputText id="doctorId" value="#{availabilityController.doctorId}" /> 
    &nbsp;&nbsp;&nbsp;&nbsp;

    <h:commandButton value="Search" action="#{availabilityController.fetchAvailability}" />
    &nbsp;&nbsp;
    <h:commandButton value="Reset" action="#{availabilityController.resetForm}" />

    <br/><br/>

    <!-- Table for availability list -->
    <h:panelGroup rendered="#{not empty availabilityController.availabilityList}">
        <h:dataTable value="#{availabilityController.availabilityList}" var="avail" border="1">

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
    </h:panelGroup>

    <!-- No Data Found Message -->
    <h:panelGroup rendered="#{empty availabilityController.availabilityList and not empty availabilityController.doctorId}">
        <h:outputText value="No availability records found for this Doctor ID." styleClass="info" />
    </h:panelGroup>

    <!-- Optional: Validation or system messages -->
    <br/>
    <h:outputText value="#{availabilityController.message}" styleClass="message" rendered="#{not empty availabilityController.message}" />

</h:form>
</f:view>
</body>
</html>
