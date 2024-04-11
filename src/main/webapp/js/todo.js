const form = document.getElementById("modal_form");
const taskName = document.getElementById("task_title");
const taskDescription = document.getElementById("task_description");
const taskDueDate = document.getElementById("task_due_date");
const lane = document.getElementById("todo_lane");
const btnSubmit = document.getElementById("modal_submit_btn");

// Open modal
document.getElementById("modal_button").addEventListener("click", () => {
    document.querySelector(".modal_background").style.display = "flex";
    form.removeEventListener("submit", addUpdateTaskEventListener);
    form.addEventListener("submit", addCreateTaskEventListener);
});

//Close modal
document.querySelector(".close-btn").addEventListener("click", () => {
    resetFormInputs();
    document.querySelector(".modal_background").style.display = "none";
});

function addTaskToLane(task) {
    const newTask = document.createElement("p");
    newTask.classList.add("task");
    newTask.setAttribute("draggable", "true");
    newTask.innerText = taskName.value;

    lane.appendChild(newTask);

    newTask.addEventListener("click", () => {
        showDetails(task.title, task.description, task.dueDate, task.conclusionDate || null);
    });
    console.log("Calling...");
    newTask.addEventListener("dragstart", () => {
        newTask.classList.add("is_dragging");
    });
    newTask.addEventListener("dragend", () => {
        newTask.classList.remove("is_dragging");
    });
    resetFormInputs();
    document.querySelector(".modal_background").style.display = "none";
};

function resetFormInputs() {
    taskName.value = ""; //Resets the input attribute
    taskDescription.value = "";
    taskDueDate.value = "";
    btnSubmit.innerText = "Add Task";
}

function addCreateTaskEventListener(event) {
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

function showDetails(title, description, dueDate, conclusionDate) {
    document.querySelector(".modal_background").style.display = "flex";
    taskName.value = title;
    taskDescription.value = description;
    taskDueDate.value = dueDate;
    btnSubmit.innerText = "Save";

    form.removeEventListener("submit", addCreateTaskEventListener);
    form.addEventListener("submit", addUpdateTaskEventListener);
}

//Todo: Chamar controller para update da task
function addUpdateTaskEventListener(event) {
    event.preventDefault();
    console.log("teste");
}

//Todo: Estilizar o novo modal para detalhes
