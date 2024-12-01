document.addEventListener('DOMContentLoaded', function () {
  const keyword = window.keyword; // 맵핑 뒤에 가져올 keyword

  console.log(`API 호출: ${keyword}`);
  fetch(`/guest/` + keyword) // API 호출하기
  .then(response => {
    if (!response.ok) {
      throw new Error("error");
    }
    return response.json(); // json 형태로 리턴
  })
  .then(rooms => {
    console.log(rooms);
    const roomList = document.getElementById('roomList');
    roomList.innerHTML = ''; // 기존의 내용 지우기

    rooms.forEach(room => {
      const roomItem = document.createElement('div');
      roomItem.classList.add("col-md-3", "mb-4");
      roomItem.innerHTML = `
                    <div class="room-container">
                        <a href="/room/detail/${room.id}" target="_blank">
                            <img src="${room.imageUrl}" class="card-img-top" alt="숙소 이미지"> 
                            <div class="room-info">
                                <h5 class="room-title">${room.title}</h5>
                                <p class="room-addr">${room.roadAddress}</p>
                                <p class="room-price">${room.price} 원 / 1주</p>
                                <p class="room-count"> 
                                 방 ${room.roomTypeCount[0]} / 화장실 ${room.roomTypeCount[1]} / 거실 ${room.roomTypeCount[2]} / 주방 ${room.roomTypeCount[3]} 
                                </p>
                            </div>
                        </a>
                    </div>
                `;
      roomList.appendChild(roomItem);
    })
  })
  .catch(error => {
    console.error('방 정보를 가져오는 중 오류 발생: ', error)
  })
});