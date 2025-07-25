<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Prescriptions</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
        }

        .container {
            max-width: 900px;
            margin: 40px auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }

        .heading {
            text-align: center;
            font-size: 22px;
            font-weight: bold;
            color: #2563eb;
            margin-bottom: 25px;
        }

        .btn {
            background-color: #4b5563;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #1f2937;
        }

        .table-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ccc;
        }

        th {
            background-color: #f1f5f9;
            font-weight: bold;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
            margin-top: 25px;
        }

        .pagination a {
            color: #2563eb;
            text-decoration: none;
            font-weight: bold;
        }

        .pagination a:hover {
            text-decoration: underline;
        }

        .arrow {
            margin-left: 5px;
            font-size: 13px;
        }
    </style>
</head>

<body>
<f:view>
    <h:form id="prescriptionForm" styleClass="container">


        <!-- Title -->
        <div class="heading">
            Prescriptions for Procedure ID
            <h:outputText value="#{medicalHistoryController.medicalProcedure.procedureId}" />
        </div>

       	<h:outputText value="Total Records : #{medicalHistoryController.totalPrescriptionRecords}"/>

        <!-- Back Button -->
        <div style="margin-bottom: 20px;">
            <h:commandButton value="Back to Procedures"
                             action="MedicalProcedureSearch?faces-redirect=true"
                             styleClass="btn"
                             immediate="true" />
        </div>

        <!-- Prescription Table -->
        <div class="table-container">
            <h:dataTable value="#{medicalHistoryController.paginatedPrescriptions}" var="presc"
                         styleClass="prescription-table"
                         rendered="#{not empty medicalHistoryController.prescriptionList}">

                <!-- Prescription ID -->
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortPrescriptionsBy('prescriptionId')}">
                            <span>Prescription ID
                                <h:outputText styleClass="arrow"
                                              value="#{medicalHistoryController.prescriptionSortColumn eq 'prescriptionId' ? (medicalHistoryController.prescriptionSortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{presc.prescriptionId}" />
                </h:column>

                <!-- Written On -->
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortPrescriptionsBy('writtenOn')}">
                            <span>Written On
                                <h:outputText styleClass="arrow"
                                              value="#{medicalHistoryController.prescriptionSortColumn eq 'writtenOn' ? (medicalHistoryController.prescriptionSortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{presc.writtenOn}">
                        <f:convertDateTime pattern="dd-MM-yyyy" />
                    </h:outputText>
                </h:column>

                <!-- Start Date -->
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortPrescriptionsBy('startDate')}">
                            <span>Start Date
                                <h:outputText styleClass="arrow"
                                              value="#{medicalHistoryController.prescriptionSortColumn eq 'startDate' ? (medicalHistoryController.prescriptionSortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{presc.startDate}">
                        <f:convertDateTime pattern="dd-MM-yyyy" />
                    </h:outputText>
                </h:column>

                <!-- End Date -->
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortPrescriptionsBy('endDate')}">
                            <span>End Date
                                <h:outputText styleClass="arrow"
                                              value="#{medicalHistoryController.prescriptionSortColumn eq 'endDate' ? (medicalHistoryController.prescriptionSortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{presc.endDate}">
                        <f:convertDateTime pattern="dd-MM-yyyy" />
                    </h:outputText>
                </h:column>

                <!-- Doctor Name -->
                <h:column>
                    <f:facet name="header">
                        <h:commandLink action="#{medicalHistoryController.sortPrescriptionsBy('doctorName')}">
                            <span>Doctor
                                <h:outputText styleClass="arrow"
                                              value="#{medicalHistoryController.prescriptionSortColumn eq 'doctorName' ? (medicalHistoryController.prescriptionSortAscending ? '▲' : '▼') : ''}" />
                            </span>
                        </h:commandLink>
                    </f:facet>
                    <h:outputText value="#{presc.doctor.doctorName}" />
                </h:column>

                <!-- View Medicines -->
                <h:column>
                    <f:facet name="header"><h:outputText value="View Prescribed Medicines" /></f:facet>
                    <h:commandButton value="View Medicines"
                                     action="#{medicalHistoryController.viewMedicinesForSelectedPrescription(presc)}"
                                     styleClass="btn" />
                </h:column>

                <!-- View Tests -->
                <h:column>
                    <f:facet name="header"><h:outputText value="View Tests" /></f:facet>
                    <h:commandButton value="View Tests"
                                     action="#{medicalHistoryController.viewTestsForSelectedPrescription(presc)}"
                                     styleClass="btn" />
                </h:column>

            </h:dataTable>
        </div>

        <!-- Pagination Controls -->
        <div class="pagination">
            <h:panelGroup layout="block" styleClass="pagination-panel" style="margin-top: 20px;">
                <h:commandButton value="First" action="#{medicalHistoryController.goToFirstPrescriptionPage}" disabled="#{medicalHistoryController.prescriptionCurrentPage == 1}" />
                    <h:commandButton value="Previous" action="#{medicalHistoryController.goToPreviousPrescriptionPage}" disabled="#{medicalHistoryController.prescriptionCurrentPage == 1}" />
                    <h:outputText value="Page #{medicalHistoryController.prescriptionCurrentPage} of #{medicalHistoryController.totalPrescriptionPages}" />
                    <h:commandButton value="Next" action="#{medicalHistoryController.goToNextPrescriptionPage}" disabled="#{medicalHistoryController.prescriptionCurrentPage == medicalHistoryController.totalPrescriptionPages}" />
                    <h:commandButton value="Last" action="#{medicalHistoryController.goToLastPrescriptionPage}" disabled="#{medicalHistoryController.prescriptionCurrentPage == medicalHistoryController.totalPrescriptionPages}" />
            </h:panelGroup>
        </div>

    </h:form>
</f:view>
</body>
</html>
