<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
<head>
<title>Search Medical Procedures</title>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: #f4f6f8;
    margin: 20px;
}

.form-panel, .results-panel {
    background-color: white;
    border: 1px solid #ccc;
    padding: 16px;
    margin-bottom: 20px;
    border-radius: 6px;
}

.error-message {
    color: red;
    font-size: 0.9em;
}

label {
    display: inline-block;
    width: 140px;
    margin-bottom: 8px;
    font-weight: bold;
}

input[type="text"], select {
    padding: 6px;
    width: 220px;
    margin-bottom: 10px;
}

.btn {
    padding: 10px 20px;
    margin-top: 10px;
    background-color: #007bff;
    border: none;
    color: white;
    cursor: pointer;
    border-radius: 4px;
    font-size: 16px;
}

.btn:hover {
    background-color: #0056b3;
}

.btn-spacing {
    margin-right: 10px;
}

.center-wrapper {
    display: flex;
    justify-content: center;
    overflow-x: auto;
}

.results-table {
    border-collapse: collapse;
    min-width: 1000px;
    background-color: white;
    text-align: center;
}

.pagination-panel {
    text-align: center;
}

.menu-link.cyan {
    background-color: #06b6d4; /* cyan-500 */
}

.menu-link.cyan:hover {
    background-color: #0891b2; /* cyan-600 */
}
</style>
</head>

