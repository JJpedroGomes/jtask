<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/styles.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/all.css">
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
                                <span>New Task</span>
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
                                <span>Coming Soon</span>
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
        <footer></footer>
    </body>
</html>