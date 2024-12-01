// 오늘 날짜에서 6개월 전의 날짜를 YYYY-MM-DD 형식으로 가져오는 함수
function getSixMonthsAgoDate() {
  const today = new Date();
  today.setMonth(today.getMonth() - 6); // 6개월 전으로 설정
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// 오늘 날짜를 YYYY-MM-DD 형식으로 가져오는 함수
function getTodayDate() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더함
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

// Flatpickr 초기화
flatpickr("#s_start_date", {
  dateFormat: "Y-m-d", // 원하는 형식으로 설정 가능
  defaultDate: getSixMonthsAgoDate(), // 초기값을 오늘 날짜로부터 6개월 전으로 설정
  onReady: function (selectedDates, dateStr, instance) {
    instance.setDate(getSixMonthsAgoDate(), false); // 6개월 전 날짜로 설정
  }
});

flatpickr("#s_end_date", {
  dateFormat: "Y-m-d", // 원하는 형식으로 설정 가능
  maxDate: "today", // 오늘 날짜까지만 선택 가능
  defaultDate: getTodayDate(), // 초기값을 오늘 날짜로 설정
  onReady: function (selectedDates, dateStr, instance) {
    instance.setDate(getTodayDate(), false); // 오늘 날짜로 설정
  }
});

// data-user-id 속성에서 사용자 ID를 가져오기
const userId = document.getElementById('user-data').getAttribute(
    'data-user-id');

// DOMContentLoaded 이벤트에서 방 이름 목록을 가져옵니다.
document.addEventListener('DOMContentLoaded', async () => {
  try {
    // 방 이름 목록을 가져오기 위한 API 호출
    const response = await fetch(`/api/host/room/all/${userId}`); // 실제 API URL로 변경
    if (!response.ok) {
      throw new Error('방 이름을 가져오는 데 실패했습니다.');
    }

    const rooms = await response.json(); // JSON 형태로 파싱
    const roomSelect = document.getElementById('opt_room_id');

    // 기존 옵션 제거
    roomSelect.innerHTML = '<option value="-1">방 이름 - 전체</option>';

    // 방 이름을 select에 추가
    rooms.forEach(room => {
      const option = document.createElement('option');
      option.value = room.id; // 방 ID 설정 (예: room.id)
      option.textContent = room.title; // 방 이름 설정 (예: room.name)
      roomSelect.appendChild(option);
    });

    // 페이지 로딩 시 기본으로 "방 이름 - 전체" 선택 상태 설정
    roomSelect.selectedIndex = 0; // 첫 번째 옵션 (방 이름 - 전체)이 기본으로 선택되도록 설정

    // 페이지 로딩되자마자 조회 버튼 클릭
    const searchButton = document.getElementById('btn_search');
    searchButton.click(); // 페이지 로딩 후 바로 조회 버튼 클릭 이벤트 실행

  } catch (error) {
    console.error('오류 발생:', error);
    alert('방 이름을 불러오는 중 오류가 발생했습니다: ' + error.message);
  }
});

// 검색 버튼 클릭 이벤트 리스너
document.getElementById('btn_search').addEventListener('click',
    async function () {
      const startDate = document.getElementById('s_start_date').value;
      const endDate = document.getElementById('s_end_date').value;
      const roomSelect = document.getElementById('opt_room_id');
      const accommodationId = Number(
          roomSelect.options[roomSelect.selectedIndex].value);

      // 선택된 방 이름 출력 (확인용)
      console.log(`선택된 방 아이디: ${accommodationId}`);

      try {
        const response = await fetch(
            `/api/settlement?hostId=${userId}&accommodationId=${accommodationId}&startDate=${startDate}&endDate=${endDate}`);
        if (!response.ok) {
          throw new Error("네트워크 응답이 올바르지 않습니다.");
        }

        const data = await response.json();
        const tableBody = document.getElementById('settlement_table_body');
        tableBody.innerHTML = ''; // 기존 테이블 내용 초기화
        let totalCount = 0;
        let totalAmount = 0;

        // 데이터를 테이블에 추가
        data.forEach(item => {
          const row = document.createElement('tr');
          row.innerHTML = `
                <td>${item.settlementDate}</td>
                <td>${item.title}</td>
                <td>${item.nickname}</td>
                <td>${item.checkInDate}</td>
                <td>${item.paymentAmount}원</td>
            `;
          tableBody.appendChild(row);
          totalCount++;
          totalAmount += parseInt(item.paymentAmount);
        });

        // 총 건수 및 총 금액 업데이트
        document.getElementById('total_count').innerText = totalCount;
        document.getElementById(
            'total_amount').innerText = totalAmount.toLocaleString() + '원';
      } catch (error) {
        console.log('숙소 정보를 가져오는 중 오류 발생: ', error);
      }
    });
