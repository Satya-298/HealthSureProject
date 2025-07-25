<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Availability by Date</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 24px;
        }
        .form-container {
            background: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            max-width: 1200px;
            margin: auto;
        }
        h2 {
            text-align: center;
            color: #1d4ed8;
            margin-bottom: 24px;
        }
        .centered-input-block {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            margin-bottom: 6px;
        }
        input[type="date"], input[type="text"] {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }
        .button-group {
            margin-top: 16px;
            display: flex;
            gap: 12px;
        }
        .btn {
            padding: 10px 16px;
            border-radius: 6px;
            border: none;
            font-weight: bold;
            cursor: pointer;
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
            background-color: #374151;
        }
        .error {
            color: red;
            margin-top: 4px;
            font-size: 13px;
        }
        .table-wrapper {
            overflow-x: auto;
            margin-top: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
            background-color: white;
        }
        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            white-space: nowrap;
        }
        th {
            background-color: #f9fafb;
            color: #2563eb;
            cursor: pointer;
        }
        .arrow {
            font-size: 14px;
            margin-left: 4px;
            color: #2563eb;
        }
        .pagination-panel {
            margin-top: 24px;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 12px;
        }
        .message {
            text-align: center;
            color: red;
            margin-top: 16px;
        }
        .btn-update {
	        background-color: #28a745; /* Bootstrap green */
	        color: white;
	        border: none;
	        padding: 5px 10px;
	        border-radius: 4px;
	        cursor: pointer;
	        font-weight: bold;
	    }
	
	    .btn-update:hover {
	        background-color: #218838;
	    }
	
	    .delete-button {
	        background-color: #dc3545; /* Bootstrap red */
	        color: white;
	        border: none;
	        padding: 5px 10px;
	        border-radius: 4px;
	        cursor: pointer;
	        font-weight: bold;
	    }
	
	    .delete-button:hover {
	        background-color: #c82333;
	    }
    </style>

    <script>
        window.addEventListener('load', function () {
            const calendarInput = document.querySelector("#availabilityForm\\:date");
            if (calendarInput) {
                calendarInput.setAttribute("type", "date");
                const today = new Date().toISOString().split("T")[0];
                calendarInput.setAttribute("min", today);
            }
        });
    </script>
</head>

<body>
<f:view>
<h:form id="availabilityForm" styleClass="form-container">
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
            <h:commandButton value="Back" action="#{availabilityController.resetAddFormBackButton}" immediate="true" styleClass="btn btn-gray" />
        </div>
    </div>

    <h:panelGroup rendered="#{not empty availabilityController.availabilityByDateList}">
        <div class="table-wrapper">
			<h:dataTable value="#{availabilityController.paginatedList}" var="avail" border="1">
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('availabilityId')}">
                            <span class="sort-header">
                                Availability ID
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'availabilityId' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.availabilityId}" />
                </h:column>
                

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('doctorName')}">
                            <span>Doctor
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'doctorName' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.doctor.doctorName}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('specialization')}">
                            <span>Specialization
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'specialization' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.doctor.specialization}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('availableDate')}">
                            <span>Available Date
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'availableDate' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.availableDate}">
                        <f:convertDateTime pattern="dd-MM-yyyy" />
                    </h:outputText>
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('startTime')}">
                            <span>Start Time
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'startTime' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.startTime}" >
                        <f:convertDateTime pattern="HH:mm" />
                    </h:outputText>
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('endTime')}">
                            <span>End Time
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'endTime' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.endTime}" >
                        <f:convertDateTime pattern="HH:mm" />
                   	</h:outputText>
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('slotType')}">
                            <span>Slot Type
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'slotType' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.slotType}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('recurring')}">
                            <span>Recurring
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'recurring' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.recurring ? 'Yes' : 'No'}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('totalSlots')}">
                            <span>Total Slots
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'totalSlots' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.totalSlots}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{availabilityController.sortBy('notes')}">
                            <span>Notes
                                <h:outputText styleClass="arrow"
                                              value="#{availabilityController.sortColumn eq 'notes' ? (availabilityController.sortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{avail.notes}" />
                </h:column>
                
                <h:column>
	                <f:facet name="header">
	                    <h:outputText value="Update" />
	                </f:facet>
	                <h:commandButton value="Update" action="updateAvailability" styleClass="btn-update">
	                    <f:setPropertyActionListener target="#{availabilityController.selectedAvailability}" value="#{avail}" />
	                </h:commandButton>
	            </h:column>
	            
            </h:dataTable>
        </div>

        <!-- Pagination -->
        <h:panelGroup layout="block" styleClass="pagination-panel">
            <h:commandButton value="First" action="#{availabilityController.goToFirstPage}"
                 disabled="#{availabilityController.currentPage == 1}" />

			<h:commandButton value="Previous" action="#{availabilityController.previousPage}"
			                 disabled="#{availabilityController.currentPage == 1}" />
			
			<h:outputText value="Page #{availabilityController.currentPage} of #{availabilityController.totalPages}" />
			
			<h:commandButton value="Next" action="#{availabilityController.nextPage}"
			                 disabled="#{availabilityController.currentPage == availabilityController.totalPages}" />
			
			<h:commandButton value="Last" action="#{availabilityController.goToLastPage}"
			                 disabled="#{availabilityController.currentPage == availabilityController.totalPages}" />

        </h:panelGroup>
    </h:panelGroup>

    <h:outputText value="#{availabilityController.message}" styleClass="message" />
</h:form>
</f:view>
</body>
</html>
