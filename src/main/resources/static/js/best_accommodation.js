document.addEventListener('DOMContentLoaded', async function() {
    try {
        console.log('Script loaded'); // 스크립트가 실행되었는지 확인
        const response = await fetch('/api/accommodations/best'); // API 호출
        if (!response.ok) {
            throw new Error('인기 호스트 방 목록을 가져오는 데 실패했습니다.');
        }

        const rooms = await response.json(); // JSON 형태로 응답 처리
        console.log('Rooms:', rooms); // 응답 내용 확인

        const container = document.getElementById('resultRoomContainer');
        container.innerHTML = ''; // 기존 콘텐츠를 초기화

        rooms.forEach(room => {
            const roomElement = document.createElement('a');
            roomElement.href = `/room/detail/${room.id}`;
            roomElement.target = "_blank"; // 새탭에서 열기

            roomElement.innerHTML = `
                <dl class="room_item">
                    <dt>
                        <img class="room_img" src="${room.imageUrl}" alt="${room.title}">
                        <div class="group">
                            <span class="badge black">인기 호스트</span>
                        </div>
                    </dt>
                    <dd class="room_name">${room.title}</dd>
                    <dd class="room_address">${room.lotAddress}</dd>
                    <dd class="room_pay">
                        <p>
                            <strong>${room.price}</strong><em>원</em>
                            <span>/1박</span>
                        </p>
                    </dd>
                </dl>
            `;

            container.appendChild(roomElement);
        });
    } catch (error) {
        console.error('Error fetching popular rooms:', error);
    }
});
