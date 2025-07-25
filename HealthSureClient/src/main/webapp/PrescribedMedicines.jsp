<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Prescribed Medicines</title>
    <style>
        .arrow {
            padding-left: 5px;
            font-size: 14px;
        }
        .pagination-panel {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<f:view>
    <h:form>
        <h2>Prescribed Medicines</h2>

       	<h:outputText value="Total Records : #{medicalHistoryController.totalMedicineRecords}" />

        <h:dataTable value="#{medicalHistoryController.paginatedMedicines}" var="med" border="1">

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('prescribedId')}">
                        <span>Prescribed ID
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'prescribedId' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.prescribedId}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('medicineName')}">
                        <span>Medicine Name
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'medicineName' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.medicineName}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('dosage')}">
                        <span>Dosage
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'dosage' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.dosage}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('type')}">
                        <span>Type
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'type' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.type}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('duration')}">
                        <span>Duration
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'duration' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.duration}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('startDate')}">
                        <span>Start Date
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'startDate' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.startDate}">
                    <f:convertDateTime pattern="dd-MM-yyyy" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortMedicinesBy('endDate')}">
                        <span>End Date
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.medicineSortColumn eq 'endDate' ? (medicalHistoryController.medicineSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{med.endDate}">
                    <f:convertDateTime pattern="dd-MM-yyyy" />
                </h:outputText>
            </h:column>

        </h:dataTable>

        <h:panelGroup layout="block" styleClass="pagination-panel">
            <h:commandButton value="First" action="#{medicalHistoryController.goToFirstMedicinePage}"
                             disabled="#{medicalHistoryController.medicineCurrentPage == 1}" />
            <h:commandButton value="Previous" action="#{medicalHistoryController.previousMedicinePage}"
                             disabled="#{medicalHistoryController.medicineCurrentPage == 1}" />
            <h:outputText value="Page #{medicalHistoryController.medicineCurrentPage} of #{medicalHistoryController.medicineTotalPages}" />
            <h:commandButton value="Next" action="#{medicalHistoryController.nextMedicinePage}"
                             disabled="#{medicalHistoryController.medicineCurrentPage == medicalHistoryController.medicineTotalPages}" />
            <h:commandButton value="Last" action="#{medicalHistoryController.goToLastMedicinePage}"
                             disabled="#{medicalHistoryController.medicineCurrentPage == medicalHistoryController.medicineTotalPages}" />
        </h:panelGroup>
    </h:form>
</f:view>
</body>
</html>
