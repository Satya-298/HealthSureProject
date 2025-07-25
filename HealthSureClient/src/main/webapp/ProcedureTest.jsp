<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Prescribed Tests</title>
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
        <h2>Prescribed Tests</h2>
        
       	<h:outputText value="Total Records : #{medicalHistoryController.totalTestRecords}"></h:outputText>

        <h:dataTable value="#{medicalHistoryController.paginatedTests}" var="test" border="1">

            <!-- Test ID -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortTestsBy('testId')}">
                        <span>Test ID
                            <h:outputText styleClass="arrow"
                                value="#{medicalHistoryController.testsSortColumn eq 'testId' ? (medicalHistoryController.testsSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{test.testId}" />
            </h:column>

            <!-- Test Name -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortTestsBy('testName')}">
                        <span>Test Name
                            <h:outputText styleClass="arrow"
                                value="#{medicalHistoryController.testsSortColumn eq 'testName' ? (medicalHistoryController.testsSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{test.testName}" />
            </h:column>

            <!-- Test Date -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortTestsBy('testDate')}">
                        <span>Test Date
                            <h:outputText styleClass="arrow"
                                value="#{medicalHistoryController.testsSortColumn eq 'testDate' ? (medicalHistoryController.testsSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{test.testDate}">
                    <f:convertDateTime pattern="dd-MM-yyyy" />
                </h:outputText>
            </h:column>

            <!-- Result Summary -->
            <h:column>
                <f:facet name="header">
                    <h:commandLink action="#{medicalHistoryController.sortTestsBy('resultSummary')}">
                        <span>Result Summary
                            <h:outputText styleClass="arrow"
                                value="#{medicalHistoryController.testsSortColumn eq 'resultSummary' ? (medicalHistoryController.testsSortAscending ? '▲' : '▼') : ''}" />
                        </span>
                    </h:commandLink>
                </f:facet>
                <h:outputText value="#{test.resultSummary}" />
            </h:column>


        </h:dataTable>

        <!-- Pagination Buttons -->
        <h:panelGroup layout="block" styleClass="pagination-panel">
            <h:commandButton value="First" action="#{medicalHistoryController.goToFirstTestPage}"
                             disabled="#{medicalHistoryController.testCurrentPage == 1}" />
            <h:commandButton value="Previous" action="#{medicalHistoryController.previousTestPage}"
                             disabled="#{medicalHistoryController.testCurrentPage == 1}" />
            <h:outputText value="Page #{medicalHistoryController.testCurrentPage} of #{medicalHistoryController.totalTestPages}" />
            <h:commandButton value="Next" action="#{medicalHistoryController.nextTestPage}"
                             disabled="#{medicalHistoryController.testCurrentPage == medicalHistoryController.totalTestPages}" />
            <h:commandButton value="Last" action="#{medicalHistoryController.goToLastTestPage}"
                             disabled="#{medicalHistoryController.testCurrentPage == medicalHistoryController.totalTestPages}" />
        </h:panelGroup>

    </h:form>
</f:view>
</body>
</html>
