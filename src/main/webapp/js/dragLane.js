const lanes = document.querySelectorAll('.lane');
const board = document.querySelector('.lane_wrapper');

lanes.forEach((lane) => {
	lane.addEventListener("dragstart", (event) => {
		if (event.target.classList.contains("task")) {
			return; // Ignora o evento de drag na lane se for uma task
		}
		lane.classList.add("is_dragging_lane");
	});
	lane.addEventListener("dragend", () => {
		lane.classList.remove("is_dragging_lane");
	});
});

board.addEventListener("dragover", (event) => {
	event.preventDefault;
	const draggingLane = document.querySelector(".is_dragging_lane");
	
	const afterLane = getLaneAfterMouse(board, event.clientX);
	if (!afterLane) {
		board.appendChild(draggingLane);
	} else {
	    board.insertBefore(draggingLane, afterLane);
	}
});

const getLaneAfterMouse = (board, mouseX) => {
	const lanesInBoard = [...board.querySelectorAll(".lane:not(.is_dragging_lane)")];
	console.log(lanesInBoard);
	
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