<body>
    <f:view>
        <h:form id="historyForm">
            <div class="form-panel">
                <h2>Search Medical Procedures</h2>

                <!-- Form Inputs -->
                <h:panelGrid columns="2">
                <div>
                    <h:outputLabel for="doctorId">
                            Doctor ID:<span style="color: red">*</span>
                    </h:outputLabel>
                </div>
                    <h:inputText id="doctorId"
                        value="#{medicalHistoryController.doctorId}" />
                    <br/>
                    <h:message for="doctorId" style="color:red" />
                </h:panelGrid>

                <h:panelGrid columns="2">
                    <div>
                        <h:outputLabel for="procedureType">
                            Procedure Type:<span style="color: red">*</span>
                        </h:outputLabel>
                    </div>

                    <br />
                    <h:selectOneRadio id="procedureType"
                        value="#{medicalHistoryController.procedureTypeSelected}"
                        layout="lineDirection">
                        <f:selectItem itemLabel="Single Day" itemValue="SINGLE_DAY" />
                        <f:selectItem itemLabel="Long Term" itemValue="LONG_TERM" />
                    </h:selectOneRadio>
                    <br/>
                    <h:message for="procedureType" style="color:red" />
                </h:panelGrid>

                <h:panelGrid columns="2">
                    <h:outputLabel value="Search Type:" />
                    <br />
                    <h:selectOneRadio value="#{medicalHistoryController.searchType}"
                        layout="lineDirection" onchange="submit()">
                        <f:selectItem itemLabel="By HID" itemValue="hid" />
                        <f:selectItem itemLabel="By Name" itemValue="name" />
                        <f:selectItem itemLabel="By Mobile" itemValue="mobile" />
                    </h:selectOneRadio>
                    <br/>
                    <h:message for="searchType" style="color:red" />
                </h:panelGrid>

                <h:panelGroup
                    rendered="#{medicalHistoryController.searchType eq 'name'}">
                    <h:panelGrid columns="2">
                        <h:outputLabel value="Name Search Mode:" />
                        <h:selectOneRadio
                            value="#{medicalHistoryController.nameSearchMode}"
                            layout="lineDirection">
                            <f:selectItem itemLabel="Starts With" itemValue="startsWith" />
                            <f:selectItem itemLabel="Contains" itemValue="contains" />
                        </h:selectOneRadio>
                        <br/>
                        <h:message for="nameSearchMode" style="color:red" />
                    </h:panelGrid>
                </h:panelGroup>

                <h:panelGroup rendered="#{medicalHistoryController.searchType ne null}">
                    <h:panelGrid columns="2">
                        <h:outputLabel for="searchKey" value="Search Value:" />
                        
                        <!-- Mobile number input (10 digits max) -->
                        <h:inputText id="searchKeyMobile" 
                            value="#{medicalHistoryController.searchKey}"
                            maxlength="10"
                            rendered="#{medicalHistoryController.searchType eq 'mobile'}" />
                            
                        <!-- HID input (no maxlength) -->
                        <h:inputText id="searchKeyHid" 
                            value="#{medicalHistoryController.searchKey}"
                            rendered="#{medicalHistoryController.searchType eq 'hid'}" />
                            
                        <!-- Name input (no maxlength) -->
                        <h:inputText id="searchKeyName" 
                            value="#{medicalHistoryController.searchKey}"
                            rendered="#{medicalHistoryController.searchType eq 'name'}" />
                            
                        <br/>
                       	<h:message for="searchKeyHid" style="color:red" />
                        <br/>
                        <h:message for="searchKeyMobile" style="color:red" />
                        <br/>
                        <h:message for="searchKeyName" style="color:red" />
                    </h:panelGrid>
                </h:panelGroup>

                <div>
                    <h:commandButton value="Search"
                        action="#{medicalHistoryController.searchProcedures}"
                        styleClass="btn btn-spacing" />
                    <h:commandButton value="Reset"
                        action="#{medicalHistoryController.resetForm}" styleClass="btn" />
                </div>

                <h:messages globalOnly="true" style="color:red; margin-top:10px;"
                    layout="list" />
            </div>

            <!-- SINGLE DAY RESULTS -->
            <h:panelGroup
                rendered="#{medicalHistoryController.procedureTypeSelected eq 'SINGLE_DAY' and not empty medicalHistoryController.searchResults}">
                <div class="results-panel">
                    <h2 style="text-align: center;">Single Day Procedures</h2>

                    <h:outputText
                        value="Total Records : #{medicalHistoryController.totalProcedureRecords}" />

                    <div class="center-wrapper">
                        <h:dataTable value="#{medicalHistoryController.paginatedList}"
                            var="proc" border="1" cellpadding="5" styleClass="results-table">

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('procedureId')}">
                                        <span class="sort-header"> Procedure ID <h:outputText
                                                styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'procedureId' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.procedureId}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('recipientName')}">
                                        <span> Recipient Name <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'recipientName' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText
                                    value="#{proc.recipient.firstName} #{proc.recipient.lastName}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('procedureDate')}">
                                        <span> Procedure Date <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'procedureDate' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.procedureDate}">
                                    <f:convertDateTime pattern="dd-MM-yyyy" />
                                </h:outputText>
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('diagnosis')}">
                                        <span> Diagnosis <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'diagnosis' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.diagnosis}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('recommendations')}">
                                        <span> Recommendations <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'recommendations' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.recommendations}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:outputText value="Prescription" />
                                </f:facet>
                                <h:commandButton value="View Prescription"
                                    action="#{medicalHistoryController.loadPrescriptions(proc)}"
                                    styleClass="btn" />
                            </h:column>
                        </h:dataTable>
                    </div>
                    <h:panelGroup layout="block" styleClass="pagination-panel"
                        style="margin-top: 20px;">
                        <h:commandButton value="First"
                            action="#{medicalHistoryController.goToFirstProcedurePage}"
                            disabled="#{medicalHistoryController.currentPage == 1}" />
                        <h:commandButton value="Previous"
                            action="#{medicalHistoryController.previousPage}"
                            disabled="#{medicalHistoryController.currentPage == 1}" />
                        <h:outputText
                            value="Page #{medicalHistoryController.currentPage} of #{medicalHistoryController.totalPages}" />
                        <h:commandButton value="Next"
                            action="#{medicalHistoryController.nextPage}"
                            disabled="#{medicalHistoryController.currentPage == medicalHistoryController.totalPages}" />
                        <h:commandButton value="Last"
                            action="#{medicalHistoryController.goToLastProcedurePage}"
                            disabled="#{medicalHistoryController.currentPage == medicalHistoryController.totalPages}" />
                    </h:panelGroup>
                </div>
            </h:panelGroup>

            <!-- LONG TERM RESULTS -->
            <h:panelGroup
                rendered="#{medicalHistoryController.procedureTypeSelected eq 'LONG_TERM' and not empty medicalHistoryController.searchResults}">
                <div class="results-panel">
                    <h2 style="text-align: center;">Long Term Procedures</h2>

                    <h:outputText
                        value="Total Records : #{medicalHistoryController.totalProcedureRecords}" />

                    <div class="center-wrapper">
                        <h:dataTable value="#{medicalHistoryController.paginatedList}"
                            var="proc" border="1" cellpadding="5" styleClass="results-table">

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('procedureId')}">
                                        <span class="sort-header"> Procedure ID <h:outputText
                                                styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'procedureId' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.procedureId}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('recipientName')}">
                                        <span> Recipient Name <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'recipientName' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText
                                    value="#{proc.recipient.firstName} #{proc.recipient.lastName}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('scheduledDate')}">
                                        <span> Scheduled Date <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'scheduledDate' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.scheduledDate}">
                                    <f:convertDateTime pattern="dd-MM-yyyy" />
                                </h:outputText>
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('fromDate')}">
                                        <span> From Date <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'fromDate' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.fromDate}">
                                    <f:convertDateTime pattern="dd-MM-yyyy" />
                                </h:outputText>
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('toDate')}">
                                        <span> To Date <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'toDate' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.toDate}">
                                    <f:convertDateTime pattern="dd-MM-yyyy" />
                                </h:outputText>
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('diagnosis')}">
                                        <span> Diagnosis <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'diagnosis' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.diagnosis}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:commandLink
                                        action="#{medicalHistoryController.sortBy('recommendations')}">
                                        <span> Recommendations <h:outputText styleClass="arrow"
                                                value="#{medicalHistoryController.sortColumn eq 'recommendations' ? (medicalHistoryController.sortAscending ? '▲' : '▼') : ''}" />
                                        </span>
                                    </h:commandLink>
                                </f:facet>
                                <h:outputText value="#{proc.recommendations}" />
                            </h:column>

                            <h:column>
                                <f:facet name="header">
                                    <h:outputText value="Prescription" />
                                </f:facet>
                                <h:commandButton value="View Prescription"
                                    action="#{medicalHistoryController.loadPrescriptions(proc)}"
                                    styleClass="btn" />
                            </h:column>
                            <h:column>
                                <f:facet name="header">
                                    <h:outputText value="Daily Logs" />
                                </f:facet>
                                <h:commandButton value="View Daily Logs"
                                    action="#{medicalHistoryController.loadProcedureLogs(proc)}"
                                    styleClass="btn" />
                            </h:column>
                        </h:dataTable>
                    </div>

                    <!-- Pagination Controls -->
                    <h:panelGroup layout="block" styleClass="pagination-panel"
                        style="margin-top: 20px;">
                        <h:commandButton value="First"
                            action="#{medicalHistoryController.goToFirstProcedurePage}"
                            disabled="#{medicalHistoryController.currentPage == 1}" />
                        <h:commandButton value="Previous"
                            action="#{medicalHistoryController.previousProcedurePage}"
                            disabled="#{medicalHistoryController.currentPage == 1}" />
                        <h:outputText
                            value="Page #{medicalHistoryController.currentPage} of #{medicalHistoryController.totalPages}" />
                        <h:commandButton value="Next"
                            action="#{medicalHistoryController.nextPage}"
                            disabled="#{medicalHistoryController.currentPage == medicalHistoryController.totalPages}" />
                        <h:commandButton value="Last"
                            action="#{medicalHistoryController.goToLastProcedurePage}"
                            disabled="#{medicalHistoryController.currentPage == medicalHistoryController.totalPages}" />
                    </h:panelGroup>
                </div>
            </h:panelGroup>
        </h:form>
    </f:view>
</body>
</html>