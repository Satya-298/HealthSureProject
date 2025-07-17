<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Update Doctor Availability</title>
    <style>
        body {
            background-color: #f3f4f6;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem;
            font-family: Arial, sans-serif;
        }

        .form-box {
            width: 100%;
            max-width: 800px;
            background-color: white;
            padding: 2.5rem;
            border-radius: 1rem;
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
        }

        h2 {
            text-align: center;
            color: #1d4ed8;
            font-size: 1.8rem;
            font-weight: bold;
            margin-bottom: 1.5rem;
        }

        label {
            font-weight: 600;
            color: #374151;
            display: block;
            margin-bottom: 4px;
        }

        input[type="text"],
        select,
        textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            margin-top: 4px;
        }

        textarea {
            resize: vertical;
        }

        .grid-2 {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 24px;
        }

        .full-span {
            grid-column: span 2;
        }

        .mt-1 {
            margin-top: 0.25rem;
        }

        .btn {
            padding: 10px 24px;
            font-weight: 600;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .btn-blue {
            background-color: #2563eb;
            color: white;
        }

        .btn-blue:hover {
            background-color: #1e3a8a;
        }

        .btn-gray {
            background-color: #6b7280;
            color: white;
        }

        .btn-gray:hover {
            background-color: #4b5563;
        }

        .btn-group {
            margin-top: 2rem;
            display: flex;
            justify-content: center;
            gap: 24px;
        }

        .message {
            text-align: center;
            color: #059669;
            font-weight: 600;
            margin-top: 1.5rem;
        }

        .error {
            color: red;
            font-size: 13px;
            margin-top: 4px;
            display: block;
        }
    </style>
</head>
<body>
<f:view>
<h:form id="availabilityForm" styleClass="form-box">

    <h2>Update Doctor Availability</h2>

    <div class="grid-2">

        <!-- Availability ID -->
        <div>
            <h:outputLabel for="availabilityId">
                <span style="color:red">*</span>Availability ID:
            </h:outputLabel>
            <div class="mt-1">
                <h:outputText value="#{availabilityController.selectedAvailability.availabilityId}" />
            </div>
        </div>

        <!-- Doctor ID -->
        <div>
            <h:outputLabel for="doctorId">
                <span style="color:red">*</span>Doctor ID:
            </h:outputLabel>
            <div class="mt-1">
                <h:outputText value="#{availabilityController.selectedAvailability.doctor.doctorId}" />
            </div>
        </div>

        <!-- Date -->
        <div>
            <h:outputLabel for="date">
                <span style="color:red">*</span>Available Date:
            </h:outputLabel>
            <h:inputText id="date" value="#{availabilityController.selectedAvailability.availableDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:inputText>
            <h:message for="date" styleClass="error" />
        </div>

        <!-- Start Time -->
        <div>
            <h:outputLabel for="startTime">
                <span style="color:red">*</span>Start Time (HH:mm):
                    </h:outputLabel>
            <h:inputText id="startTime" value="#{availabilityController.selectedAvailability.startTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:inputText>
            <h:message for="startTime" styleClass="error" />
        </div>

        <!-- End Time -->
        <div>
			<h:outputLabel for="endTime">
                <span style="color:red">*</span>End Time (HH:mm):
       	    </h:outputLabel>
       	    <h:inputText id="endTime" value="#{availabilityController.selectedAvailability.endTime}">
                <f:convertDateTime pattern="HH:mm" />
            </h:inputText>
            <h:message for="endTime" styleClass="error" />
        </div>

        <!-- Slot Type -->
        <div>
            <h:outputLabel for="slotType">
                <span style="color:red">*</span>Slot Type:
            </h:outputLabel>
            <h:selectOneMenu id="slotType" value="#{availabilityController.selectedAvailability.slotType}">
                <f:selectItem itemValue="STANDARD" itemLabel="Standard" />
                <f:selectItem itemValue="ADHOC" itemLabel="Adhoc" />
            </h:selectOneMenu>
            <h:message for="slotType" styleClass="error" />
        </div>

        <!-- Recurring -->
        <div>
			<h:outputLabel for="recurring">
            Recurring:
            <span style="font-size: 0.75rem; color: #6b7280; font-weight: normal;">
            (Auto-Check/Uncheck as per SlotType)
            </span>
           	</h:outputLabel>
           	<h:selectBooleanCheckbox id="recurring" value="#{availabilityController.selectedAvailability.recurring}" disabled="true" />
        </div>

        <!-- Total Slots -->
        <div>
			<h:outputLabel for="totalSlots">
                 <span style="color:red">*</span>Total Slots:
            </h:outputLabel>
            <h:inputText id="totalSlots" value="#{availabilityController.selectedAvailability.totalSlots}" />
            <h:message for="totalSlots" styleClass="error" />
        </div>

        <!-- Notes -->
        <div class="full-span">
            <h:outputLabel for="notes">Notes (Optional):</h:outputLabel>
            <h:inputTextarea id="notes" value="#{availabilityController.selectedAvailability.notes}" rows="3" cols="30" />
        </div>
    </div>

    <!-- Buttons -->
    <div class="btn-group">
        <h:commandButton value="Update"
                         action="#{availabilityController.updateAvailability}"
                         styleClass="btn btn-blue" />
        <h:commandButton value="Discard"
                         action="#{availabilityController.resetUpdateForm}"
                         styleClass="btn btn-gray" />
    </div>

    <!-- Message -->
    <div class="message">
        <h:outputText value="#{availabilityController.message}" />
    </div>

</h:form>
</f:view>
</body>
</html>
