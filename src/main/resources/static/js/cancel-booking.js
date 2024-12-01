document.getElementById(("btn-cancel-booking-popup").onclick = function () {
  fetch('/', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({})
  })
})