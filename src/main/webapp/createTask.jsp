<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management</title>
</head>
<body>
<h1>Create Task</h1>
<form action="task" method="post">
    <input type="hidden" name="action" value="CreateTask">
    <label for="title">Title:</label><br>
    <input type="text" id="title" name="title" required><br>
    <label for="description">Description:</label><br>
    <textarea id="description" name="description" rows="4" cols="50" required></textarea><br>
    <label for="dueDate">Due Date:</label><br>
    <input type="date" id="dueDate" name="dueDate"><br><br>
    <input type="submit" value="Create Task">
</form>
</body>
</html>
