const lanes = document.querySelectorAll('.lane');
const board = document.querySelector('.lane_wrapper');
const dragModal = document.querySelector('.drag_modal_container');


let dragginLane;
let laneOriginalPosition;
let isDeleting = false;

lanes.forEach((lane) => {
	setDragAndDropListeners(lane);
});

function setDragAndDropListeners(lane) {
	lane.addEventListener("dragstart", (event) => {
		if (event.target.classList.contains("task")) {
			return; // Ignora o evento de drag na lane se for uma task
		}
		lane.classList.add("is_dragging_lane");
		dragginLane = lane;

		laneOriginalPosition = [...board.children].indexOf(dragginLane);

		showDiv();
	});

	lane.addEventListener("dragend", (event) => {
		lane.classList.remove("is_dragging_lane");
		const newPosition = [...board.children].indexOf(dragginLane);
		
		if(!isDeleting && laneOriginalPosition != newPosition) {	
			updateLanePositionOnBackend(dragginLane, newPosition);
		}

		dragginLane = "";
		isDeleting = false;
		hideDiv();
	});
}

board.addEventListener("dragover", (event) => {
	event.preventDefault();
	if(!dragginLane) return;

	const afterLane = getLaneAfterMouse(board, event.clientX);
	if (!afterLane) {
		board.appendChild(dragginLane);
	} else {
	    board.insertBefore(dragginLane, afterLane);
	}
});

dragModal.addEventListener("dragover", (event) => {
	event.preventDefault();
});

const target = document.getElementById("droptarget");
target.addEventListener("drop", async (event) => {
  event.preventDefault();
  
  isDeleting = true;
  const managedLane = dragginLane;

  if (event.target.className === "drag_modal_container visible") {
	  board.removeChild(managedLane);

	  const data = new URLSearchParams({ action: "DeleteLane", laneId: managedLane.id });
	  try {
		  const response = await fetch("/jtask/lane", {
			  method: "post",
			  body: data
		  });

		  if (!response.ok) {
			  throw new Error();
		  }
	  } catch (error) {
		  alert("Error occurred deleting lane");
		  revertLanePosition(managedLane, laneOriginalPosition);
	  }
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

function showDiv() {
    dragModal.style.display = "flex";  // Make sure the div is in the layout
    setTimeout(() => {
        dragModal.classList.add("visible");
    }, 10); // Small timeout to allow the transition to happen
}

function hideDiv() {
    dragModal.classList.remove("visible"); // Transition out
    setTimeout(() => {
        dragModal.style.display = "none";  // Hide the div after transition
    }, 500); // Delay to match the transition duration (0.5s)
}

async function updateLanePositionOnBackend(draggingLane, newPositionIndex) {
	const data = new URLSearchParams({action: "SwitchLanePosition", laneId: draggingLane.id, newPositionIndex: newPositionIndex});
	
	try {	
		const response = await fetch("/jtask/lane", {
			method: "post",
			body: data
		});
		
		if(!response.ok) {
			throw new Error();
		}
	} catch(error) {
		alert("Error occurred saving lane position");
		revertLanePosition(draggingLane, laneOriginalPosition);
	}
}

function revertLanePosition(lane, laneOriginalPosition) {
	const lanes = [...board.children];
	if (lanes[laneOriginalPosition]) {
		board.insertBefore(lane, lanes[laneOriginalPosition]);
	} else {
	    board.appendChild(lane);
	}
}

