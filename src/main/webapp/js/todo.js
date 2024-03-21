const form = document.getElementById("modal_form");
const taskName = document.getElementById("task_title");
const taskDescription = document.getElementById("task_description");
const taskDueDate = document.getElementById("task_due_date");
const lane = document.getElementById("todo_lane");

// Open modal
document.getElementById("modal_button").addEventListener("click", () => {
    document.querySelector(".modal_background").style.display = "flex";
});

//Close modal
document.querySelector(".close-btn").addEventListener("click", () => {
    document.querySelector(".modal_background").style.display = "none";
});

form.addEventListener("submit", (event) => {
    event.preventDefault(); //prevent screen reload after submit
    const value = taskName.value;

    if (!value) return;

    const newTask = document.createElement("p");
    newTask.classList.add("task");
    newTask.setAttribute("draggable", "true");
    newTask.innerText = value;

    newTask.addEventListener("dragstart", () => {
        newTask.classList.add("is_dragging");
    });
    newTask.addEventListener("dragend", () => {
       newTask.classList.remove("is_dragging");
    });

    lane.appendChild(newTask);
    taskName.value = ""; //Resets the input attribute
    taskDescription.value = "";
    taskDueDate.value = "";
    document.querySelector(".modal_background").style.display = "none";
});