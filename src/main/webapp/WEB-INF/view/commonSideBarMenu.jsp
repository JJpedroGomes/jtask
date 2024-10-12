<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/all.css">
        <script src="${pageContext.request.contextPath}/js/sideBar.js" defer></script>
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
                                <img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="" class="logo_img">
                            </div>
                        </li>
                        <!--End: navigation bar logo -->
                        <!--Start:  navigation bar main links-->
                        <li class="menu-button" id="home">
                            <a href="${pageContext.request.contextPath}/board">
                                <i class="fas fa-home"></i>
                                <span>Home</span>
                            </a>
                        </li>
                        <li class="menu-button" id="today">
                            <a href="#">
                                <i class="fas fa-calendar-day"></i>
                                <span>Today</span>
                            </a>
                        </li>
                        <li class="menu-button" id="upcoming">
                            <a href="#">
                                <i class="fas fa-calendar"></i>
                                <span>Upcoming</span>
                            </a>
                        </li>
                        <!--End:  navigation bar main links-->
                        <!--Start:  navigation bar bottom links-->
                        <div class="bottom_links_container">
                            <li class="menu-button" id="displayName">
                                <a href="${pageContext.request.contextPath}/user">
                                    <i class="fas fa-user-circle"></i>
                                    <span><%= session.getAttribute("displayName") %></span>
                                </a>
                            </li>
                            <li class="menu-button" id="logOut">
                                <a href="#" id="logout_link">
                                    <i class="fas fa-sign-out-alt"></i>
                                    <span>Log Out</span>
                                </a>
                                <form id="logout_form" action="${pageContext.request.contextPath}/logout" method="POST" style="display: none;"></form>
                            </li>
                        </div>
                        <!--End:  navigation bar bottom links-->
                    </ul>
                </div>
            </div>
        </section>
        <!--End: side navigation bar -->
</body>
</html>