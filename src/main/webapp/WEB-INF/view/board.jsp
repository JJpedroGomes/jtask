<%@ page import="com.jjpedrogomes.model.task.Task" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Jtask</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <!-- Start Header -->
        <header id="main-header">
            <div class="account">
                <figure>
                    <div class="account-avatar">
                        <img src="" alt="">
                    </div>
                    <figcaption>
                        <h2 class="account-name">Logged user name</h2>
                    </figcaption>
                </figure>
            </div>
        </header>
        <!-- End Header -->

        <!-- Start Main Content -->
        <section>
            <div class="main-content">
                <header class="main-content-header">
                    <ul class="main-content-list-inline">
                        <li>
                            <a href="" title="">
                                <span class="state-yellow"></span>
                                <span>Task</span>
                            </a>
                        </li>
                        <li>
                            <a href="" title="">
                                <span class="state-blue"></span>
                                <span>Feature</span>
                            </a>
                        </li>
                        <li>
                            <a href="" title="">
                                <span class="state-red"></span>
                                <span>Bug</span>
                            </a>
                        </li>
                    </ul>
                    <div class="main-content-header-action">
                        <a href="" title="">New Task</a>
                    </div>
                </header>
                <div class="dashboard">
                    <div class="dashboard-row">
                        <header class="dashboard-row-header">
                            <h4>BackLog<span>(7)</span></h4>
                        </header>
                        <c:set var="taskList" value="${requestScope.taskList}" scope="session"/>
                        <div class="dashboard-row-content">
                            <ul class="list">
                                <c:if test="${not empty taskList}">
                                    <c:forEach items="${taskList}" var="task">
                                        <li class="el">
                                            <div class="task">
                                                <header>
                                                    <h3>${task.getTitle()}</h3>
                                                </header>
                                            </div>
                                            <div class="task-content">
                                                ${task.getDescription()}
                                            </div>
                                        </li>
                                    </c:forEach>
                                </c:if>
                            </ul>
                        </div>
                        <form action="main" method="post">
                            <input type="hidden" name="action" value="CreateTask">
                            <label for="title">Title:</label><br>
                            <input type="text" id="title" name="title" required><br>
                            <label for="description">Description:</label><br>
                            <textarea id="description" name="description" rows="4" cols="50" required></textarea><br>
                            <label for="dueDate">Due Date:</label><br>
                            <input type="date" id="dueDate" name="dueDate"><br><br>
                            <input type="submit" value="Create Task">
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
