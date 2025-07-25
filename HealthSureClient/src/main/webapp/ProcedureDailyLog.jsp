<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Procedure Daily Logs</title>
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
    <h:form id="logForm">
        <h2>Procedure Daily Log</h2>

        <h:panelGrid columns="2" cellpadding="5">
            <h:outputText value="Procedure ID:" />
            <h:outputText value="#{medicalHistoryController.medicalProcedure.procedureId}" />

            <h:outputText value="Diagnosis:" />
            <h:outputText value="#{medicalHistoryController.medicalProcedure.diagnosis}" />

            <h:outputText value="Recipient:" />
            <h:outputText value="#{medicalHistoryController.medicalProcedure.recipient.firstName} #{medicalHistoryController.medicalProcedure.recipient.lastName}" />
        </h:panelGrid>

        <br/><hr/><br/>

        <h3>Daily Logs</h3>
        <h:outputText value="Total Records : #{medicalHistoryController.totalLogRecords}"></h:outputText>

        <h:dataTable value="#{medicalHistoryController.paginatedLogs}" var="log" border="1" cellpadding="5" style="width:100%">
            
            <!-- Date Column -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortLogsBy('logDate')}">
                        <span>Date
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.logSortColumn eq 'logDate' ? (medicalHistoryController.logSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{log.logDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <!-- Notes Column -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortLogsBy('notes')}">
                        <span>Notes
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.logSortColumn eq 'notes' ? (medicalHistoryController.logSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{log.notes}" />
            </h:column>

            <!-- Vital Signs Column -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortLogsBy('vitals')}">
                        <span>Vital Signs
                            <h:outputText styleClass="arrow"
                                          value="#{medicalHistoryController.logSortColumn eq 'vitals' ? (medicalHistoryController.logSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{log.vitals}" />
            </h:column>
        </h:dataTable>

        <h:panelGroup layout="block" styleClass="pagination-panel">
            <h:commandButton value="First" action="#{medicalHistoryController.goToFirstLogPage}"
                             disabled="#{medicalHistoryController.logCurrentPage == 1}" />
            <h:commandButton value="Previous" action="#{medicalHistoryController.goToPreviousLogPage}"
                             disabled="#{medicalHistoryController.logCurrentPage == 1}" />
            <h:outputText value="Page #{medicalHistoryController.logCurrentPage} of #{medicalHistoryController.totalLogPages}" />
            <h:commandButton value="Next" action="#{medicalHistoryController.goToNextLogPage}"
                             disabled="#{medicalHistoryController.logCurrentPage == medicalHistoryController.totalLogPages}" />
            <h:commandButton value="Last" action="#{medicalHistoryController.goToLastLogPage}"
                             disabled="#{medicalHistoryController.logCurrentPage == medicalHistoryController.totalLogPages}" />
        </h:panelGroup>

        <br/><br/>
        <h:commandButton value="Back to Procedures" action="MedicalProcedureSearch?faces-redirect=true" />
    </h:form>
</f:view>
</body>
</html>
