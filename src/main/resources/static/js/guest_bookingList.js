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

        const responseData = await response.json(); // JSON 응답 처리

        if (!response.ok) {
            throw new Error(responseData.error || "결제 취소 실패");
        }

        // 성공적으로 결제가 취소된 경우
        alert(responseData.message);
        location.reload(); // 페이지 새로고침하여 변경사항 반영

    } catch (error) {
        console.error(error);
        alert(error.message); // 오류 메시지 출력
    }
}
