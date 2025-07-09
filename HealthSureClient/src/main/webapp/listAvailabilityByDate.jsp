<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Availability by Date</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            min-height: 100vh;
            padding: 24px;
            margin: 0;
        }

        .form-container {
            background: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            max-width: 1100px;
            margin: auto;
        }

        h2 {
            font-size: 24px;
            font-weight: bold;
            color: #1d4ed8;
            margin-bottom: 24px;
            text-align: center;
        }

        .centered-input-block {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 24px;
        }

        label {
            font-weight: 600;
            margin-bottom: 6px;
            color: #374151;
        }

        input[type="text"] {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            width: 240px;
            font-size: 14px;
        }

        .error {
            color: red;
            font-size: 13px;
            margin-top: 4px;
            min-height: 18px;
            text-align: center;
        }

        .button-group {
            display: flex;
            justify-content: center;
            gap: 16px;
            margin-top: 12px;
        }

        .btn {
            padding: 10px 16px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-blue {
            background-color: #2563eb;
            color: white;
        }

        .btn-blue:hover {
            background-color: #1e40af;
        }

        .btn-gray {
            background-color: #6b7280;
            color: white;
        }

        .btn-gray:hover {
            background-color: #4b5563;
        }

        .table-wrapper {
            display: flex;
            justify-content: center;
            margin-top: 20px;
        }

        table {
            width: 100%;
            max-width: 1100px;
            table-layout: fixed;
            border-collapse: collapse;
            text-align: center;
            background-color: white;
            word-wrap: break-word;
        }

        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            white-space: normal;
            word-wrap: break-word;
        }

        th {
            background-color: #f9fafb;
            color: #2563eb;
            cursor: pointer;
        }

        th:hover {
            text-decoration: underline;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 24px;
            margin-top: 24px;
            background-color: white;
            padding: 12px;
            width: 100%;
            position: fixed;
            bottom: 12px;
            left: 0;
            right: 0;
            box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.08);
        }

        .link {
            color: #2563eb;
            text-decoration: none;
        }

        .link:hover {
            text-decoration: underline;
        }

        .message {
            text-align: center;
            color: red;
            margin-top: 16px;
        }

        .btn-update {
            background-color: #22c55e;
            color: white;
            padding: 6px 12px;
            border-radius: 6px;
        }

        .btn-update:hover {
            background-color: #15803d;
        }
    </style>
</head>
<body>
<f:view>
<h:form prependId="false" styleClass="form-container">

    <h2>Search Availability by Date</h2>

    <div class="centered-input-block">
        <h:outputLabel for="date" value="Select Date:" />
        <h:inputText id="date" value="#{availabilityController.selectedDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:inputText>
        <h:message for="date" styleClass="error" />

        <div class="button-group">
            <h:commandButton value="Search" action="#{availabilityController.fetchAvailabilityByDate}" styleClass="btn btn-blue" />
            <h:commandButton value="Reset" action="#{availabilityController.resetSearchDateForm}" styleClass="btn btn-gray" />
        </div>
    </div>

    <div class="table-wrapper">
        <h:dataTable value="#{availabilityController.paginatedList}" var="avail"
                     rendered="#{not empty availabilityController.availabilityByDateList}"
                     styleClass="availability-table">

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Availability ID" action="#{availabilityController.sortBy('availabilityId')}" />
                </f:facet>
                <h:outputText value="#{avail.availabilityId}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Doctor ID" action="#{availabilityController.sortBy('doctorId')}" />
                </f:facet>
                <h:outputText value="#{avail.doctor.doctorId}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Available Date" action="#{availabilityController.sortBy('availableDate')}" />
                </f:facet>
                <h:outputText value="#{avail.availableDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Start Time" action="#{availabilityController.sortBy('startTime')}" />
                </f:facet>
                <h:outputText value="#{avail.startTime}">
                    <f:convertDateTime pattern="HH:mm" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="End Time" action="#{availabilityController.sortBy('endTime')}" />
                </f:facet>
                <h:outputText value="#{avail.endTime}">
                    <f:convertDateTime pattern="HH:mm" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Slot Type" action="#{availabilityController.sortBy('slotType')}" />
                </f:facet>
                <h:outputText value="#{avail.slotType}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Recurring" action="#{availabilityController.sortBy('recurring')}" />
                </f:facet>
                <h:outputText value="#{avail.recurring ? 'Yes' : 'No'}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Total Slots" action="#{availabilityController.sortBy('totalSlots')}" />
                </f:facet>
                <h:outputText value="#{avail.totalSlots}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Notes" action="#{availabilityController.sortBy('notes')}" />
                </f:facet>
                <h:outputText value="#{avail.notes}" style="display:block; white-space:normal;" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:outputText value="Actions" />
                </f:facet>
                <h:commandButton value="Update" action="updateAvailability" styleClass="btn-update">
                    <f:setPropertyActionListener 
                        target="#{availabilityController.selectedAvailability}" 
                        value="#{avail}" />
                </h:commandButton>
            </h:column>

        </h:dataTable>
    </div>

    <h:panelGroup rendered="#{not empty availabilityController.availabilityByDateList}">
        <div class="pagination">
            <h:commandLink action="#{availabilityController.previousPage}" styleClass="link">
                <h:outputText value="Previous" />
                <f:param name="scroll" value="top" />
            </h:commandLink>

            <h:outputText value="Page #{availabilityController.currentPage + 1} of #{availabilityController.totalPages}" />

            <h:commandLink action="#{availabilityController.nextPage}" styleClass="link">
                <h:outputText value="Next" />
                <f:param name="scroll" value="top" />
            </h:commandLink>
        </div>
    </h:panelGroup>

    <h:outputText value="#{availabilityController.message}" styleClass="message" />

</h:form>
</f:view>
</body>
</html>
