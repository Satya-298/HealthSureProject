<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Add Doctor Availability</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            display: flex;
            justify-content: center;
        }

        .container {
            background-color: #fff;
            padding: 20px 25px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 800px;
            width: 100%;
        }

        .form-title {
            font-size: 22px;
            font-weight: bold;
            color: #1d4ed8;
            text-align: center;
            margin-bottom: 20px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        .form-group.full-width {
            grid-column: span 2;
        }

        label {
            font-weight: 600;
            margin-bottom: 5px;
        }

        input[type="text"],
        select,
        textarea {
            padding: 8px 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }

        input[readonly] {
            background-color: #f9fafb;
        }

        .error-message {
            color: red;
            font-size: 12px;
            margin-top: 4px;
            min-height: 14px;
        }

        .form-footer {
            margin-top: 25px;
            text-align: center;
        }

        .btn {
            padding: 8px 20px;
            font-size: 14px;
            margin: 0 10px;
            border-radius: 6px;
            border: none;
            cursor: pointer;
        }

        .btn-primary {
            background-color: #2563eb;
            color: white;
        }

        .btn-primary:hover {
            background-color: #1d4ed8;
        }

        .btn-secondary {
            background-color: #6b7280;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #4b5563;
        }

        .success-message {
            color: green;
            font-weight: bold;
            text-align: center;
            margin-top: 15px;
            min-height: 20px;
        }
    </style>
</head>

<body>
<f:view>
    <h:form id="availabilityForm">
        <div class="container">

            <div class="form-title">Add Doctor Availability</div>

            <h:messages globalOnly="true" layout="table" styleClass="error-message" />

            <div class="form-grid">

                <div class="form-group">
                    <label>Availability ID:</label>
                    <h:inputText id="availabilityId" value="#{availabilityController.availability.availabilityId}" readonly="true" />
                    <div class="error-message">&nbsp;</div>
                </div>

                <div class="form-group">
                    <label>Doctor ID:</label>
                    <h:inputText id="doctorId" value="#{availabilityController.doctorId}" />
                    <h:message for="doctorId" styleClass="error-message" />
                </div>

                <div class="form-group">
                    <label>Available Date:</label>
                    <h:inputText id="date" value="#{availabilityController.availability.availableDate}">
                        <f:convertDateTime pattern="yyyy-MM-dd" />
                    </h:inputText>
                    <h:message for="date" styleClass="error-message" />
                </div>

                <div class="form-group">
                    <label>Start Time (HH:mm):</label>
                    <h:inputText id="startTime" value="#{availabilityController.availability.startTime}">
                        <f:convertDateTime pattern="HH:mm" />
                    </h:inputText>
                    <h:message for="startTime" styleClass="error-message" />
                </div>

                <div class="form-group">
                    <label>End Time (HH:mm):</label>
                    <h:inputText id="endTime" value="#{availabilityController.availability.endTime}">
                        <f:convertDateTime pattern="HH:mm" />
                    </h:inputText>
                    <h:message for="endTime" styleClass="error-message" />
                </div>

                <div class="form-group">
                    <label>Slot Type:</label>
                    <h:selectOneMenu id="slotType" value="#{availabilityController.availability.slotType}">
                        <f:selectItem itemValue="" itemLabel="-- Select Slot Type --" noSelectionOption="true" />
                        <f:selectItem itemValue="STANDARD" itemLabel="Standard" />
                        <f:selectItem itemValue="ADHOC" itemLabel="Adhoc" />
                    </h:selectOneMenu>
                    <h:message for="slotType" styleClass="error-message" />
                </div>

                <div class="form-group">
                    <label>Recurring:</label>
                    <h:selectBooleanCheckbox id="recurring" value="#{availabilityController.availability.recurring}" disabled="true" />
                    <h:inputHidden value="#{availabilityController.availability.recurring}" />
                    <div class="error-message">&nbsp;</div>
                </div>

                <div class="form-group">
                    <label>Total Slots:</label>
                    <h:inputText id="totalSlots" value="#{availabilityController.availability.totalSlots}" />
                    <h:message for="totalSlots" styleClass="error-message" />
                </div>

                <div class="form-group full-width">
                    <label>Notes:</label>
                    <h:inputTextarea id="notes" value="#{availabilityController.availability.notes}" rows="3" cols="40" />
                    <h:message for="notes" styleClass="error-message" />
                </div>

            </div>

            <div class="form-footer">
                <h:commandButton value="Add Availability" action="#{availabilityController.addAvailability}" styleClass="btn btn-primary" />
                <h:commandButton value="Reset Form" action="#{availabilityController.resetForm}" immediate="true" styleClass="btn btn-secondary" />
            </div>

            <div class="success-message">
                <h:outputText value="#{availabilityController.message}" />
            </div>

        </div>
    </h:form>
</f:view>
</body>
</html>
