<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Update Doctor Availability</title>
</head>
<body>
<f:view>
<h:form>

    <h2>Update Doctor Availability</h2>

    <h:panelGrid columns="2" cellpadding="5">

        <h:outputLabel value="Availability ID:" />
        <h:outputText value="#{availabilityController.selectedAvailability.availabilityId}" />

        <h:outputLabel value="Doctor ID:" />
        <h:outputText value="#{availabilityController.selectedAvailability.doctor.doctorId}" />

        <h:outputLabel value="Available Date:" />
        <h:inputText value="#{availabilityController.selectedAvailability.availableDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:inputText>

        <h:outputLabel value="Start Time:" />
        <h:inputText value="#{availabilityController.selectedAvailability.startTime}">
            <f:convertDateTime pattern="HH:mm" />
        </h:inputText>

        <h:outputLabel value="End Time:" />
        <h:inputText value="#{availabilityController.selectedAvailability.endTime}">
            <f:convertDateTime pattern="HH:mm" />
        </h:inputText>

        <h:outputLabel value="Slot Type:" />
        <h:selectOneMenu value="#{availabilityController.selectedAvailability.slotType}">
            <f:selectItem itemValue="STANDARD" itemLabel="Standard" />
            <f:selectItem itemValue="ADHOC" itemLabel="Adhoc" />
        </h:selectOneMenu>

        <h:outputLabel value="Recurring:" />
        <h:selectBooleanCheckbox value="#{availabilityController.selectedAvailability.recurring}" />

        <h:outputLabel value="Total Slots:" />
        <h:inputText value="#{availabilityController.selectedAvailability.totalSlots}" />

        <h:outputLabel value="Notes:" />
        <h:inputTextarea value="#{availabilityController.selectedAvailability.notes}" rows="3" cols="30" />

    </h:panelGrid>

    <br/>
    <h:commandButton value="Update" action="#{availabilityController.updateAvailability}" />
    <br/><br/>

    <h:outputText value="#{availabilityController.message}" style="color:green;" />

</h:form>
</f:view>
</body>
</html>
