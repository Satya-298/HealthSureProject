<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<html>
<head>
    <title>Add Doctor Availability</title>
</head>
<body>
<f:view>
    <h:form>

        <h3>Add Doctor Availability</h3>

        <h:panelGrid columns="2" cellpadding="5">

            <!-- Doctor ID -->
            <h:outputLabel for="doctorId" value="Doctor ID:" />
            <h:inputText id="doctorId" value="#{availabilityController.doctorId}" />

            <!-- Available Date -->
            <h:outputLabel for="date" value="Available Date:" />
            <h:inputText id="date" value="#{availabilityController.availability.availableDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:inputText>

            <!-- Start Time -->
            <h:outputLabel for="startTime" value="Start Time (HH:mm):" />
            <h:inputText id="startTime" value="#{availabilityController.availability.startTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:inputText>

            <!-- End Time -->
            <h:outputLabel for="endTime" value="End Time (HH:mm):" />
            <h:inputText id="endTime" value="#{availabilityController.availability.endTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:inputText>

            <!-- Slot Type -->
            <h:outputLabel for="slotType" value="Slot Type:" />
            <h:selectOneMenu id="slotType" value="#{availabilityController.availability.slotType}">
                <f:selectItem itemValue="" itemLabel="-- Select Slot Type --" noSelectionOption="true" />
                <f:selectItem itemValue="STANDARD" itemLabel="Standard" />
                <f:selectItem itemValue="ADHOC" itemLabel="Adhoc" />
            </h:selectOneMenu>

            <!-- Recurring -->
            <h:outputLabel for="recurring" value="Recurring:" />
            <h:selectBooleanCheckbox id="recurring" value="#{availabilityController.availability.recurring}" />

            <!-- Total Slots -->
            <h:outputLabel for="totalSlots" value="Total Slots:" />
            <h:inputText id="totalSlots" value="#{availabilityController.availability.totalSlots}" />

            <!-- Notes -->
            <h:outputLabel for="notes" value="Notes:" />
            <h:inputTextarea id="notes" value="#{availabilityController.availability.notes}" rows="4" cols="40" />

        </h:panelGrid>

        <br/>

        <!-- Submit & Reset Buttons -->
        <h:commandButton value="Add Availability" action="#{availabilityController.addAvailability}" /> &nbsp;&nbsp;&nbsp;&nbsp;
		<h:commandButton value="Reset Form" action="#{availabilityController.resetForm}" />

        <br/><br/>

        <!-- Feedback Message -->
        <h:outputText value="#{availabilityController.message}" style="color: green;" />

    </h:form>
</f:view>
</body>
</html>
