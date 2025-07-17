<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Prescribed Medicines</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            padding: 24px;
            min-height: 100vh;
        }

        .container {
            background-color: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            max-width: 1100px;
            margin: auto;
        }

        h2 {
            text-align: center;
            color: #1d4ed8;
            font-size: 24px;
            margin-bottom: 24px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
        }

        th, td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f9fafb;
            color: #2563eb;
            cursor: pointer;
        }

        th:hover {
            text-decoration: underline;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 24px;
            margin-top: 24px;
        }

        .link {
            color: #2563eb;
            text-decoration: none;
        }

        .link:hover {
            text-decoration: underline;
        }

        .back-btn {
            margin-bottom: 16px;
            display: inline-block;
            color: #2563eb;
            text-decoration: none;
            font-weight: bold;
        }

        .back-btn:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<f:view>
<h:form styleClass="container">

    <h:commandLink action="MedicalProcedureSearch" styleClass="back-btn">
        <h:outputText value="Back to Procedure List" />
    </h:commandLink>

    <h2>Prescribed Medicines</h2>

    <h:dataTable value="#{medicalHistoryController.paginatedMedicines}" var="med"
                 rendered="#{not empty medicalHistoryController.selectedPrescription.prescribedMedicines}">

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Medicine Name"
                               action="#{medicalHistoryController.sortMedicinesBy('medicineName')}" />
            </f:facet>
            <h:outputText value="#{med.medicineName}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Dosage"
                               action="#{medicalHistoryController.sortMedicinesBy('dosage')}" />
            </f:facet>
            <h:outputText value="#{med.dosage}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Frequency"
                               action="#{medicalHistoryController.sortMedicinesBy('frequency')}" />
            </f:facet>
            <h:outputText value="#{med.frequency}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Duration"
                               action="#{medicalHistoryController.sortMedicinesBy('duration')}" />
            </f:facet>
            <h:outputText value="#{med.duration}" />
        </h:column>

        <h:column>
            <f:facet name="header">Instructions</f:facet>
            <h:outputText value="#{med.instructions}" />
        </h:column>

    </h:dataTable>

    <div class="pagination">
        <h:commandLink action="#{medicalHistoryController.previousMedicinePage}"
                       rendered="#{not medicalHistoryController.medicinePreviousDisabled}"
                       styleClass="link">
            Previous
        </h:commandLink>

        <h:outputText value="Page #{medicalHistoryController.medicineCurrentPage + 1} of #{medicalHistoryController.totalMedicinePages}" />

        <h:commandLink action="#{medicalHistoryController.nextMedicinePage}"
                       rendered="#{not medicalHistoryController.medicineNextDisabled}"
                       styleClass="link">
            Next
        </h:commandLink>
    </div>

</h:form>
</f:view>
</body>
</html>
