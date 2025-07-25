<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Doctor Appointment Search</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 30px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }

        .heading {
            text-align: center;
            font-size: 22px;
            font-weight: bold;
            color: #2563eb;
            margin-bottom: 25px;
        }

        .form-panel {
            background-color: #f9fafb;
            padding: 20px;
            border-radius: 6px;
            margin-bottom: 30px;
            border: 1px solid #e5e7eb;
        }

        .form-row {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }

        label {
            width: 150px;
            font-weight: bold;
            margin-right: 10px;
        }

        .btn {
            background-color: #4b5563;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
        }

        .btn:hover {
            background-color: #1f2937;
        }

        .btn-primary {
            background-color: #2563eb;
        }

        .btn-primary:hover {
            background-color: #1d4ed8;
        }

        .error-message {
            color: red;
            font-size: 0.9em;
        }

        .table-container {
            overflow-x: auto;
            margin-top: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ccc;
        }

        th {
            background-color: #f1f5f9;
            font-weight: bold;
            position: relative;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
            margin-top: 25px;
        }

        .pagination a {
            color: #2563eb;
            text-decoration: none;
            font-weight: bold;
        }

        .pagination a:hover {
            text-decoration: underline;
        }

        .sort-icon {
            font-size: 0.7em;
            padding: 0;
            margin: 0;7
            line-height: 1;
            display: inline-block;
        }

        .sort-icons {
            display: inline-flex;
            flex-direction: column;
            align-items: center;
            margin-left: 4px;
        }

        .total-records {
            font-weight: bold;
            margin-bottom: 15px;
            display: block;
            text-align: center;
        }
    </style>
</head>

<body>
<f:view>
    <div class="container">
        <h:form id="appointmentForm">
            <div class="heading">Doctor Appointment Search</div>

            <div class="form-panel">
                <h:messages globalOnly="true" styleClass="error-message" />

                <div class="form-row">
				    <h:outputLabel for="doctorId">
				    	Doctor ID:<span style="color: red">*</span>
				    </h:outputLabel>
				    <div style="display: flex; flex-direction: column;">
				        <h:inputText id="doctorId" value="#{appointmentController.doctorId}" />
				        <h:message for="doctorId" styleClass="error-message" />
				    </div>
				</div>
				
				<div class="form-row">
				    <h:outputLabel for="status">
				    	Appointment Status:<span style="color: red">*</span>
				    </h:outputLabel>
				    <div style="display: flex; flex-direction: column;">
				        <h:selectOneMenu id="status" value="#{appointmentController.status}">
				            <f:selectItem itemLabel="-- Select Status --" itemValue="" />
				            <f:selectItem itemLabel="PENDING" itemValue="PENDING" />
				            <f:selectItem itemLabel="BOOKED" itemValue="BOOKED" />
				            <f:selectItem itemLabel="CANCELLED" itemValue="CANCELLED" />
				            <f:selectItem itemLabel="COMPLETED" itemValue="COMPLETED" />
				        </h:selectOneMenu>
				        <h:message for="status" styleClass="error-message" />
				    </div>
				</div>

                <div style="margin-top: 20px;">
                    <h:commandButton value="Search" action="#{appointmentController.loadAppointmentsByStatus}" styleClass="btn btn-primary" />
                    <h:commandButton value="Reset" action="#{appointmentController.resetForm}" styleClass="btn" />
                </div>
            </div>

            <h:panelGroup rendered="#{not empty appointmentController.paginatedAppointments}">
                <h:outputText value="Total Records: #{appointmentController.totalRecords}" styleClass="total-records" />

                <div class="table-container">
                    <h:dataTable value="#{appointmentController.paginatedAppointments}" var="a" border="1">
                        <!-- Appointment ID -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="Appointment ID" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('appointmentId')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'appointmentId' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('appointmentId')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'appointmentId' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.appointmentId}" />
                        </h:column>

                        <!-- Recipient ID -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="Recipient ID" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('hId')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'hId' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('hId')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'hId' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.recipient.hId}" />
                        </h:column>

                        <!-- Recipient Name -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="Recipient Name" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('recipientName')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'recipientName' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('recipientName')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'recipientName' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.recipient.firstName} #{a.recipient.lastName}" />
                        </h:column>

                        <!-- Status -->
                        <h:column>
                            <f:facet name="header"><h:outputText value="Status" /></f:facet>
                            <h:outputText value="#{a.status}" />
                        </h:column>

                        <!-- Appointment Date -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="Appointment Date" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('appointmentDate')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'appointmentDate' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('appointmentDate')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'appointmentDate' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.start}">
                                <f:convertDateTime pattern="dd-MM-yyyy" />
                            </h:outputText>
                        </h:column>

                        <!-- Start Time -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="Start Time" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('start')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'start' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('start')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'start' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.start}">
                                <f:convertDateTime pattern="HH:mm" />
                            </h:outputText>
                        </h:column>

                        <!-- End Time -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup layout="block" style="display: flex; align-items: center; justify-content: center;">
                                    <h:outputText value="End Time" />
                                    <h:panelGroup styleClass="sort-icons">
                                        <h:commandLink action="#{appointmentController.sortByAsc('end')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'end' and appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▲</h:commandLink>
                                        <h:commandLink action="#{appointmentController.sortByDesc('end')}" 
                                                       rendered="#{!(appointmentController.sortColumn eq 'end' and not appointmentController.sortAscending)}" 
                                                       styleClass="sort-icon">▼</h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{a.end}">
                                <f:convertDateTime pattern="HH:mm" />
                            </h:outputText>
                        </h:column>

                        <!-- Book and Cancel Buttons -->
                        <h:column rendered="#{appointmentController.status eq 'PENDING'}">
                            <f:facet name="header"><h:outputText value="Book" /></f:facet>
                            <h:commandButton value="Book" action="#{appointmentController.approve(a.appointmentId)}" styleClass="btn btn-primary" />
                        </h:column>

                        <h:column rendered="#{appointmentController.status eq 'PENDING'}">
                            <f:facet name="header"><h:outputText value="Cancel" /></f:facet>
                            <h:commandButton value="Cancel" action="#{appointmentController.cancel(a.appointmentId)}" styleClass="btn" />
                        </h:column>
                    </h:dataTable>
                </div>

                <!-- Pagination Controls -->
                <div class="pagination">
                    <h:commandButton value="First" action="#{appointmentController.goToFirstPage}" disabled="#{appointmentController.currentPage == 1}" styleClass="btn" />
                    <h:commandButton value="Previous" action="#{appointmentController.previousPage}" disabled="#{appointmentController.currentPage == 1}" styleClass="btn" />
                    <h:outputText value="Page #{appointmentController.currentPage} of #{appointmentController.totalPages}" />
                    <h:commandButton value="Next" action="#{appointmentController.nextPage}" disabled="#{appointmentController.currentPage == appointmentController.totalPages}" styleClass="btn" />
                    <h:commandButton value="Last" action="#{appointmentController.goToLastPage}" disabled="#{appointmentController.currentPage == appointmentController.totalPages}" styleClass="btn" />
                </div>
            </h:panelGroup>
        </h:form>
    </div>
</f:view>
</body>
</html>