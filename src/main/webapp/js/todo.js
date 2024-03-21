const form = document.getElementById("todo_form");
const input = document.getElementById("todo_input");
const lane = document.getElementById("todo_lane");

form.addEventListener("submit", (event) => {
    event.preventDefault(); //prevent screen reload after submit
    const value = input.value;

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
    input.value = ""; //Resets the input attribute
});