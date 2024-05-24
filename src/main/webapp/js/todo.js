let form = document.getElementById("modal_form");
const taskName = document.getElementById("task_title");
const taskDescription = document.getElementById("task_description");
const taskDueDate = document.getElementById("task_due_date");
const lane = document.getElementById("todo_lane");
const btnSubmit = document.getElementById("modal_submit_btn");
const modalBackground = document.querySelector(".modal_background");
const modalContainer = document.querySelector(".modal_container");
const myDropdown = document.getElementById("myDropdown");
const openDropDownBtn = document.getElementById("openDropDownBtn");
const myDropdownDelete = document.getElementById("myDropdownDelete");

// Event Listeners
document.getElementById("modal_button").addEventListener("click", openModalForCreate);
// document.querySelector(".close-btn").addEventListener("click", closeModal);

// Function to open modal for creating a task
function openModalForCreate() {
    modalBackground.style.display = "flex";
    openDropDownBtn.style.display = "none";
    form.removeEventListener("submit", handleUpdateTask);
    form.addEventListener("submit", handleCreateTask);
    resetFormInputs();
}

// Function to close the modal and reset inputs
function closeModal() {
    resetFormInputs();
    form.removeEventListener("submit", handleCreateTask);
    form.removeEventListener("submit", handleUpdateTask);
    modalBackground.style.display = "none";
}

// Function to reset form inputs
function resetFormInputs() {
    taskName.value = ""; //Resets the input attribute
    taskDescription.value = "";
    taskDueDate.value = "";
    btnSubmit.innerText = "Add Task";
}

// Close details modal when clicked outside 
modalBackground.addEventListener("click", function(event) {
    const currentDropDownDisplay = window.getComputedStyle(myDropdown).display;
    if (currentDropDownDisplay === "flex" && !openDropDownBtn.classList.contains("firstOpen")) {
            myDropdown.style.display = "none";
            return;
    } else {
        openDropDownBtn.classList.remove("firstOpen");
    }

    const currentModalDisplay = window.getComputedStyle(modalBackground).display;
    if (currentModalDisplay === "flex" && !modalContainer.contains(event.target)) {
        closeModal();
    }
});

// Function to create a task and add it to the lane
function handleCreateTask(event) {
    event.preventDefault();
    const value = taskName.value;

    if (!value) return;

    const formData = {
        action: "CreateTask",
        title: value,
        description: taskDescription.value,
        dueDate: taskDueDate.value
    };

    $.ajax({
        type: "POST",
        url: "main",
        data: formData,
        success: function (response) {
            addTaskToLane(response);
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Error occurred trying to add task");
        }
    });
}

// Function to add a task DOM element to the lane
function addTaskToLane(task) {
    const newTask = document.createElement("p");
    newTask.id = `task-${task.id}`;
    newTask.classList.add("task");
    newTask.setAttribute("draggable", "true");
    newTask.innerText = taskName.value;

    lane.appendChild(newTask);

    newTask.dataset.taskId = task.id;
    addAndUpdateDatasets(newTask, task);
    addTaskClickListener(newTask);
    newTask.addEventListener("dragstart", () => {
        newTask.classList.add("is_dragging");
    });
    newTask.addEventListener("dragend", () => {
        newTask.classList.remove("is_dragging");
    });
    resetFormInputs();
    document.querySelector(".modal_background").style.display = "none";
}

window.addEventListener('load', function() {
    document.querySelectorAll('.task').forEach(function(taskElement) {
        addTaskClickListener(taskElement);
    });
});

function addTaskClickListener(taskElement) {
    taskElement.addEventListener('click', function() {
        const taskId = taskElement.dataset.taskId;
        const taskTitle = taskElement.dataset.taskTitle;
        const taskDescription = taskElement.dataset.taskDescription;
        const taskDueDate = taskElement.dataset.taskDuedate;
        const taskConclusionDate = taskElement.dataset.taskConclusiondate;

        showDetails(taskId, taskTitle, taskDescription, taskDueDate, taskConclusionDate);
    });
}

// Function to show task details in the modal for updating
function showDetails(id, title, description, dueDate, conclusionDate) {
    if (dueDate instanceof Object) {
        dueDate = formatDate(dueDate);
    }

    openModalDetails(title, description, dueDate);
    form.removeEventListener("submit", handleCreateTask);
    form.removeEventListener("submit", handleUpdateTask);
    
    form.addEventListener("submit", handleUpdateTask);
    
    //Todo: isso aqui ta acumulando eventos que quando clica manda um monte de id 
    myDropdownDelete.addEventListener("click", function(event){
		handleDeleteTask(event, id);
	});
	
    form.id = id;
}

// Function to update a task
function handleUpdateTask(event) {
    const taskId = event.currentTarget.id;

    event.preventDefault();
    const taskElement = document.getElementById(`task-${taskId}`);

    const value = taskName.value;
    if (!value) return;

    const formData = {
        action: "UpdateTask",
        id: taskId,
        title: value,
        description: taskDescription.value,
        dueDate: taskDueDate.value
    };

    $.ajax({
        type: "POST",
        url: "main",
        data: formData,
        success: function (response) {
            updateTaskElement(response, taskElement);
            closeModal();
        },
        error: function (xhr, status, error) {
            alert("Error occurred trying to update task");
        }
    });
}

// Function to open modal with task details
function openModalDetails(title, description, dueDate) {
    modalBackground.style.display = "flex";
    openDropDownBtn.style.display = "flex";
    taskName.value = title;
    taskDescription.value = description;
    taskDueDate.value = dueDate;
    btnSubmit.innerText = "Save";
}

// Function to update a taskElement
function updateTaskElement(response, taskElement) {
    if (!taskElement) {
        return;
    }
    taskElement.textContent = response.title;
    taskElement.removeEventListener('click', showDetails);

    addAndUpdateDatasets(taskElement, response);
}

function addAndUpdateDatasets(taskElement, task) {
    taskElement.dataset.taskTitle = task.title;
    taskElement.dataset.taskDescription = task.description;
    taskElement.dataset.taskDuedate = formatDate(task.dueDate);
    if (task.conclusionDate != null) {
        taskElement.dataset.taskDuedate = formatDate(task.conclusionDate);
    }
}

function formatDate(dateObj) {
    const { year, month, day } = dateObj;
    const formattedMonth = month.toString().padStart(2, '0');
    const formattedDay = day.toString().padStart(2, '0');
    return `${year}-${formattedMonth}-${formattedDay}`;
}

function openDropDown() {
    myDropdown.style.display = "flex";
    openDropDownBtn.classList.toggle("firstOpen");
}

function handleDeleteTask(event, id) {
	const formData = {
		action: "DeleteTask",
		id: id,
	};
	
	$.ajax({
		type: "POST",
		url: "main",
		data: formData,
		success: function(response) {
			removeTaskElement(response);
			closeModal();
		},
		error: function() {
			alert("Error occurred trying to delete task");
		}
	});
}


function removeTaskElement(response) {
	const elementToRemove = document.getElementById("task-" + response.id);
	if (!elementToRemove) {
		console.error("Task element not found in DOM");
    	return;
    }
    elementToRemove.remove();
}

