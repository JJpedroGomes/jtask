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
        <title>My Board</title>
    </head>
    <body>
        <%@ include file="commonSideBarMenu.jsp" %>

        <!--Start: Board -->
        <section>
            <div class="board_container">
                <!--Start: New task modal form section -->              
                <a id="modal_button_lane">
                    <i class="fas fa-plus-circle"></i>Add lane
                </a>                
                <div class="modal_background">
                    <form id="modal_form">
                        <div class="modal_wrapper">
                            <div class="modal_container">
                                <div class="modal_header">
                                    <div class="modal_header_content">
                                        <input type="text" id="task_title" name="title" placeholder="Task name" required>
                                    </div>
                                    <div class="dropdown">
                                        <i onclick="openDropDown()" class="fas fa-ellipsis-v" id="openDropDownBtn"></i>
                                        <div id="myDropdown" class="dropdown_options">
                                            <a id="myDropdownDelete" href="#">
                                                <i class="fas fa-trash"></i>
                                                <span>Delete</span>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal_form_element" id="description_details">
                                    <textarea id="task_description" name="description" placeholder="Description"></textarea>
                                </div>
                                <div class="modal_form_element">
                                    <label class="date_label" for="task_due_date">Due Date:</label>
                                    <input type="date" id="task_due_date" name="dueDate">
                                </div>
                                <div class="modal_form_element">
                                    <button type="submit" id="modal_submit_btn">Add task</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <!--End: New task modal form section -->
                <!--Start: Lanes section -->              
                <c:set var="taskList" value="${sessionScope.taskList}" scope="session"/>            
                <div class="lane_wrapper">
	                <c:forEach items="${lanes}" var="lane">
	                	<div class="lane" id="${lane.getId()}" draggable="true">
	                		<h3 class="lane_heading" contenteditable="true">
	                			${lane.getName()}
	                		</h3>
	                		<a id="new_task_for_lane_${lane.getId()}">
	                			<i class="fas fa-plus-circle modal_button"></i>
	                		</a>
	                		<c:if test="${not empty lane.getTasks()}">
							<c:forEach items="${lane.getTasksInOrder()}" var="task">
								<div class="task" draggable="true" id="task-${task.id}"
									data-task-id="${task.id}" data-task-title="${task.title}"
									data-task-description="${task.description}"
									data-task-duedate="${task.dueDate}">
									<p>${task.title}</p>
									<c:choose>
									    <c:when test="${task.completed}">
									        <input type="checkbox" id="conclude_button"
											class="conclude_button hidden" checked />
										<div class="button_checkmark completed">
											<img
												src="${pageContext.request.contextPath}/assets/img/checkmark.png"
												alt="" class="check_img completed" />
										</div>
										<svg class="circle completed">
											<circle cx="12" cy="12" r="10" />
										</svg>
									    </c:when>
										<c:otherwise>
											<input type="checkbox" id="conclude_button"
												class="conclude_button hidden" />
											<div class="button_checkmark">
												<img
													src="${pageContext.request.contextPath}/assets/img/checkmark.png"
													alt="" class="check_img" />
											</div>
											<svg class="circle">
												<circle cx="12" cy="12" r="10" />
											</svg>
										</c:otherwise>
									</c:choose>
								</div>
							</c:forEach>
						</c:if>
	                	</div>
	                </c:forEach>
	                </div>
                <!--End: Lanes section -->
                <!--Start: Drag modal section --> 
	                <div class="drag_modal_container" id="droptarget">
	                	<div class="trash_icon">
	                		<i class="fas fa-trash"></i>
	                	</div>
	                </div>
	            <!--End: Drag modal section --> 
            </div>
        </section>
        <!--End: Board -->
        <footer></footer>
        <script src="${pageContext.request.contextPath}/js/drag.js" defer></script>
        <script src="${pageContext.request.contextPath}/js/todo.js" defer></script>
        <script src="${pageContext.request.contextPath}/js/dragLane.js" defer></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    </body>
</html>