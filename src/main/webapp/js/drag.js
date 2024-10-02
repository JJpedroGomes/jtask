const draggables = document.querySelectorAll(".task");
const droppables = document.querySelectorAll(".lane");

let taskOriginalPosition;
let taskOriginalLane;

draggables.forEach((task) => {
	addDragAndDropListenerToTask(task);
});

function addDragAndDropListenerToTask(task) {
	task.addEventListener("dragstart", (event) => {
		task.classList.add("is_dragging");
		
		taskOriginalLane = document.elementFromPoint(event.clientX, event.clientY).closest(".lane");
		taskOriginalPosition = Array.from(taskOriginalLane.querySelectorAll(".task")).indexOf(task);
	});
	task.addEventListener("dragend", (event) => {
		task.classList.remove("is_dragging");

		const targetLane = document.elementFromPoint(event.clientX, event.clientY).closest(".lane");
		const laneId = targetLane.id;
		const desiredIndex = Array.from(targetLane.querySelectorAll(".task")).indexOf(task);
		
		updateTaskPositionOnBackend(task, laneId, desiredIndex);
	});
}

async function updateTaskPositionOnBackend(task, laneId, desiredIndex) {
	const data = new URLSearchParams({action: "ChangeTaskPosition", taskId: task.dataset.taskId, laneId: laneId, newPositionIndex:desiredIndex});
	
	try {	
		const response = await fetch("/jtask/main", {
			method: "post",
			body: data
		});
		
		if(!response.ok) {
			throw new Error();
		}
	} catch(error) {
		alert("Error occurred saving task position");
		revertTaskPosition(task, taskOriginalPosition, taskOriginalLane);
	}
}

function revertTaskPosition(task, taskOriginalPosition, taskOriginalLane) {
	const tasks = [...taskOriginalLane.children];
	
	if (tasks[taskOriginalPosition]) {
		taskOriginalLane.insertBefore(task, tasks[taskOriginalPosition]);
	} else {
	    taskOriginalLane.appendChild(task);
	}
}

droppables.forEach((lane) => {
	addDragOverListenerForTasks(lane);
});

function addDragOverListenerForTasks(lane) {
	lane.addEventListener("dragover", (event) => {
		event.preventDefault();
		const curtTask = document.querySelector(".is_dragging");
		if (!curtTask) return;

		const bottomTask = insertAboveTask(lane, event.clientY);

		if (!bottomTask) {
			lane.appendChild(curtTask);
		} else {
			lane.insertBefore(curtTask, bottomTask);
		}
	});
}

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