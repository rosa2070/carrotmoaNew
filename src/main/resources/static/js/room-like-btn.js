// 찜하기 버튼 클릭 시 동작
document.addEventListener('DOMContentLoaded', function() {
    // 로그인된 사용자의 ID 가져오기
    const userIdElement = document.getElementById('user-data');
    const likeButton = document.getElementById('likeButton');
    const accommodationId = likeButton.getAttribute("data-accommodation-id");

    // 찜하기 버튼 클릭 이벤트
    likeButton.addEventListener('click', function() {
        const userId = userIdElement ? userIdElement.getAttribute('data-user-id') : null;

        if (!userId) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            window.location.href = "/user/login-page"; // 로그인 페이지로 리다이렉트
        } else {
            // 로그인된 사용자일 경우 찜하기 기능 처리
            likeButton.classList.toggle("is_active");

            // 상태 및 userId 출력
            console.log(likeButton.classList.contains("is_active") ? "찜하기 활성화됨" : "찜하기 비활성화됨");
            console.log("User ID: ", userId);
            console.log("accommodation ID: ", accommodationId);

            // API 호출 (찜하기 상태 확인)
            fetch(`/api/room/${accommodationId}/like?userId=${userId}`, {
                method: 'GET'
            })

                .then(response => response.json())
                .then(data => {
                    console.log("찜 상태 확인 결과: ", data);
                    if (data) {
                        // 이미 찜한 상태라면 취소, 아니면 찜하기 추가
                        fetch(`/api/room/${accommodationId}/like?userId=${userId}`, {
                            method: 'POST'
                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log(data); // "찜하기 성공" 또는 "찜하기 취소됨"
                            })
                            .catch(error => {
                                console.error('Error: ', error);
                            });
                    } else {
                        // 찜하지 않았다면 찜하기 추가
                        fetch(`/api/room/${accommodationId}/like?userId=${userId}`, {
                            method: 'POST'
                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log(data); // "찜하기 성공" 또는 "찜하기 취소됨"
                            })
                            .catch(error => {
                                console.log('Error:', error);
                            });
                    }
                })
                .catch(error => {
                    console.log('Error:', error);
                })

        }
    });

})