document.addEventListener('click', function (event) {
  const title = document.getElementById('room-info-title').textContent;
  const addr = document.getElementById('room-addr-info').textContent;
  const name = document.getElementById('host_name').textContent;
  const price = parseFloat(
      document.getElementById('room_info_price').textContent);
  // const night = document.getElementById('nights-count');
  const nightsCountElement = document.getElementById('nights-count');
  const nightsCountText = nightsCountElement.textContent.trim(); // 공백 제거
  const night = parseFloat(nightsCountText);
  const totalPrice = price * night;

  console.log(price);
  console.log(night);
  console.log(totalPrice);

  sessionStorage.setItem('room-info-title', title);
  sessionStorage.setItem('room-addr-info', addr);
  sessionStorage.setItem('host_name', name);
  sessionStorage.setItem('room_info_price', totalPrice);
})