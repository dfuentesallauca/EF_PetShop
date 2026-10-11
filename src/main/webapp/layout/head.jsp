<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PetShop</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <style>
        :root { --bs-primary: #2C7A7B; --bs-primary-rgb: 44,122,123; }
        body  { font-family: 'Inter', system-ui, sans-serif; display: flex; flex-direction: column; min-height: 100vh; }
        main  { flex: 1; }
        .btn-primary        { background-color: #2C7A7B; border-color: #2C7A7B; }
        .btn-primary:hover  { background-color: #1F5F60; border-color: #1F5F60; }
        .btn-check:checked + .btn-primary,
        .btn-primary:active { background-color: #1F5F60; border-color: #1F5F60; }
    </style>
</head>
