document.getElementById('btn_start_booking_now').addEventListener('click',
    function () {
      const calendar = document.getElementById('calendar');

      // 달력이 보이지 않을 때만 보이도록 설정
      if (calendar.style.display === 'none' || calendar.style.display === '') {
        calendar.style.display = 'block';
        calendar.scrollIntoView({behavior: 'smooth', block: 'start'});
      }
    });