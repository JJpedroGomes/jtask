# jTask

## Table of Contents
- [About](#about)
- [Technologies](#technologies)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [How to Run It](#how-to-run-it)

---

## About
This project is a **Task Management System** written in **Java 8**, developed primarily for **study purposes**. It aims to help users manage and organize their tasks efficiently, offering features such as task creation, updates, and tracking. The project was built as a way to deepen my understanding of Java programming, object-oriented principles, and software development best practices.

During development, I experimented with various approaches, some of which may not adhere to best practices. As such, certain parts of the code could benefit from improvements and refactoring. This project serves as a learning experience and a starting point for exploring more robust solutions and design patterns in Java development.

## Application Overview

![Dashboard](./src/main/webapp/assets/img/board.png)

## Technologies
This project utilizes a range of technologies to provide a robust and scalable solution:

- **Java 8**
- **Servlet**
- **JSP (JavaServer Pages)**
- **JavaScript**
- **Docker**
- **Postgres**
- **Shell**
- **Maven**
- **JUnit**
- **Mockito**
- **JPA (Java Persistence API)**
- **H2 Database**
- **Wildfly**
- **Log4J**

## Features
- **Create Lanes**: Organize your tasks by creating custom lanes to represent different stages or categories in your workflow.
- **Edit Lanes**: Update lane names to better reflect changes in your task management structure.
- **Reorganize Lanes**: Move lanes around the board to customize your workspace layout and optimize your task view.
- **Add Tasks**: Create new tasks and assign them to specific lanes to keep track of your activities.
- **Edit Tasks**: Modify task details, such as titles, descriptions, and due dates, to keep your task list up to date.
- **Move Tasks**: Drag and drop tasks between lanes, allowing for easy status updates or reassignments.
- **Delete Tasks**: Remove tasks from your board when they are no longer needed.
- **Delete Lanes**: Clean up your board by removing lanes that are no longer relevant.
- **Mark Tasks as Completed**: Mark tasks as done to keep track of progress and focus on what’s left.
- **Account Management**: Manage your user account, including creating, updating, and personalizing your experience.

## Prerequisites
Make sure the following software and tools are installed before running the project:
- Java 8
- Maven
- Docker

## How to Run It
Follow these steps to set up and run the project:

```bash
# Clone the repository
git clone https://github.com/JJpedroGomes/jtask.git

# Navigate into the project directory
cd Jtask

# Build the project using Maven
mvn clean install -DskipTests

# Run the application using Docker
docker compose up --build
```
**url**: localhost:8080/jtask

This application comes with a default user for testing purposes, or you can create your own user if preferred.
**You can use the following default user credentials to quickly test the application:**

- **Email:** `email@email.com`
- **Password:** `123456`
