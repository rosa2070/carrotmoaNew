document.addEventListener("DOMContentLoaded", function () {
  const bookingButton = document.getElementById('btn_start_booking_now');

  if (!bookingButton) {
    console.error("btn_start_booking_now 요소를 찾을 수 없습니다!");
    return;
  }

  bookingButton.addEventListener('click', function () {
    // 숙소 정보 가져오기
    const title = document.getElementById('room-info-title').textContent;
    const addr = document.getElementById('room-addr-info').textContent;
    const name = document.getElementById('host_name').textContent;
    const price = parseFloat(document.getElementById('room_info_price').textContent);

    // 숙박일수 가져오기
    const nightsCountElement = document.getElementById('nights-count');
    const nightsCountText = nightsCountElement.textContent.trim();
    const night = parseFloat(nightsCountText);
    const totalPrice = price * night;

    // sessionStorage에 데이터 저장
    sessionStorage.setItem('room-info-title', title);
    sessionStorage.setItem('room-addr-info', addr);
    sessionStorage.setItem('host_name', name);
    sessionStorage.setItem('room_info_price', totalPrice);

    console.log("숙소 정보가 sessionStorage에 저장되었습니다!");

    // 숙소 ID 가져오기
    const roomId = bookingButton.getAttribute('data-accommodation-id');

    if (!roomId) {
      console.error("숙소 ID가 설정되지 않았습니다.");
      return;
    }

    // 페이지 이동
    window.location.href = `/guest/booking/start/${roomId}`;
  });
});
