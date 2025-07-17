<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Add Doctor Availability</title>
    <style>
        body {
            background-color: #f3f4f6;
            min-height: 100vh;
            padding: 2rem;
            margin: 0;
            font-family: Arial, sans-serif;
        }

        h1 {
            font-size: 2rem;
            font-weight: 800;
            text-align: center;
            color: #1e40af;
            margin-bottom: 1.5rem;
        }

        .center {
            display: flex;
            justify-content: center;
        }

        .form-container {
            background-color: #ffffff;
            padding: 2rem;
            border-radius: 0.5rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 960px;
        }

        .form-container h2 {
            font-size: 1.25rem;
            font-weight: bold;
            text-align: center;
            color: #2563eb;
            margin-bottom: 1.5rem;
        }

        .messages {
            text-align: center;
            color: #dc2626;
            font-size: 0.875rem;
            margin-bottom: 1rem;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 1.5rem;
        }

        @media (min-width: 768px) {
            .form-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .full-width {
                grid-column: span 2;
            }
        }

        label, .form-grid h\:outputLabel {
            font-weight: 600;
            margin-bottom: 0.5rem;
            display: block;
        }

        input[type="text"], select, textarea {
            width: 100%;
            padding: 0.5rem 0.75rem;
            border: 1px solid #d1d5db;
            border-radius: 0.375rem;
            font-size: 0.9rem;
        }

        input[type="date"], input[type="time"], input[type="number"] {
            padding: 0.5rem 0.75rem;
            border: 1px solid #d1d5db;
            border-radius: 0.375rem;
            font-size: 0.9rem;
            width: 100%;
        }

        input[readonly] {
            background-color: #f9fafb;
        }

        .checkbox-wrapper {
            display: flex;
            align-items: center;
            height: 2.6rem;
        }

        .error-message {
            color: #dc2626;
            font-size: 0.875rem;
            margin-top: 0.25rem;
            display: block;
        }

        .form-actions {
            text-align: center;
            margin-top: 2rem;
        }

        .btn {
            padding: 0.5rem 1.25rem;
            font-size: 0.9rem;
            border-radius: 0.375rem;
            border: none;
            cursor: pointer;
            margin: 0 0.5rem;
        }

        .btn-primary {
            background-color: #2563eb;
            color: #fff;
        }

        .btn-primary:hover {
            background-color: #1d4ed8;
        }

        .btn-secondary {
            background-color: #6b7280;
            color: #fff;
        }

        .btn-secondary:hover {
            background-color: #4b5563;
        }

        .popup-box {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #d1fae5;
            color: #065f46;
            border: 1px solid #10b981;
            padding: 1rem 2rem;
            border-radius: 0.5rem;
            font-weight: 600;
            font-size: 1rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
            display: flex;
            align-items: center;
            gap: 1rem;
            z-index: 9999;
        }

        .popup-box.hidden {
            display: none;
        }

        .spinner {
            border: 4px solid #6ee7b7;
            border-top: 4px solid transparent;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>

<body>
<f:view>
    <h1>Add Doctor Availability</h1>

    <div class="center">
        <h:form id="availabilityForm" styleClass="form-container">
            <h2>Availability Form</h2>

            <h:inputHidden id="hiddenMsg" value="#{availabilityController.message}" />

            <div id="popupBox" class="popup-box hidden">
                <span id="popupMessage">Adding...</span>
                <div id="spinner" class="spinner"></div>
            </div>

            <div class="form-grid">
                <div>
                    <h:outputLabel for="availabilityId">
                        <span style="color:red">*</span>Availability ID:
                    </h:outputLabel>
                    <h:inputText id="availabilityId" value="#{availabilityController.availability.availabilityId}" readonly="true" />
                </div>

                <div>
                    <h:outputLabel for="doctorId">
                        <span style="color:red">*</span>Doctor ID:
                    </h:outputLabel>
                    <h:inputText id="doctorId" value="#{availabilityController.doctorId}" />
                    <h:message for="doctorId" styleClass="error-message" />
                </div>

                <div>
                    <h:outputLabel for="date">
                        <span style="color:red">*</span>Available Date:
                    </h:outputLabel>
                    <h:inputText id="date" value="#{availabilityController.availability.availableDate}">
                        <f:convertDateTime pattern="yyyy-MM-dd" />
                    </h:inputText>
                    <h:message for="date" styleClass="error-message" />
                </div>

                <div>
                    <h:outputLabel for="startTime">
                        <span style="color:red">*</span>Start Time (HH:mm):
                    </h:outputLabel>
                    <h:inputText id="startTime" value="#{availabilityController.availability.startTime}">
                        <f:convertDateTime pattern="HH:mm" />
                    </h:inputText>
                    <h:message for="startTime" styleClass="error-message" />
                </div>

                <div>
                    <h:outputLabel for="endTime">
                        <span style="color:red">*</span>End Time (HH:mm):
                    </h:outputLabel>
                    <h:inputText id="endTime" value="#{availabilityController.availability.endTime}">
                        <f:convertDateTime pattern="HH:mm" />
                    </h:inputText>
                    <h:message for="endTime" styleClass="error-message" />
                </div>

                <div>
                    <h:outputLabel for="slotType">
                        <span style="color:red">*</span>Slot Type:
                    </h:outputLabel>
                    <h:selectOneMenu id="slotType" value="#{availabilityController.availability.slotType}">
                        <f:selectItem itemValue="" itemLabel="-- Select Slot Type --" noSelectionOption="true" />
                        <f:selectItem itemValue="STANDARD" itemLabel="Standard" />
                        <f:selectItem itemValue="ADHOC" itemLabel="Adhoc" />
                    </h:selectOneMenu>
                    <h:message for="slotType" styleClass="error-message" />
                </div>

                <div class="checkbox-wrapper">
                    <h:outputLabel for="recurring">
                        Recurring:
                        <span style="font-size: 0.75rem; color: #6b7280; font-weight: normal;">
                            (Auto-Check/Uncheck as per SlotType)
                        </span>
                    </h:outputLabel>
                    <h:selectBooleanCheckbox id="recurring" value="#{availabilityController.availability.recurring}" disabled="true" />
                    <h:inputHidden value="#{availabilityController.availability.recurring}" />
                </div>

                <div>
                    <h:outputLabel for="totalSlots">
                        <span style="color:red">*</span>Total Slots:
                    </h:outputLabel>
                    <h:inputText id="totalSlots" value="#{availabilityController.availability.totalSlots}" />
                    <h:message for="totalSlots" styleClass="error-message" />
                </div>

                <div class="full-width">
                    <h:outputLabel for="notes">Notes (Optional):</h:outputLabel>
                    <h:inputTextarea id="notes" value="#{availabilityController.availability.notes}" rows="3" cols="40" />
                </div>
            </div>

            <div class="form-actions">
                <h:commandButton value="Add Availability" action="#{availabilityController.addAvailability}" styleClass="btn btn-primary" />
                <h:commandButton value="Reset Form" action="#{availabilityController.resetForm}" immediate="true" styleClass="btn btn-secondary" />
                <h:commandButton value="Back" action="#{availabilityController.resetAddFormBackButton}" immediate="true" styleClass="btn btn-secondary" />
            </div>
        </h:form>
    </div>

    <script>
	document.addEventListener("DOMContentLoaded", function () {
	    const popupBox = document.getElementById("popupBox");
	    const popupMessage = document.getElementById("popupMessage");
	    const spinner = document.getElementById("spinner");
	    const hiddenMsg = document.getElementById("availabilityForm:hiddenMsg");
	
	    if (hiddenMsg && hiddenMsg.value && hiddenMsg.value.trim() === "Availability Added Successfully...") {
	        popupBox.classList.remove("hidden");
	        popupMessage.textContent = hiddenMsg.value;
	        spinner.style.display = "none";

	        // Hide popup after 3 seconds
	        setTimeout(() => {
	            popupBox.classList.add("hidden");
	        }, 3000);
	    }
	
	    const calendarInput = document.querySelector("#availabilityForm\\:date");
	    if (calendarInput) {
	        calendarInput.setAttribute("type", "date");
	        const today = new Date().toISOString().split("T")[0];
	        calendarInput.setAttribute("min", today);
	    }
	
	    const startTime = document.querySelector("#availabilityForm\\:startTime");
	    const endTime = document.querySelector("#availabilityForm\\:endTime");
	    if (startTime) startTime.setAttribute("type", "time");
	    if (endTime) endTime.setAttribute("type", "time");
	
	    const totalSlotsInput = document.querySelector("#availabilityForm\\:totalSlots");
	    if (totalSlotsInput) {
	        totalSlotsInput.setAttribute("type", "number");
	        totalSlotsInput.setAttribute("maxlength", "3");
	        totalSlotsInput.addEventListener("input", function () {
	            if (this.value.length > 3) {
	                this.value = this.value.slice(0, 3);
	            }
	        });
	    }
	});
	</script>

</f:view>
</body>
</html>
