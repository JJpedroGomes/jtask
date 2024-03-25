<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.jjpedrogomes.model.task.Task" %>
<%@ page import="java.util.List" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/all.css">
        <script src="${pageContext.request.contextPath}/js/drag.js" defer></script>
        <script src="${pageContext.request.contextPath}/js/todo.js" defer></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <title>My Board</title>
    </head>
    <body>
        <!--Start: side navigation bar -->
        <section class="sidebar_container">
            <div class="column_wrapper">
                <div class="sidebar">
                    <ul>
                        <!--Start: navigation bar logo -->
                        <li class="logo_container">
                            <!--Todo Change logo-->
                            <div class="logo">
                                <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="">
                                <span>Jtask</span>
                            </div>
                        </li>
                        <!--End: navigation bar logo -->
                        <!--Start:  navigation bar main links-->
                        <li>
                            <a href="#">
                                <i class="fas fa-home"></i>
                                <span>Home</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-calendar-day"></i>
                                <span>Today</span>
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <i class="fas fa-calendar"></i>
                                <span>Upcoming</span>
                            </a>
                        </li>
                        <!--End:  navigation bar main links-->
                        <!--Start:  navigation bar bottom links-->
                        <div class="bottom_links_container">
                            <li>
                                <a href="#">
                                    <i class="fas fa-user-circle"></i>
                                    <span>User name</span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fas fa-sign-out-alt"></i>
                                    <span>Log Out</span>
                                </a>
                            </li>
                        </div>
                        <!--End:  navigation bar bottom links-->
                    </ul>
                </div>
            </div>
        </section>
        <!--End: side navigation bar -->

        <!--Start: Board -->
        <section>
            <div class="board_container">
                <!--Start: New task modal form section -->
                <a href="#" id="modal_button">
                    <i class="fas fa-plus-circle"></i>Add task
                </a>
                <div class="modal_background">
                    <form id="modal_form">
                        <div class="modal_container">
                            <div class="close-btn">&times;</div>
                            <div class="modal_form_element">
                                <input type="text" id="task_title" name="title" placeholder="Task name" required>
                            </div>
                            <div class="modal_form_element">
                                <input type="text" id="task_description" name="description"
                                       placeholder="Description">
                            </div>
                            <div class="modal_form_element">
                                <label for="task_due_date">Due Date:</label>
                                <input type="date" id="task_due_date" name="dueDate">
                            </div>
                            <div class="modal_form_element">
                                <button type="submit">Add task</button>
                            </div>
                        </div>
                    </form>
                </div>
                <!--End: New task modal form section -->

                <!--Start: Lanes section -->
                <c:set var="taskList" value="${sessionScope.taskList}" scope="session"/>
                <div class="lane_wrapper">
                    <div class="lane" id="todo_lane">
                        <h3 class="lane_heading">Todo</h3>
                        <c:if test="${not empty taskList}">
                            <c:forEach items="${taskList}" var="task">
                                <p class="task" draggable="true">${task.title}</p>
                            </c:forEach>
                        </c:if>
                    </div>
                    <div class="lane">
                        <h3 class="lane_heading">Doing</h3>
                        <!--TODO: Load Tasks from Database-->
                    </div>
                    <div class="lane">
                        <h3 class="lane_heading">Done</h3>
                        <!--TODO: Load Tasks from Database-->
                    </div>
                </div>
                <!--End: Lanes section -->
            </div>
        </section>
        <!--End: Board -->
        <footer></footer>
    </body>
</html>