<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
<head>
    <title>Doctor Availability Added</title>
    <style>
        body {
            background-color: #f3f4f6;
            font-family: Arial, sans-serif;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
        }

        .container {
            background-color: #ffffff;
            padding: 40px 30px;
            border-radius: 16px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
            text-align: center;
            max-width: 500px;
            width: 100%;
        }

        .message {
            color: #15803d; /* green-700 */
            font-size: 20px;
            font-weight: 600;
            margin-bottom: 30px;
        }

        .button {
            padding: 12px 24px;
            background-color: #22c55e; /* green-500 */
            color: white;
            font-size: 16px;
            font-weight: 500;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.2s ease-in-out;
        }

        .button:hover {
            background-color: #16a34a; /* green-600 */
        }
    </style>
</head>
<body>

<f:view>
    <h:form>
        <div class="container">

            <div class="message">
                ✅ Doctor Availability has been successfully added!
            </div>

            <h:commandButton value="Back to Menu" action="menu" styleClass="button" />

        </div>
    </h:form>
</f:view>

</body>
</html>
