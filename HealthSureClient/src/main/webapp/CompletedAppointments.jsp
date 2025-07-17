<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Completed Appointments</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f0f0f0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
        }

        th {
            background-color: #ddd;
        }

        .completed {
            color: green;
            font-weight: bold;
        }
    </style>
</head>
<body>

<f:view>
    <h:form>
        <h2><h:outputText value="Completed Appointments" /></h2>

        <h:commandButton value="Load Completed Appointments"
                         action="#{appointmentController.loadCompletedAppointments}" />

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
                <f:facet name="header"><h:outputText value="Status" /></f:facet>
                <h:outputText value="#{appt.status}" styleClass="completed" />
            </h:column>

            <h:column>
                <f:facet name="header"><h:outputText value="Notes" /></f:facet>
                <h:outputText value="#{appt.notes}" />
            </h:column>

        </h:dataTable>
    </h:form>
</f:view>

</body>
</html>
