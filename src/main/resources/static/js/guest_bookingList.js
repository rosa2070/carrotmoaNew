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
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        // 서버 응답이 정상적이지 않을 경우 (400번대 & 500번대)
        if (!response.ok) {
            const errorMessage = await response.text();  // 서버에서 반환한 메시지 가져오기

            if (response.status >= 400 && response.status < 500) {
                throw new Error('잘못된 요청입니다: ' + errorMessage);
            } else if (response.status >= 500) {
                throw new Error('서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
            } else {
                throw new Error('결제 취소 실패: ' + errorMessage);
            }
        }

        // 성공적으로 결제가 취소되었을 경우
        const result = await response.text();
        alert(result); // 성공 메시지 표시
        location.reload();  // 페이지 새로고침하여 상태 반영

    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}
