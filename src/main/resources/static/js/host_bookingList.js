// 드롭다운 메뉴에서 선택된 값에 따라 필터링된 데이터를 보여주기
const userId = document.getElementById('user-data').getAttribute(
    "data-user-id");

document.addEventListener('DOMContentLoaded', () => {
  // 드롭다운 값 변화에 따른 필터링
  const searchStatusSelect = document.getElementById('search_status');
  searchStatusSelect.addEventListener('change', () => {
    const selectedStatus = searchStatusSelect.value;
    fetchReservations(selectedStatus);
  });

  // 초기 페이지 로드 시, 모든 예약을 불러옴
  fetchReservations('using_before');
});

async function fetchReservations(status) {
  try {
    // API 호출하여 예약 정보 가져오기
    const response = await fetch(`/api/host/reservation/${userId}`); // 실제 API URL로 변경
    if (!response.ok) {
      throw new Error('예약 정보를 가져오는 데 실패했습니다.');
    }

    const reservations = await response.json(); // JSON 형태로 파싱
    const hostContractDiv = document.querySelector('.guest_contract');
    hostContractDiv.innerHTML = ''; // 기존 계약 항목 초기화

    // status 값에 맞춰 필터링 (빈 값이면 전체 가져오기)
    const filteredReservations = reservations.filter(reservation => {
      if (status === '') {
        return true;
      } // 모든 예약 가져오기
      if (status === 'using_before' && reservation.status === 1) {
        return true;
      } // 계약 완료 상태
      if (status === 'using_after' && reservation.status === 3) {
        return true;
      } // 계약 만료 상태
      return false; // 그 외 상태는 제외
    });

    // 필터링된 예약 목록을 화면에 추가
    filteredReservations.forEach(reservation => {
      const contractItem = document.createElement('div');
      contractItem.className = 'contract_item';

      // status에 따라 배지 텍스트 설정
      let badgeText;
      let badgeClass = '';

      switch (reservation.status) {
        case 1:
          badgeText = '계약 완료';
          badgeClass = 'orange';
          break;
        case 3:
          badgeText = '계약 만료';
          badgeClass = 'gray';
          break;
        default:
          break;
      }

      const contractHeader = `
                    <div class="contract_header">
                        <div class="flex contract_info" style="cursor:pointer;" onclick="window.location.href='/room/detail/${reservation.accommodationId}'">
                            <span class="badge ${badgeClass}">${badgeText}</span>
                            <p class="room_title">${reservation.title}</p>
                        </div>
                    </div>
                `;

      const contractContent = `
                    <div class="contract_cont" style="align-items:center">
                        <dl class="room_item" style="cursor:pointer;" onclick="window.location.href='/room/detail/${reservation.accommodationId}'">
                            <dt>
                                <img class="room_img" src="${reservation.imageUrl}" alt="">
                            </dt>
                            <dd>
                                <div class="room_address ellipsis">${reservation.lotAddress} ${reservation.detailAddress} ${reservation.floor}층</div>
                                <div class="room_address ellipsis">${reservation.guestName}</div>
                                <p class="room_period">${reservation.checkInDate} ~ ${reservation.checkOutDate}</p>
                            </dd>
                        </dl>
                        <strong class="room_pay">${reservation.totalPrice} 원</strong>
                    </div>
                `;

      contractItem.innerHTML = contractHeader + contractContent;

      // 계약 리스트에 추가
      hostContractDiv.appendChild(contractItem);
    });
  } catch (error) {
    console.error('오류 발생:', error);
    alert('예약 정보를 불러오는 중 오류가 발생했습니다: ' + error.message);
  }
}