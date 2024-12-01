document.addEventListener('DOMContentLoaded', function () {
  var calendarEl = document.getElementById('calendar');
  var reservationContainer = document.querySelector('.reservation-container');
  if (!reservationContainer) {
    console.error('reservation-container 요소를 찾을 수 없습니다.');
    return; // 요소가 없으면 함수 종료
  }
  var id = reservationContainer.getAttribute('data-accommodation-id');
  if (!id) {
    console.error('Accommodation ID를 찾을 수 없습니다.');
    return; // ID가 없으면 함수 종료
  }
  var today = new Date(); // 오늘
  var tomorrow = new Date(today);
  tomorrow.setDate(today.getDate() + 1);
  var sixMonthsLater = new Date(today.getFullYear(), today.getMonth() + 6, 1); // 6개월 후
  var checkInDate = null;
  var checkOutDate = null;
  var bookedDates = [];

  // 예약된 날짜 가져오기
  fetch(`/guest/booking/${id}`)
  .then(response => response.json())
  .then(data => {
    console.log(data)
    const events = data.map(booking => ({
      start: booking.checkInDate,
      end: booking.checkOutDate,
      display: 'background', // 배경으로 표시하여 예약 불가 처리
      color: '#FF6F0F', // 예약된 날짜의 배경색
    }));

    bookedDates = data.map(booking => ({
      start: new Date(booking.checkInDate),
      end: new Date(booking.checkOutDate)
    }))

    var calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      // YYYY년 MM월
      titleFormat: function (date) {
        return `${date.date.year}년 ${date.date.month + 1}월`;
      },
      selectable: true,
      selectAllow: function (info) {
        var selectedStart = info.start;
        var selectedEnd = info.end;

        return !bookedDates.some(booked => {
          var bookedEndPermit = new Date(booked.end);
          bookedEndPermit.setDate(bookedEndPermit.getDate() - 1);

          return (selectedStart < bookedEndPermit && selectedEnd
              > booked.start);
        });
      },
      // dragScroll: true,
      validRange: {
        start: tomorrow.toISOString().split('T')[0], // 오늘부터 예약 가능
        end: sixMonthsLater.toISOString().split('T')[0] // 6개월까지만 예약 가능
      },
      events: events, // 예약된 날짜를 events로 추가
      select: function (info) {
        // 하루만 선택하는 거 불가
        const endDate = new Date(info.end.getFullYear(), info.end.getMonth(),
            info.end.getDate());
        endDate.setDate(info.end.getDate() - 1);
        console.log(endDate);

        if (info.start.getDate() == endDate.getDate()) {
          alert('최소 1박이상 가능합니다, 하루 이상 선택해주세요.');
          calendar.unselect();
          return;
        }

        // var checkInDate = info.startStr; // 문자열로 받아오는 방식
        var checkInDate = info.start; // Date 객체로 받아오는 방식
        var updatedCheckInDate = new Date(checkInDate);
        var checkOutDate = info.end;
        updatedCheckInDate.setDate(updatedCheckInDate.getDate() + 1);

        if (checkInDate && checkOutDate) {
          document.getElementById(
              'checkin-dates').textContent = updatedCheckInDate.toISOString().split(
              'T')[0];
          document.getElementById(
              'checkout-dates').textContent = checkOutDate.toISOString().split(
              'T')[0];
          sessionStorage.setItem('checkin-dates',
              updatedCheckInDate.toISOString().split('T')[0]);
          sessionStorage.setItem('checkout-dates',
              checkOutDate.toISOString().split('T')[0]);
          // 계약 시작하기 버튼 비활성화 풀기
          document.getElementById("btn_start_booking_now").classList.remove(
              'disabled');
          document.getElementById(
              "btn_start_booking_now").dataset.enabled = "true";

        }
        var timeDifference = checkOutDate - checkInDate;
        var countNights = ((timeDifference) / (1000 * 60 * 60 * 24));
        document.getElementById('nights-count').textContent = countNights;
      },
      unselect: function () {
        if (!checkOutDate) {
          checkInDate = null;
          calendar.getEvents().forEach(event => {
            if (event.display == 'background' && event.color == '#FF6F0F') {
              event.remove();
            }
          });
        }
      }
    });
    calendar.render();
    document.getElementById("btn_start_booking_now").classList.add('disabled');
    document.getElementById("btn_start_booking_now").dataset.enabled = "false";

    document.getElementById("btn_start_booking_now").addEventListener("click",
        function (event) {
          if (this.dataset.enabled !== "true") {
            event.preventDefault();
            alert("날짜를 먼저 선택해주세요.")
          }
        })
  })
  .catch(error => console.error('Error fetching bookings:', error));
});