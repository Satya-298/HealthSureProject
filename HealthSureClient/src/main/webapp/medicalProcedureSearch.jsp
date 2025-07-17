<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Search Medical Procedures</title>
    <style>
        body {
            background-color: #f3f4f6;
            min-height: 100vh;
            padding: 2rem;
            font-family: Arial, sans-serif;
        }

        .form-container {
            background-color: white;
            max-width: 1200px;
            margin: auto;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        h2.title {
            text-align: center;
            font-size: 1.75rem;
            font-weight: bold;
            color: #2563eb;
            margin-bottom: 1.5rem;
        }

        .input-group {
            margin-bottom: 1rem;
        }

        input[type="text"] {
            padding: 10px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
            width: 250px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-weight: 600;
            cursor: pointer;
        }

        .btn-blue {
            background-color: #2563eb;
            color: white;
        }

        .btn-blue:hover {
            background-color: #1d4ed8;
        }

        .btn-gray {
            background-color: #6b7280;
            color: white;
        }

        .btn-gray:hover {
            background-color: #4b5563;
        }

        .error-message {
            color: #dc2626;
            font-size: 0.85rem;
            margin-top: 0.25rem;
        }

        .table-container {
            margin-top: 2rem;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
        }

        th, td {
            padding: 10px;
            text-align: center;
            vertical-align: top;
        }

        th {
            background-color: #f1f5f9;
            font-weight: bold;
            border-bottom: 1px solid #d1d5db;
        }

        .pagination {
            margin-top: 1.5rem;
            text-align: center;
        }

        .pagination a {
            margin: 0 1rem;
            color: #2563eb;
            font-weight: bold;
            text-decoration: none;
        }

        .searching-popup {
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

        .searching-popup.hidden {
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
            to {
                transform: rotate(360deg);
            }
        }
    </style>

    <script>
        function showSearchingBox() {
            document.getElementById("searchingBox").classList.remove("hidden");
        }

        function hideSearchingBox() {
            document.getElementById("searchingBox").classList.add("hidden");
        }

        window.addEventListener('load', function () {
            hideSearchingBox();
        });
    </script>
</head>
<body>
<f:view>
    <h:form id="historyForm" styleClass="form-container">
        <h2 class="title">Search Medical Procedures</h2>

        <!-- Searching Box -->
        <div id="searchingBox" class="searching-popup hidden">
            <span>Searching...</span>
            <div class="spinner"></div>
        </div>

        <!-- Doctor ID -->
        <div class="input-group">
            <h:outputLabel for="doctorId" value="Doctor ID:" />
            <h:inputText id="doctorId" value="#{medicalHistoryController.doctorId}" />
            <h:message for="doctorId" styleClass="error-message" />
        </div>

        <!-- Search Type -->
        <div class="input-group">
            <h:outputLabel for="searchType" value="Select Search Type (optional):" />
            <h:selectOneRadio id="searchType" value="#{medicalHistoryController.searchType}" layout="lineDirection" onclick="this.form.submit();">
                <f:selectItem itemValue="hid" itemLabel="Search by HID" />
                <f:selectItem itemValue="name" itemLabel="Search by Name" />
                <f:selectItem itemValue="mobile" itemLabel="Search by Phone" />
            </h:selectOneRadio>
            <h:message for="searchType" styleClass="error-message" />
        </div>

        <!-- Name Search Mode -->
        <h:panelGroup rendered="#{medicalHistoryController.searchType eq 'name'}">
            <div class="input-group">
                <h:outputLabel for="nameSearchMode" value="Name Search Mode:" />
                <h:selectOneRadio id="nameSearchMode" value="#{medicalHistoryController.nameSearchMode}" layout="lineDirection">
                    <f:selectItem itemValue="startsWith" itemLabel="Starts With" />
                    <f:selectItem itemValue="contains" itemLabel="Contains" />
                </h:selectOneRadio>
                <h:message for="nameSearchMode" styleClass="error-message" />
            </div>
        </h:panelGroup>

        <!-- Search Key -->
        <div class="input-group">
            <h:outputLabel for="searchKey" value="Enter Search Value:" />
            <h:inputText id="searchKey" value="#{medicalHistoryController.searchKey}" />
            <h:message for="searchKey" styleClass="error-message" />
        </div>

        <!-- Buttons -->
        <div class="input-group" style="margin-top: 1.5rem;">
            <h:commandButton value="Search" action="#{medicalHistoryController.searchProcedures}" onclick="showSearchingBox()" styleClass="btn btn-blue" />
            <h:commandButton value="Reset" action="#{medicalHistoryController.resetForm}" styleClass="btn btn-gray" immediate="true" />
        </div>

        <!-- Global Messages -->
        <h:messages globalOnly="true" styleClass="error-message" />

        <!-- Result Table -->
        <div class="table-container">
            <h:dataTable value="#{medicalHistoryController.paginatedList}" var="proc" rendered="#{not empty medicalHistoryController.searchResults}">
                
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('procedureId')}">
                            <h:outputText value="Procedure ID" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.procedureId}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('procedureDate')}">
                            <h:outputText value="Procedure Date" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.procedureDate}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('type')}">
                            <h:outputText value="Type" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.type}" />
                </h:column>

                <h:column rendered="#{medicalHistoryController.showDurationDates}">
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('fromDate')}">
                            <h:outputText value="From Date" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.fromDate}" />
                </h:column>

                <h:column rendered="#{medicalHistoryController.showDurationDates}">
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('toDate')}">
                            <h:outputText value="To Date" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.toDate}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('diagnosis')}">
                            <h:outputText value="Diagnosis" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.diagnosis}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('recommendations')}">
                            <h:outputText value="Recommendations" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.recommendations}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortBy('recipientName')}">
                            <h:outputText value="Recipient" />
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{proc.recipient.firstName} #{proc.recipient.lastName}" />
                </h:column>

            </h:dataTable>
        </div>

        <!-- Pagination -->
        <div class="pagination">
            <h:commandLink action="#{medicalHistoryController.previousPage}" rendered="#{!medicalHistoryController.previousButtonDisabled}">
                <h:outputText value="Previous" />
            </h:commandLink>

            <h:outputText value="Page #{medicalHistoryController.currentPage + 1} of #{medicalHistoryController.totalPages}" />

            <h:commandLink action="#{medicalHistoryController.nextPage}" rendered="#{!medicalHistoryController.nextButtonDisabled}">
                <h:outputText value="Next" />
            </h:commandLink>
        </div>
    </h:form>
</f:view>
</body>
</html>
