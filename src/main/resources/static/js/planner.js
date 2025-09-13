
let currentDate = new Date();
let selectedDate = null;
const events = {};


// makes the calendar work
function renderCalendar() {
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
  
  document.getElementById('currentMonthYear').textContent = 
    new Date(year, month).toLocaleString('default', { month: 'long', year: 'numeric' });
  
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const daysInMonth = lastDay.getDate();
  const startingDay = firstDay.getDay();
  
  const calendarDays = document.getElementById('calendarDays');
  calendarDays.innerHTML = '';
  
  // Previous month days
  const prevMonthDays = new Date(year, month, 0).getDate();
  for (let i = startingDay - 1; i >= 0; i--) {
    const dayDiv = createDayElement(prevMonthDays - i, true);
    calendarDays.appendChild(dayDiv);
  }
  
  // Current month days
  for (let day = 1; day <= daysInMonth; day++) {
    const dayDiv = createDayElement(day, false);
    const dateString = `${year}-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    if (events[dateString]) {
      events[dateString].forEach(event => {
        const eventDiv = document.createElement('div');
        eventDiv.className = 'event-marker';
        eventDiv.textContent = `${event.title} (${event.time})`;
        dayDiv.appendChild(eventDiv);
      });
    }
    calendarDays.appendChild(dayDiv);
  }
  
  // Next month days
  const remainingDays = 42 - (startingDay + daysInMonth);
  for (let day = 1; day <= remainingDays; day++) {
    const dayDiv = createDayElement(day, true);
    calendarDays.appendChild(dayDiv);
  }
}

function createDayElement(day, isOtherMonth) {
  const dayDiv = document.createElement('div');
  dayDiv.className = `calendar-day${isOtherMonth ? ' other-month' : ''}`;
  dayDiv.textContent = day;
  if (!isOtherMonth) {
    dayDiv.onclick = () => openEventModal(day);
  }
  return dayDiv;
}

function previousMonth() {
  currentDate.setMonth(currentDate.getMonth() - 1);
  renderCalendar();
}

function nextMonth() {
  currentDate.setMonth(currentDate.getMonth() + 1);
  renderCalendar();
}

// Opens the event popup
function openEventModal(day) {
  selectedDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), day);
  document.getElementById('eventModal').style.display = 'block';
  document.getElementById('eventTitle').value = '';
  document.getElementById('eventTime').value = '';
}

document.getElementById('closeBtn').addEventListener('click', async () => {
	closeModal("eventModal");
});

// Closes any popups
function closeModal(modalID) {
	const modal = document.getElementById(modalID);
    modal.style.display = 'none';
}





// Saves the event to the calendar
function saveEvent() {
  const title = document.getElementById('eventTitle').value;
  const time = document.getElementById('eventTime').value;
  
  if (!title || !time) return;
  
  const dateString = selectedDate.toISOString().split('T')[0];
  if (!events[dateString]) {
    events[dateString] = [];
  }
  
  events[dateString].push({ title, time });
  closeModal('eventModal');
  renderCalendar();
}



let tasks = [];
let editingTaskId = null;

async function getUserId() {
	const res = await fetch('/users/sessionUser', { credentials: 'include' });
	const user = await res.json();
	return user.id || user._id;
}
function formatDueDate(dateInput) {
  const date = new Date(dateInput);
  return date.toISOString().slice(0, 19);
}
async function addTask() { 
	const userId = await getUserId();
	const title = document.getElementById('todoTitle').value;
	const description = document.getElementById('taskDescription').value;
	const priority = document.getElementById('todoPriority').value;
	const categoryType = document.getElementById('taskCategory').value;
	const dueDateRaw = document.getElementById('todoDeadline').value;
	const completed = document.getElementById('taskCompleted').checked || false;

	if (!title || !dueDateRaw) {
		document.getElementById('error-text').textContent = "Title and Due Date are required.";
		return;
	}

	const dueDate = formatDueDate(dueDateRaw);
	
	const newTask = {
		userId,
		title,
		description,
		priority,
		categoryType,
		dueDate,
		completed
	};

	let res;
	if (editingTaskId) {
		res = await fetch(`/tasks/updateTask/${editingTaskId}`, {
			method: 'PATCH',
			credentials: 'include',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(newTask)
		});
	} else {
		res = await fetch('/tasks/createTask', {
			method: 'POST',
			credentials: 'include',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(newTask)
		});
	}

	if (res.ok) {
		fetchTasks();
		clearTaskForm();
	} else {
		const text = await res.text();
		document.getElementById('error-text').textContent = `Error: ${text}`;
	}
}

function clearTaskForm() {
	document.getElementById('todoTitle').value = '';
	document.getElementById('taskDescription').value = '';
	document.getElementById('todoPriority').value = 'low';
	document.getElementById('taskCategory').value = '';
	document.getElementById('todoDeadline').value = '';
	document.getElementById('taskCompleted').checked = false;
	document.querySelector('.task-form button').textContent = "Add Task";
	editingTaskId = null;
}



async function fetchTasks() {
	const userId = await getUserId();
	const res = await fetch(`/tasks/getAllTask/${userId}`, {
		credentials: 'include'
	});
	const data = await res.json();
	tasks = data.map(t => ({ ...t, id: t._id || t.id }));
	displayTasks(tasks);
}

function displayTasks(tasksToDisplay = tasks) {
	const container = document.getElementById('todoList');
	document.getElementById('taskFormButton').textContent = "Add Task";
	container.innerHTML = '';

	if (!tasksToDisplay.length) {
		container.innerHTML = '<p>No tasks found.</p>';
		return;
	}

	tasksToDisplay.forEach(task => {
		const taskDiv = document.createElement('div');
		taskDiv.classList.add('task-item');

		taskDiv.innerHTML = `
			<h3>${task.title}</h3>
			<p><strong>Due:</strong> ${formatDate(task.dueDate)}</p>
			<p><strong>Description:</strong> ${task.description}</p>
			<p><strong>Priority:</strong> ${task.priority}</p>
			<p><strong>Category:</strong> ${task.categoryType}</p>
			<p><strong>Completed:</strong> ${task.completed ? 'Yes' : 'No'}</p>
			<div class="task-actions">
				<button onclick="editTask('${task.id}')">Edit</button>
				<button onclick="deleteTask('${task.id}')">Delete</button>
			</div>
		`;
		container.appendChild(taskDiv);
	});
}

function editTask(id) {
	const task = tasks.find(t => t.id === id);
	if (!task) return;

	document.getElementById('todoTitle').value = task.title;
	document.getElementById('taskDescription').value = task.description || '';
	document.getElementById('todoPriority').value = task.priority || 'low';
	document.getElementById('taskCategory').value = task.categoryType || '';
	document.getElementById('todoDeadline').value = task.dueDate ? task.dueDate.slice(0, 16) : '';
	document.getElementById('taskCompleted').checked = task.completed || false;

	editingTaskId = task.id;

	document.getElementById('taskFormButton').textContent = "Update Task";
}

async function deleteTask(id) {
	const res = await fetch(`/tasks/deleteTask/${id}`, {
		method: 'DELETE',
		credentials: 'include'
	});
	if (res.ok) {
		fetchTasks();
	} else {
		const err = await res.text();
		document.getElementById('error-text').textContent = `Delete failed: ${err}`;
	}
}

function searchTasks() {
	const term = document.getElementById('taskSearch').value.toLowerCase();
	const filtered = tasks.filter(task =>
		task.title.toLowerCase().includes(term) ||
		task.description.toLowerCase().includes(term)
	);
	displayTasks(filtered);
}



/*// Runs the calendar to show on the page
document.addEventListener('DOMContentLoaded', renderCalendar);*/

let userID = null;

// Fetch the logged-in user info from saved session
async function getLoggedInUser() {
  const response = await fetch('/users/getUserByEmail', { credentials: 'include' });
  if (response.ok) {
    const user = await response.json();
    userID = user._id;
    return user;
  } else {
	document.getElementById('error-text').textContent = "Session expired. Please log in again.";
    window.location.href = 'index.html';
  }
}

// Global variables
let reminders = [];
let editingReminderId = null;

// Creates a reminder
async function addReminder() {
	const title = document.getElementById('reminderTitle').value;
	const reminderTime = document.getElementById('reminderDate').value;
	const notes = document.getElementById('reminderNotes').value;
	const repeat = document.getElementById('reminderRepeat').value;

	if (!title || !reminderTime) {
		document.getElementById('error-text').textContent = "Title and Date are required.";
	    return;
	}

	const newReminder = {
	    title,
	    reminderTime,
	    notes,
	    repeat,
		emailSent: false
	};
	
	let res;
	if (editingReminderId) {
		// Update reminder using PATCH
		res = await fetch(`/reminders/updateReminderByEmail/${editingReminderId}`, {
		    method: 'PATCH',
		    credentials: 'include',
		    headers: { 'Content-Type': 'application/json' },
		    body: JSON.stringify(newReminder)
		  });
	} else {
		res = await fetch('/reminders/createReminder', {
				method: 'POST',
			    credentials: 'include',
			    headers: { 'Content-Type': 'application/json' },
			    body: JSON.stringify(newReminder)
		});
	}
	

	if (res.ok) {
		clearReminderForm();
	    fetchReminders();
	} else {
		const text = await res.text();
		  document.getElementById('error-text').textContent = `Error: ${text}`;
	}
}

// Clears reminders
function clearReminderForm() {
	document.getElementById('reminderTitle').value = '';
	  document.getElementById('reminderDate').value = '';
	  document.getElementById('reminderNotes').value = '';
	  document.getElementById('reminderRepeat').value = 'never';
	  document.querySelector('.reminder-form button').textContent = "Add Reminder";
	  editingReminderId = null;
}

async function fetchReminders(){
	const res = await fetch(`/reminders/getUserReminders`, {
	    credentials: 'include'
	  });
	  const data = await res.json();
	  reminders = data.map(r => ({
	      ...r,
	      id: r._id || r.id
	    }));
	  displayReminders(reminders);
}

// Display reminders
function displayReminders(remindersToDisplay = reminders) {
	
	const container = document.getElementById('remindersList');
	  container.innerHTML = ''; // Clear previous content

	  if (!remindersToDisplay.length) {
	    container.innerHTML = '<p>No reminders found.</p>';
	    return;
	  }

	  remindersToDisplay.forEach(reminder => {
	    //const id = reminder._id || reminder.id;
		const reminderDiv = document.createElement('div');
		reminderDiv.classList.add('reminder-item');;

	    reminderDiv.innerHTML = `
	      <h3>${reminder.title}</h3>
	      <p><strong>Date:</strong> ${formatDate(reminder.reminderTime)}</p>
	      <p><strong>Notes:</strong> ${reminder.notes}</p>
	      <p><strong>Repeat:</strong> ${reminder.repeat}</p>
	      <div class="reminder-actions">
	        <button onclick="editReminder('${reminder.id || reminder.id}')">Edit</button>
	        <button onclick="deleteReminder('${reminder.id || reminder.id}')">Delete</button>
	      </div>
	    `;

	    container.appendChild(reminderDiv);
	  });
}

// Searches for reminders by title or notes
function searchReminders() {
	const term = document.getElementById('reminderSearch').value.toLowerCase();
	  const filtered = reminders.filter(reminder =>
	    reminder.title.toLowerCase().includes(term) ||
	    reminder.notes.toLowerCase().includes(term)
	  );
	  displayReminders(filtered);
}

// Lets you edit reminders
function editReminder(id) {
	const reminder = reminders.find(r => r.id === id || r._id === id);
	  if (!reminder) return;

	  document.getElementById('reminderTitle').value = reminder.title;
	  document.getElementById('reminderDate').value = reminder.reminderTime;
	  document.getElementById('reminderNotes').value = reminder.notes;
	  document.getElementById('reminderRepeat').value = reminder.repeat || 'never';

	  editingReminderId = reminder._id || reminder.id;

	  document.querySelector('.reminder-form button').textContent = "Update Reminder";
}

// Deletes reminders
async function deleteReminder(id) {
	const res = await fetch(`/reminders/deleteReminderByEmail/${id}`, {
			method: 'DELETE',
			credentials: 'include'
		});
		if (res.ok) {
			fetchReminders();
		} else {
			const err = await res.text();
			    document.getElementById('error-text').textContent = `Delete failed: ${err}`;
		}
}




function formatDate(dateString) {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
}



//----Dropdown menu----//
// Modify user account info button listener
document.getElementById('modify-account').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('modifyModal').style.display = 'block';
});

// Delete user account button listener
document.getElementById('delete-account').addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('deleteModal').style.display = 'block';
});

// Logout user button listener & function
document.getElementById('logout').addEventListener('click', async (e) => {
    e.preventDefault();

	const response = await fetch('/users/logout', {
	            method: 'POST',
	            credentials: 'include'
	});
	
	const message = await response.text();
	
	if (response.ok) {
	      window.location.href = 'index.html?message=You%20have%20been%20logged%20out';
	} else {
		document.getElementById('error-text').textContent = message;
	}
});


// Function for when click submit in modifyModal popup
async function submitModify () {
	const name = document.getElementById('editName').value;
	const email = document.getElementById('editEmail').value;
	const password = document.getElementById('editPassword').value;
	
	const response = await fetch('/users/updateCurrentUser', {
	            method: 'PATCH',
	            credentials: 'include',
	            headers: {
	                'Content-Type': 'application/json'
	            },
	            body: JSON.stringify({ name, email, password })
	});
	
	const message = await response.text(); 
		
	if (response.ok) {
		document.getElementById('error-text').textContent = message;
		closeModal('modifyModal');
		window.location.reload();
	} else {
		 document.getElementById('error-text').textContent = message;
	}
}

// Function for when click Yes, delete button in deleteModal popup
async function confirmDelete() {
	const response = await fetch('/users/deleteCurrentUser', {
	            method: 'DELETE',
	            credentials: 'include'
	});

	const message = await response.text(); 
	
	if (response.ok) {
	     window.location.href = 'index.html?message=Your%20account%20has%20been%20deleted';
	} else {
		document.getElementById('error-text').textContent = message;
	}
}



// Check if user is logged in to be able to access planner page; Display calendar, reminders
document.addEventListener('DOMContentLoaded', async () => {
	const result = await fetch('/users/checkSession', {
	    credentials: 'include'
	  });
	  const data = await result.json();

	  if (data.loggedIn) {
	    document.getElementById("welcome-title").textContent = "Welcome, " + data.name + "!";
	    /*fetchEntries();*/
		renderCalendar();
		fetchTasks();
		fetchReminders();
	  } else {
	    window.location.href = "index.html?message=Please%20login%20or%20signup%20to%20access%20Digi%20Planner";
	  }
});

