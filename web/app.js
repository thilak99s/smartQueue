const state = { role: 'user', position: 4, wait: 12, ahead: 3, visitors: 4 };
const views = ['dashboard', 'appointment', 'analytics'];
const toast = document.querySelector('#toast');
let toastTimer;

function showToast(message) {
  toast.textContent = message;
  toast.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove('show'), 2800);
}

function navigate(view) {
  views.forEach((name) => {
    document.querySelector(`#${name}View`).classList.toggle('active', name === view);
  });
  document.querySelectorAll('[data-view]').forEach((button) => {
    button.classList.toggle('active', button.dataset.view === view);
  });
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

document.querySelectorAll('[data-view]').forEach((button) => {
  button.addEventListener('click', () => navigate(button.dataset.view));
});

document.querySelector('#roleButton').addEventListener('click', () => {
  state.role = state.role === 'user' ? 'admin' : 'user';
  document.querySelector('#roleButton').innerHTML = `${state.role === 'user' ? 'User' : 'Admin'} view <span>↔</span>`;
  showToast(`${state.role === 'user' ? 'User' : 'Admin'} dashboard enabled`);
});

document.querySelector('#notificationsButton').addEventListener('click', () => showToast('You are 3 places away from the counter.'));
document.querySelector('#leaveQueue').addEventListener('click', () => {
  document.querySelector('#leaveQueue').textContent = 'Queue left';
  document.querySelector('#leaveQueue').disabled = true;
  document.querySelector('#positionMetric').textContent = '--';
  document.querySelector('#waitMetric').textContent = '--';
  showToast('Your active ticket has been cancelled.');
});

document.querySelectorAll('.service-card').forEach((card) => card.addEventListener('click', () => {
  document.querySelector('#serviceSelect').value = card.dataset.service === 'General enquiry' ? 'General enquiry' : card.dataset.service;
  navigate('appointment');
}));

document.querySelector('#bookingForm').addEventListener('submit', (event) => {
  event.preventDefault();
  showToast(`Appointment reserved for ${document.querySelector('#serviceSelect').value}.`);
});

const dialog = document.querySelector('#simulationDialog');
const range = document.querySelector('#visitorRange');
const output = document.querySelector('#visitorOutput');
range.addEventListener('input', () => { output.value = `${range.value} visitors`; output.textContent = `${range.value} visitors`; });
document.querySelector('#openSimulation').addEventListener('click', () => dialog.showModal());
document.querySelector('#applySimulation').addEventListener('click', () => {
  state.visitors = Number(range.value);
  state.wait = Math.max(4, 12 + (state.visitors - 4) * 2);
  state.position = state.visitors + 1;
  state.ahead = state.visitors;
  document.querySelector('#waitMetric').textContent = `${state.wait} min`;
  document.querySelector('#positionMetric').textContent = `#${String(state.position).padStart(2, '0')}`;
  document.querySelector('#aheadCount').textContent = state.ahead;
  document.querySelector('#queueProgress').style.width = `${Math.max(26, 82 - state.visitors * 4)}%`;
  showToast('Queue estimate recalculated.');
});
