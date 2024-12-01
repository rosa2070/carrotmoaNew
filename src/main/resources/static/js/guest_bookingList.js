// 문서가 로드된 후에 이벤트 리스너를 추가합니다.
document.addEventListener('DOMContentLoaded', () => {
  const cancelButtons = document.querySelectorAll('.btn-cancel-booking-popup'); // 클래스로 모든 버튼 선택

  cancelButtons.forEach(cancelButton => {
    cancelButton.addEventListener('click', () => {
      const impUid = cancelButton.getAttribute('data-imp-uid'); // 각 버튼에서 data-imp-uid 값을 가져오기
      console.log('impUid:', impUid); // impUid 값 로그 출력

      // 사용자에게 취소 여부 확인
      const isConfirmed = confirm("정말로 취소하시겠습니까?"); // 확인/취소 팝업

      if (isConfirmed) {
        cancelBooking(impUid); // 취소가 확인되면 cancelBooking 함수 호출
      } else {
        // 취소가 취소되었습니다.
      }
    });
  });
});

// cancelBooking 함수 정의
async function cancelBooking(impUid) {
  try {
    const response = await fetch(`/api/payment/cancel/${impUid}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      throw new Error('결제 취소에 실패했습니다.');
    }

    const result = await response.text(); // 문자열로 응답 받기
    alert(result); // 성공 메시지 알림

    // 페이지 새로 고침: 결제가 성공적으로 완료되었으면 페이지를 새로 고침
    location.href = location.href;  // 현재 페이지를 다시 로드하여 새로고침

  } catch (error) {
    console.error(error);
    alert('오류가 발생했습니다: ' + error.message);
  }
}
