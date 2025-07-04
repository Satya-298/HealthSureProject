<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Availability Added</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 40px;
            text-align: center;
        }
        .message {
            font-size: 20px;
            color: green;
            margin-bottom: 20px;
        }
        .button {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
        }
        .button:hover {
            background-color: #45a049;
            cursor: pointer;
        }
    </style>
</head>
<body>
<f:view>
    <h:form>
        <div class="message">
            ✅ Doctor Availability has been successfully added!
        </div>

        <h:commandButton value="Back to Menu" action="menu" styleClass="button" />
    </h:form>
</f:view>
</body>
</html>
