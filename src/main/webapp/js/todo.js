const form = document.getElementById("modal_form");
const taskName = document.getElementById("task_title");
const taskDescription = document.getElementById("task_description");
const taskDueDate = document.getElementById("task_due_date");
const lane = document.getElementById("todo_lane");
const modal = document.querySelector(".modal_background");

// Open modal
document.getElementById("modal_button").addEventListener("click", () => {
    modal.style.display = "flex";
});

//Close modal
document.querySelector(".close-btn").addEventListener("click", () => {
    resetFormInputs();
    modal.style.display = "none";
});

function addTaskToLane() {
    const newTask = document.createElement("p");
    newTask.classList.add("task");
    newTask.setAttribute("draggable", "true");
    newTask.innerText = taskName.value;

    lane.appendChild(newTask);

    newTask.addEventListener("dragstart", () => {
        newTask.classList.add("is_dragging");
    });
    newTask.addEventListener("dragend", () => {
        newTask.classList.remove("is_dragging");
    });
    resetFormInputs();
    modal.style.display = "none";
};

function resetFormInputs() {
    taskName.value = ""; //Resets the input attribute
    taskDescription.value = "";
    taskDueDate.value = "";
}

form.addEventListener("submit", (event) => {
    event.preventDefault(); //prevent screen reload after submit
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
            addTaskToLane();
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Error occurred trying to add task");
        }
    });
});

// Toda vez que uma tarefa é clicada apenas a primeira aparece nos detalhes  do modal
function showDetails(title, description, dueDate, conclusionDate) {
    modal.style.display = "flex";
    document.querySelector(".modal_container").className = "modal_container_details";
    document.getElementById("description_details").classList.remove("modal_form_element");
    document.getElementById("modal_submit_btn").innerHTML = "Save";

    taskName.value = title;
    taskDescription.value = description;
    taskDueDate.value = dueDate;
}