<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Booked Appointments</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f4f4f4;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
        }
        th, td {
            border: 1px solid #aaa;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #e0e0e0;
        }
        .booked { color: blue; font-weight: bold; }
        .completed { color: green; font-weight: bold; }
    </style>
</head>
<body>

<f:view>
    <h:form>
        <h2><h:outputText value="Booked Appointments" /></h2>

        <h:commandButton value="Load Booked Appointments"
                         action="#{appointmentController.loadBookedAppointments}" />

        <h:messages style="color:red;" />
        <h:outputText value="#{appointmentController.statusMessage}" style="color:green;" />

        <h:dataTable value="#{appointmentController.allAppointments}" var="appt"
                     rendered="#{not empty appointmentController.allAppointments}">
                     
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
                <f:facet name="header"><h:outputText value="Status" /></f:facet>
                <h:outputText value="#{appt.status}" styleClass="booked" />
            </h:column>

            <h:column>
                <f:facet name="header"><h:outputText value="Slot No" /></f:facet>
                <h:outputText value="#{appt.slotNo}" />
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
                <f:facet name="header"><h:outputText value="Notes" /></f:facet>
                <h:outputText value="#{appt.notes}" />
            </h:column>

            <h:column>
                <f:facet name="header"><h:outputText value="Action" /></f:facet>

                <!-- Show Complete button if status is BOOKED -->
                <h:panelGroup rendered="#{appt.status eq 'BOOKED'}">
                    <h:commandButton value="Complete"
                                     action="#{appointmentController.complete(appt.appointmentId)}" />
                </h:panelGroup>

                <!-- Show label if already completed (after reload) -->
                <h:panelGroup rendered="#{appt.status eq 'COMPLETED'}">
                    <h:outputText value="Completed" styleClass="completed" />
                </h:panelGroup>
            </h:column>
        </h:dataTable>
    </h:form>
</f:view>

</body>
</html>
