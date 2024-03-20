<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/all.css">
        <script src="${pageContext.request.contextPath}/js/drag.js" defer></script>
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
                                <img src="assets/img/logo.png" alt="">
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
                                <i class="fas fa-plus-circle"></i>
                                <span>Add Task</span>
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

        <section>
            <div class="board_container">
                <form id="todo_form">
                    <input type="text" placeholder="Add task..." id="todo_input" required>
                    <button type="submit"><i class="fas fa-plus"></i></button>
                </form>

                <div class="lane_wrapper">
                    <div class="lane">
                        <h3 class="lane_heading">Todo</h3>
                        <p class="task" draggable="true">Get groceries</p>
                        <p class="task" draggable="true">Feed dogs</p>
                        <p class="task" draggable="true">Go to the doctor</p>
                    </div>
                    <div class="lane">
                        <h3 class="lane_heading">Doing</h3>
                        <p class="task" draggable="true">coding jtask project</p>
                    </div>
                    <div class="lane">
                        <h3 class="lane_heading">Done</h3>
                        <p class="task" draggable="true">Math class</p>
                    </div>
                </div>
            </div>
        </section>

        <footer></footer>
    </body>
</html>