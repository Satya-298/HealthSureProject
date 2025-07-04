<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Search Medical Procedures</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #333;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<f:view>
<h:form>

    <h2>Search Medical Procedures</h2>

    <!-- Search input -->
    <h:outputLabel for="searchKey" value="Search by HID / Name / Phone:" />
    <h:inputText id="searchKey" value="#{medicalProcedureController.searchKey}" />
    &nbsp;&nbsp;&nbsp;&nbsp;
    <h:commandButton value="Search" action="#{medicalProcedureController.searchProcedures}" />
    
    <br/><br/>

    <!-- Data Table -->
    <h:dataTable value="#{medicalProcedureController.searchResults}" var="proc"
                 border="1" rendered="#{not empty medicalProcedureController.searchResults}">

        <h:column>
            <f:facet name="header"><h:outputText value="Procedure ID" /></f:facet>
            <h:outputText value="#{proc.procedureId}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Recipient" /></f:facet>
            <h:outputText value="#{proc.recipient.firstName} #{proc.recipient.lastName}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Doctor" /></f:facet>
            <h:outputText value="#{proc.doctor.doctorName}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Provider" /></f:facet>
            <h:outputText value="#{proc.provider.hospitalName}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Procedure Date" /></f:facet>
            <h:outputText value="#{proc.procedureDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:outputText>
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Diagnosis" /></f:facet>
            <h:outputText value="#{proc.diagnosis}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="Recommendations" /></f:facet>
            <h:outputText value="#{proc.recommendations}" />
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="From Date" /></f:facet>
            <h:outputText value="#{proc.fromDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:outputText>
        </h:column>

        <h:column>
            <f:facet name="header"><h:outputText value="To Date" /></f:facet>
            <h:outputText value="#{proc.toDate}">
                <f:convertDateTime pattern="yyyy-MM-dd" />
            </h:outputText>
        </h:column>

    </h:dataTable>

</h:form>
</f:view>
</body>
</html>
