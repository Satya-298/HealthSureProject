<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Prescriptions</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        td, th {
            padding: 8px;
            vertical-align: top;
            word-break: break-word;
        }
        th {
            background-color: #f1f5f9;
            font-weight: bold;
        }
    </style>
</head>

<body class="bg-gray-100 min-h-screen py-8 px-4">
<f:view>
<h:form id="prescriptionForm" styleClass="bg-white max-w-6xl mx-auto p-8 rounded-lg shadow-md">

    <h2 class="text-2xl font-bold text-center text-blue-600 mb-6">
    	Prescriptions for Procedure ID <h:outputText value="#{medicalHistoryController.medicalProcedure.procedureId}" />
	</h2>

    <!-- Back Button -->
    <div class="mb-4">
        <h:commandButton value="Back to Procedures"
                         action="MedicalProcedureSearch?faces-redirect=true"
                         styleClass="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-800"
                         immediate="true" />
    </div>

    <!-- Prescription Table -->
    <div class="overflow-x-auto">
        <h:dataTable value="#{medicalHistoryController.paginatedPrescriptions}" var="presc"
                     styleClass="w-full border border-gray-300 rounded text-center"
                     rendered="#{not empty medicalHistoryController.prescriptionList}">

            <!-- Prescription ID -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Prescription ID"
                                   action="#{medicalHistoryController.sortPrescriptionsBy('prescriptionId')}" />
                </f:facet>
                <h:outputText value="#{presc.prescriptionId}" />
            </h:column>

            <!-- Written On -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Written On"
                                   action="#{medicalHistoryController.sortPrescriptionsBy('writtenOn')}" />
                </f:facet>
                <h:outputText value="#{presc.writtenOn}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <!-- Start Date -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Start Date"
                                   action="#{medicalHistoryController.sortPrescriptionsBy('startDate')}" />
                </f:facet>
                <h:outputText value="#{presc.startDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <!-- End Date -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink value="End Date"
                                   action="#{medicalHistoryController.sortPrescriptionsBy('endDate')}" />
                </f:facet>
                <h:outputText value="#{presc.endDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <!-- Doctor Name -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Doctor"
                                   action="#{medicalHistoryController.sortPrescriptionsBy('doctorName')}" />
                </f:facet>
                <h:outputText value="#{presc.doctor.doctorName}" />
            </h:column>
        </h:dataTable>
    </div>

    <!-- Pagination Controls -->
    <div class="mt-6 flex justify-center items-center gap-8">
        <!-- Previous -->
        <h:commandLink action="#{medicalHistoryController.previousPrescriptionPage}"
                       rendered="#{medicalHistoryController.prescriptionCurrentPage > 0}"
                       styleClass="text-blue-600 hover:underline">
            <h:outputText value="Previous" />
        </h:commandLink>

        <!-- Page Indicator -->
        <h:outputText value="Page #{medicalHistoryController.prescriptionCurrentPage + 1} of #{medicalHistoryController.totalPrescriptionPages}" />

        <!-- Next -->
        <h:commandLink action="#{medicalHistoryController.nextPrescriptionPage}"
                       rendered="#{medicalHistoryController.prescriptionCurrentPage + 1 < medicalHistoryController.totalPrescriptionPages}"
                       styleClass="text-blue-600 hover:underline">
            <h:outputText value="Next" />
        </h:commandLink>
    </div>

</h:form>
</f:view>
</body>
</html>
