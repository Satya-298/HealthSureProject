<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Appointment Viewer</title>
</head>

<body>
    <f:view>
        <h:form>

            <h2>Doctor Appointment Status Viewer</h2>

            <!-- Doctor ID Input + Status Selection -->
            <h:panelGrid columns="2" cellpadding="5">
                <h:outputText value="Doctor ID:" />
                <h:inputText value="#{appointmentController.doctorId}" />

                <h:outputText value="Select Status:" />
                <h:selectOneMenu value="#{appointmentController.status}">
                    <f:selectItem itemLabel="Select Status" itemValue="" />
                    <f:selectItem itemLabel="Pending" itemValue="PENDING" />
                    <f:selectItem itemLabel="Booked" itemValue="BOOKED" />
                    <f:selectItem itemLabel="Completed" itemValue="COMPLETED" />
                    <f:selectItem itemLabel="Cancelled" itemValue="CANCELLED" />
                </h:selectOneMenu>

                <h:outputText />
                <h:commandButton value="Search" action="#{appointmentController.loadAppointmentsByStatus}" />
            </h:panelGrid>

            <!-- Error or Info Messages -->
            <h:messages globalOnly="true" style="color:red;" />

            <!-- Appointments Table -->
            <h:panelGroup rendered="#{not empty appointmentController.allAppointments}">
                <h3>
                    <h:outputText value="Appointments (" />
                    <h:outputText value="#{appointmentController.status}" />
                    <h:outputText value=")" />
                </h3>

                <h:dataTable value="#{appointmentController.allAppointments}" var="appt" border="1" cellpadding="5">
                    <h:column>
                        <f:facet name="header"><h:outputText value="Appointment ID" /></f:facet>
                        <h:outputText value="#{appt.appointmentId}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Doctor ID" /></f:facet>
                        <h:outputText value="#{appt.doctor.doctorId}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Recipient ID" /></f:facet>
                        <h:outputText value="#{appt.recipient.hId}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Start Time" /></f:facet>
                        <h:outputText value="#{appt.start}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="End Time" /></f:facet>
                        <h:outputText value="#{appt.end}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Status" /></f:facet>
                        <h:outputText value="#{appt.status}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Notes" /></f:facet>
                        <h:outputText value="#{appt.notes}" />
                    </h:column>
                </h:dataTable>
            </h:panelGroup>
        </h:form>
    </f:view>
</body>
</html>
