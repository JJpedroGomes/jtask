const draggables = document.querySelectorAll(".task");
const droppables = document.querySelectorAll(".lane");

draggables.forEach((task) => {
    task.addEventListener("dragstart", () => {
        task.classList.add("is_dragging");
    });
    task.addEventListener("dragend", () => {
        task.classList.remove("is_dragging");
    });
});

droppables.forEach((lane) => {
    lane.addEventListener("dragover", (event) => {
        event.preventDefault();
        const bottomTask = insertAboveTask(lane, event.clientY);
        const curtTask = document.querySelector(".is_dragging");
        if (!bottomTask) {
            lane.appendChild(curtTask);
        } else {
            lane.insertBefore(curtTask, bottomTask);
        }
    });
});

const insertAboveTask = (lane, mouseY) => {
    const tasksInLane = lane.querySelectorAll(".task:not(.is_dragging)");
    let closestTask = null;
    let closestOffset = Number.NEGATIVE_INFINITY;

    tasksInLane.forEach((task) => {
        const { top } = task.getBoundingClientRect();
        const offset = mouseY - top;
        if (offset < 0 && offset > closestOffset) {
            closestOffset = offset;
            closestTask = task;
        }
    });
    return closestTask;
};