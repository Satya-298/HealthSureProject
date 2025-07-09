<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Search Medical Procedures</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            min-height: 100vh;
            padding: 24px;
        }

        .form-container {
            background-color: white;
            padding: 24px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            max-width: 1200px;
            margin: auto;
        }

        h2 {
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            color: #1d4ed8;
            margin-bottom: 24px;
        }

        label, .label {
            font-weight: 600;
            color: #374151;
            margin-bottom: 6px;
            display: block;
        }

        .flex-row {
            display: flex;
            gap: 16px;
            align-items: flex-end;
            margin-bottom: 16px;
        }

        .radio-row {
            display: flex;
            gap: 16px;
            margin-bottom: 16px;
        }

        input[type="text"], textarea {
            padding: 8px 12px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            width: 260px;
        }

        .btn {
            padding: 10px 16px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.2s;
        }

        .btn-blue {
            background-color: #2563eb;
            color: white;
        }

        .btn-blue:hover {
            background-color: #1e40af;
        }

        .btn-gray {
            background-color: #6b7280;
            color: white;
        }

        .btn-gray:hover {
            background-color: #4b5563;
        }

        .error {
            color: red;
            font-size: 13px;
            margin-top: 4px;
        }

        .table-wrapper {
            border: 1px solid #ccc;
            border-radius: 10px;
            overflow-x: auto;
            margin-top: 20px;
            min-height: 400px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
            word-wrap: break-word;
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
            background-color: white;
            padding: 12px;
            position: fixed;
            bottom: 12px;
            left: 0;
            right: 0;
            box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.08);
        }

        .link {
            color: #2563eb;
            text-decoration: none;
        }

        .link:hover {
            text-decoration: underline;
        }

        .message {
            color: red;
            margin-bottom: 20px;
            font-size: 14px;
            display: block;
        }
    </style>
</head>
<body>
<f:view>
<h:form prependId="false" styleClass="form-container">

    <a name="top"></a>

    <h2>Search Medical Procedures</h2>

    <div class="label">Select Search Type:</div>
    <div class="radio-row">
        <h:selectOneRadio value="#{medicalProcedureController.searchType}" onclick="this.form.submit();">
            <f:selectItem itemValue="hid" itemLabel="Search by HID" />
            <f:selectItem itemValue="name" itemLabel="Search by Name" />
            <f:selectItem itemValue="mobile" itemLabel="Search by Phone" />
        </h:selectOneRadio>
    </div>

    <h:panelGroup rendered="#{medicalProcedureController.searchType eq 'name'}">
        <div class="label">Name Search Mode:</div>
        <div class="radio-row">
            <h:selectOneRadio value="#{medicalProcedureController.nameSearchMode}">
                <f:selectItem itemValue="startsWith" itemLabel="Starts With" />
                <f:selectItem itemValue="contains" itemLabel="Contains" />
            </h:selectOneRadio>
        </div>
    </h:panelGroup>

    <div class="flex-row">
        <div>
            <h:outputLabel for="searchKey" value="Enter Search Value:" styleClass="label" />
            <h:inputText id="searchKey" value="#{medicalProcedureController.searchKey}" />
        </div>
        <h:commandButton value="Search" action="#{medicalProcedureController.searchProcedures}" styleClass="btn btn-blue" />
        <h:commandButton value="Reset" action="#{medicalProcedureController.resetForm}" styleClass="btn btn-gray" />
    </div>

    <h:messages globalOnly="true" styleClass="message" layout="table" />

    <div class="table-wrapper">
        <h:dataTable value="#{medicalProcedureController.paginatedList}" var="proc"
                     border="1" rendered="#{not empty medicalProcedureController.searchResults}">

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Procedure ID" action="#{medicalProcedureController.sortBy('procedureId')}" />
                </f:facet>
                <h:outputText value="#{proc.procedureId}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Recipient" action="#{medicalProcedureController.sortBy('recipientName')}" />
                </f:facet>
                <h:outputText value="#{proc.recipient.firstName} #{proc.recipient.lastName}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Doctor" action="#{medicalProcedureController.sortBy('doctorName')}" />
                </f:facet>
                <h:outputText value="#{proc.doctor.doctorName}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Provider" action="#{medicalProcedureController.sortBy('providerName')}" />
                </f:facet>
                <h:outputText value="#{proc.provider.hospitalName}" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Procedure Date" action="#{medicalProcedureController.sortBy('procedureDate')}" />
                </f:facet>
                <h:outputText value="#{proc.procedureDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Diagnosis" action="#{medicalProcedureController.sortBy('diagnosis')}" />
                </f:facet>
                <h:outputText value="#{proc.diagnosis}" style="white-space: normal;" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="Recommendation" action="#{medicalProcedureController.sortBy('recommendations')}" />
                </f:facet>
                <h:outputText value="#{proc.recommendations}" style="white-space: normal;" />
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="From Date" action="#{medicalProcedureController.sortBy('fromDate')}" />
                </f:facet>
                <h:outputText value="#{proc.fromDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

            <h:column>
                <f:facet name="header">
                    <h:commandLink value="To Date" action="#{medicalProcedureController.sortBy('toDate')}" />
                </f:facet>
                <h:outputText value="#{proc.toDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:outputText>
            </h:column>

        </h:dataTable>
    </div>

    <div class="pagination">
        <h:commandLink action="#{medicalProcedureController.previousPage}" styleClass="link">
            <h:outputText value="Previous" />
            <f:param name="scroll" value="top" />
        </h:commandLink>

        <h:outputText value="Page #{medicalProcedureController.currentPage + 1} of #{medicalProcedureController.totalPages}" />

        <h:commandLink action="#{medicalProcedureController.nextPage}" styleClass="link">
            <h:outputText value="Next" />
            <f:param name="scroll" value="top" />
        </h:commandLink>
    </div>

</h:form>
</f:view>
</body>
</html>
