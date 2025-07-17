<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
    <h:form>
        <h2>Doctor Appointment Status Viewer</h2>

        <!-- Doctor ID Input -->
        <h:panelGrid columns="2" cellpadding="5">
            <h:outputLabel for="doctorId" value="Doctor ID:" />
            <h:inputText id="doctorId" value="#{appointmentController.status}" />

            <!-- Status Dropdown -->
            <h:outputLabel for="status" value="Select Status:" />
			<h:selectOneMenu id="status" value="#{appointmentController.status}" 
				valueChangeListener="#{appointmentController.statusChanged}" onchange="submit()" immediate="true">
                <f:selectItem itemLabel="Select Status" itemValue="" />
                <f:selectItem itemLabel="Pending" itemValue="PENDING" />
                <f:selectItem itemLabel="Booked" itemValue="BOOKED" />
                <f:selectItem itemLabel="Completed" itemValue="COMPLETED" />
                <f:selectItem itemLabel="Cancelled" itemValue="CANCELLED" />
            </h:selectOneMenu>
        </h:panelGrid>

        <h:messages globalOnly="true" style="color:red;" />

        <!-- Appointments Table -->
        <h:panelGroup rendered="#{not empty appointmentController.allAppointments}">
			<h3>
			    <h:outputText value="Appointments (#{appointmentController.status})" />
			</h3>

            <h:dataTable value="#{appointmentController.allAppointments}" var="appt" border="1" cellpadding="5">

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Appointment ID" />
                    </f:facet>
                    <h:outputText value="#{appt.appointmentId}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Doctor ID" />
                    </f:facet>
                    <h:outputText value="#{appt.doctor.doctorId}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Recipient ID" />
                    </f:facet>
                    <h:outputText value="#{appt.recipient.hId}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Start Time" />
                    </f:facet>
                    <h:outputText value="#{appt.start}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="End Time" />
                    </f:facet>
                    <h:outputText value="#{appt.end}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Status" />
                    </f:facet>
                    <h:outputText value="#{appt.status}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Notes" />
                    </f:facet>
                    <h:outputText value="#{appt.notes}" />
                </h:column>

            </h:dataTable>
        </h:panelGroup>

    </h:form>
</f:view>
