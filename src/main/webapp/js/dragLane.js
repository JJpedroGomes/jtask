const lanes = document.querySelectorAll('.lane');
const board = document.querySelector('.lane_wrapper');
const dragModal = document.querySelector('.drag_modal_container');

let dragginLane;

lanes.forEach((lane) => {
	lane.addEventListener("dragstart", (event) => {
		if (event.target.classList.contains("task")) {
			return; // Ignora o evento de drag na lane se for uma task
		}
		console.log("Dragging", lane);
		lane.classList.add("is_dragging_lane");
		dragginLane = lane;
		dragModal.style.display = "flex";
	});
	lane.addEventListener("dragend", () => {
		lane.classList.remove("is_dragging_lane");
		dragginLane = "";
		dragModal.style.display = "none";
	});
});

board.addEventListener("dragover", (event) => {
	event.preventDefault;	
	if(!dragginLane) return;

	const afterLane = getLaneAfterMouse(board, event.clientX);
	if (!afterLane) {
		board.appendChild(dragginLane);
	} else {
	    board.insertBefore(dragginLane, afterLane);
	}
});

const getLaneAfterMouse = (board, mouseX) => {
	const lanesInBoard = board.querySelectorAll(".lane:not(.is_dragging_lane)");
	
	let closestLane = null;
	let closestOffset = Number.NEGATIVE_INFINITY;
	
	lanesInBoard.forEach((lane) => {
		const { left } = lane.getBoundingClientRect();
		const offset = mouseX - left;
		
		if (offset < 0 && offset > closestOffset) {
			closestOffset = offset;
		    closestLane = lane;
		}
	});
	return closestLane;
};