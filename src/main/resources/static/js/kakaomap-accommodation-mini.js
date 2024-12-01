// 기본 위도 경도 설정
let jhtaLat = 37.572927;
let jhtaLon = 126.992337;
var locPosition;
var lat, lon; // 사용자 현 위치

// 마커 이미지 생성
var imageSrc = "/images/icons/marker.svg";
var imageSize = new kakao.maps.Size(34, 39);
var imageOption = { offset: new kakao.maps.Point(17, 39) };
var markerImage;

var mapContainer = document.getElementById('map'),
    mapOption = {
        center: new kakao.maps.LatLng(jhtaLat, jhtaLon),
        level: 4
    };
var map = new kakao.maps.Map(mapContainer, mapOption);

// // 숙소 클러스터러
// var accommodationClusterer = new kakao.maps.MarkerClusterer({
//     map: map,
//     averageCenter: true,
//     minLevel: 2,
//     gridSize: 80
// // });
//
// // 현재 내위치
// if (navigator.geolocation) {
//     navigator.geolocation.getCurrentPosition(function (position) {
//         lat = position.coords.latitude;
//         lon = position.coords.longitude;
//         var locPosition = new kakao.maps.LatLng(lat, lon);
//         userMarker(locPosition, '/images/icons/currentlocation.svg');
//     });
// } else {
//     locPosition = new kakao.maps.LatLng(jhtaLat, jhtaLon);
//     userMarker(locPosition, '/images/icons/currentlocation.svg');
// }

// // 현재 내 위치 마커를 생성하는 함수
// function userMarker(locPosition, imageSrcUrl) {
//     markerImage = new kakao.maps.MarkerImage(imageSrcUrl, imageSize, imageOption);
//     var marker = new kakao.maps.Marker({
//         map: map,
//         position: locPosition,
//         image: markerImage
//     });
//     map.setCenter(locPosition);
// }

// // 사용자 현 위치로 이동하는 버튼
// function setCurrentLocation() {
//     var moveLatLon = new kakao.maps.LatLng(lat, lon);
//     map.panTo(moveLatLon);
// }

// // 오버레이 콘텐츠 생성 함수
// function createContent(type, data) {
//     const wrap = document.createElement('div');
//     wrap.className = 'wrap';
//
//     const info = document.createElement('div');
//     info.className = 'info';
//
//     const title = document.createElement('div');
//     title.className = 'title';
//     title.innerHTML = data.name;
//
//     const closeBtn = document.createElement('div');
//     closeBtn.className = 'close';
//     closeBtn.title = '닫기';
//
//     title.appendChild(closeBtn);
//     info.appendChild(title);
//
//     const body = document.createElement('div');
//     body.className = 'body';
//
//     const imgDiv = document.createElement('div');
//     imgDiv.className = 'img';
//     const img = document.createElement('img');
//     // img.src = type === 'accommodation' ? "/images/accommdation-smaple.png" : "/images/accommdation-smaple.png"; // 이미지 URL 설정
//     img.src = data.imageUrl || "/images/accommdation-smaple.png";
//     img.width = 73;
//     img.height = 70;
//     imgDiv.appendChild(img);
//
//     const desc = document.createElement('div');
//     desc.className = 'desc';
//     const ellipsis = document.createElement('div');
//     ellipsis.className = 'ellipsis';
//     ellipsis.innerText = type === 'accommodation' ? data.location : data.venue;
//
//     const performanceInfo = document.createElement('div');
//     performanceInfo.className = 'accommodation-info';
//     performanceInfo.innerText = data.accommodationInfo;
//
//     const ticketLinkDiv = document.createElement('div');
//     const ticketLink = document.createElement('a');
//     ticketLink.href = "/room/detail/" + data.accommodationId; // 숙소 예매 링크
//     ticketLink.target = "_blank";
//     ticketLink.className = 'link';
//     ticketLink.innerText = data.name; // 링크 텍스트
//
//     ticketLinkDiv.appendChild(ticketLink); // 링크를 div에 추가
//
//     desc.appendChild(ellipsis);
//     desc.appendChild(performanceInfo);
//     desc.appendChild(ticketLinkDiv); // desc에 티켓 링크 추가
//     body.appendChild(imgDiv);
//     body.appendChild(desc);
//     info.appendChild(body);
//     wrap.appendChild(info);
//     return wrap; // DOM 요소 반환
// }

// function createAccommodationContent(accommodation) {
//     const accommodationInfo =
//         `방 ${accommodation.room}/ ` +
//         `화장실 ${accommodation.bath}/ ` +
//         `거실 ${accommodation.living}/ ` +
//         `주방 ${accommodation.kitchen}`;
//
//     return createContent('accommodation', {
//         name: accommodation.title,
//         imageUrl: accommodation.imageUrl, // responentity에서 이미지 URL 가져오기
//         location: accommodation.lotAddress, // 숙소 위치
//         accommodationId: accommodation.accommodationId,
//         accommodationInfo: accommodationInfo
//     });
// }
//
// // 오버레이 생성 함수
// function createOverlay(content, map, position) {
//     const overlay = new kakao.maps.CustomOverlay({
//         content: content,
//         map: null,
//         position: position
//     });
//
//     // Close button 클릭 시 오버레이 닫기
//     const closeBtn = content.querySelector('.close');
//     closeBtn.addEventListener('click', function () {
//         closeOverlay(overlay);
//     });
//
//     return overlay; // 생성된 오버레이 반환
// }

// 마커 생성 함수
function createAccommodationMarker(position, imageScrUrl, map, overlay) {
    markerImage = new kakao.maps.MarkerImage(imageScrUrl, imageSize, imageOption);
    var marker = new kakao.maps.Marker({
        position: position,
        image: markerImage,
        map: map
    });

    // 오버레이를 마커에 연결
    marker.kakaoOverlay = overlay;

    return marker;
}

// 오버레이 닫기 함수
function closeOverlay(overlay) {
    if (overlay) {
        overlay.setMap(null); // 오버레이를 지도에서 제거
    } else {
        console.error("오버레이가 존재하지 않습니다.");
    }
}

var accommodationMarkers = []; // 숙소 마커 저장

// 데이터 가져오기
fetch("/api/accommodation-data")
    .then(response => response.json())
    .then(data => {
        data.accommodations.forEach(accommodation => {
            const position = new kakao.maps.LatLng(accommodation.latitude, accommodation.longitude);
            const overlayContent = createAccommodationContent(accommodation);
            const overlay = createOverlay(overlayContent, map, position);
            const marker = createAccommodationMarker(position, "/images/icons/exh.svg", map, overlay); // 오버레이를 전달

            // 마커 클릭 시 오버레이 표시
            kakao.maps.event.addListener(marker, 'click', function () {
                overlay.setMap(map);
            });

            accommodationMarkers.push(marker); // 전시회 마커 저장
            accommodationClusterer.addMarker(marker); // 클러스터러에 마커 추가
        });
    })
    .catch(error => console.error('Error fetching accommodation data:', error));
