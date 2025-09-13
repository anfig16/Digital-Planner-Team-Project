let editingJournalId = null;

async function getUserId() {
	const res = await fetch('/users/sessionUser', { credentials: 'include' });
	const user = await res.json();
	return user.id || user._id;
}
function formatDueDate(dateInput) {
  const date = new Date(dateInput);
  return date.toISOString().slice(0, 19);
}

async function createJournalEntry() {
  userId = await getUserId();
  const title = document.getElementById('journalTitle').value;
  const content = document.getElementById('journalContent').value;
  const date = document.getElementById('entry-date').value;

  if (editingJournalId) {
    // Update existing journal entry
    await fetch(`/journal/updateJournalEntry/${editingJournalId}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, content, date })
    });

    editingJournalId = null;
    document.getElementById('addEntryBtn').textContent = 'Add Entry';
  } else {
    // Create new journal entry
    await fetch('/journal/createJournalEntry', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId, title, content, date })
    });
  }

  document.getElementById('journalTitle').value = '';
  document.getElementById('journalContent').value = '';
  document.getElementById('entry-date').value = '';
  fetchEntries();
}

document.getElementById('addEntryBtn').addEventListener('click', createJournalEntry);


async function fetchEntries() {
	userId = await getUserId();
      const res = await fetch(`/journal/getJournalEntries/${userId}`);
      const entries = await res.json();
      renderEntries(entries);
}

function renderEntries(entries) {
      const container = document.getElementById('entriesList');
      container.innerHTML = '';
      entries.forEach(entry => {
        const div = document.createElement('div');
        div.classList.add('journal-entry');
        div.innerHTML = `
          <h3>${entry.title}</h3>
          <p class="entry-date">${entry.date}</p>
          <p>${entry.content}</p>
          <button onclick="editEntry('${entry.id}')">Edit</button>
          <button onclick="deleteEntry('${entry.id}')">Delete</button>
        `;
        container.appendChild(div);
      });
}

async function deleteEntry(id) {
      await fetch(`/journal/deleteJournalEntry/${id}`, { method: 'DELETE' });
      fetchEntries();
}



async function editEntry(id) {
  const res = await fetch(`/journal/getJournalEntry/${id}`);
  const entry = await res.json();

  document.getElementById('journalTitle').value = entry.title || '';
  document.getElementById('journalContent').value = entry.content || '';
  document.getElementById('entry-date').value = entry.date?.slice(0, 10) || '';

  editingJournalId = id;
  document.getElementById('addEntryBtn').textContent = 'Update Entry';
}

document.getElementById("openSidebarBtn").onclick = function () {
     document.getElementById("sidebar").style.width = "250px";
};
 document.getElementById("closeSidebarBtn").onclick = function () {
     document.getElementById("sidebar").style.width = "0";
};

// Load and display journal entries
document.addEventListener('DOMContentLoaded', async () => {
	fetchEntries();
});
