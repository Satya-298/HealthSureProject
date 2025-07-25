<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Doctor Availability - Menu</title>
    <style>
        body {
            background-color: #f3f4f6;
            color: #1f2937;
            font-family: Arial, sans-serif;
            min-height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .menu-container {
            background-color: #ffffff;
            padding: 40px 30px;
            border-radius: 16px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        .menu-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #2563eb;
        }

        .menu-link {
            display: block;
            margin: 10px 0;
            padding: 12px 20px;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 500;
            text-decoration: none;
            color: white;
            transition: background-color 0.2s ease-in-out;
        }

        .menu-link.blue {
            background-color: #3b82f6;
        }

        .menu-link.blue:hover {
            background-color: #2563eb;
        }

        .menu-link.green {
            background-color: #22c55e;
        }

        .menu-link.green:hover {
            background-color: #16a34a;
        }

        .menu-link.purple {
            background-color: #8b5cf6;
        }

        .menu-link.purple:hover {
            background-color: #7c3aed;
        }

        .menu-link.cyan {
            background-color: #06b6d4;
        }

        .menu-link.cyan:hover {
            background-color: #0891b2;
        }
    </style>
</head>
<body>
<f:view>
    <h:form>
        <div class="menu-container">
            <div class="menu-title">Doctor Availability - Menu</div>

            <h:commandLink value="Add Doctor Availability" action="addAvailability"
                           styleClass="menu-link blue" />

            <h:commandLink value="List Availability by Date" action="listAvailabilityByDate"
                           styleClass="menu-link green" />

            <h:commandLink value="Search Patient Medical History" action="MedicalProcedureSearch"
                           styleClass="menu-link purple" />

            <h:commandLink value="View Appointments" action="Appointments"
                           styleClass="menu-link cyan" />
        </div>
    </h:form>
</f:view>
</body>
</html>
