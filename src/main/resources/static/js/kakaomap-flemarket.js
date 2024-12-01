// 중고거래 장소 선정에 쓸 마커임. 이걸 전역변수로 선언해줘야. 거래장소 등록할 때 드래그를 금지시킬 수 있음.
var marker;
var locPosition;
var lat;
var lon;
// var lastMakerPosition;

// 기본 위도 경도 설정
let jhtaLat = 37.572927; // lat = 위도, y
let jhtaLon = 126.992337; // lon = 경도, x

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(jhtaLat, jhtaLon), // 기본 위치값: 중앙hta 주소 표시
      level: 2 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// HTML5의 geolocation으로 사용할 수 있는지 확인 -> 만약 사용할 수 있다면 현재 위치와 메세지 변수를 저장해서 displayMarker 함수 실행.
if (navigator.geolocation) {
  // GeoLocation을 이용해서 접속 위치를 얻어옵니다
  navigator.geolocation.getCurrentPosition(function (position) {
    lat = position.coords.latitude; // 위도
    lon = position.coords.longitude; // 경도
    var locPosition = new kakao.maps.LatLng(lat, lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성
    // 현재 위치 마커 표시
    displayMarker(locPosition);

    // 마커 생성(중고거래 할 장소, 이미지 포함)
    createTradeMarker(map.getCenter());

    // 지도 이동 시 중앙에 마커를 유지하는 이벤트 리스너 추가
    kakao.maps.event.addListener(map, 'center_changed', updateMarkerPosition);
  });
} else { // HTML5의 GeoLocation을 사용할 수 없을때 설정.
  locPosition = new kakao.maps.LatLng(jhtaLat, jhtaLon);
  displayMarker(locPosition);
  // 마커 생성(중고거래 할 장소)
  var markerPosition = new kakao.maps.LatLng(lat, lon);
  marker = new kakao.maps.Marker({
    position: markerPosition
  });
  marker.setMap(map);
  marker.setDraggable(true);
}

function updateMarkerPosition() {
  var centerPosition = map.getCenter();
  marker.setPosition(centerPosition);
  // lastMakerPosition = marker.getPosition();
  var geocoder = new kakao.maps.services.Geocoder();

  // 위치 정보 좌표를 얻은 후 행정 구역 반환하기
  var addressDisplay = document.getElementById('addressDisplay');
  var centerAddr = document.getElementById('centerAddr');
  var callback = function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
      for (var i = 0; i < result.length; i++) {
        // console.log(result); // 값이 행정동 값과 법정동 값 두개 들어오는데, 그 중 h로 행정동의 값만 출력함.
        if (result[i].region_type === 'H') { // 행정동일 경우
          // console.log("행정동 이름: " + result[i].address_name); // 전체 주소 출력
          addressDisplay.innerHTML = "현재 마커의 위치는 '"
              + result[i].region_3depth_name + "' 입니다."
          centerAddr.innerHTML = result[i].address_name;
          // 행정동 이름 출력
          break; // 첫 번째 행정동 이름만 출력
        }
      }
    }
  };
  geocoder.coord2RegionCode(marker.getPosition().La, marker.getPosition().Ma,
      callback);
}

function createTradeMarker(LatLng) {
  var imageSrc = '/images/icons/marker.svg',
      imageSize = new kakao.maps.Size(64, 69),
      imageOption = {offset: new kakao.maps.Point(27, 69)};
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
      imageOption);
  marker = new kakao.maps.Marker({
    position: LatLng,
    image: markerImage,
    map: map
  });
}

// 사용자 현재 위치 마커(위에서 함수 실행)
function displayMarker(locPosition) {
  var imageSrc = '/images/icons/currentlocation.svg', // 마커이미지의 주소
      imageSize = new kakao.maps.Size(34, 39), // 마커이미지의 크기입니다
      imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
      imageOption);
  // 마커를 생성합니다
  var userMarker = new kakao.maps.Marker({
    map: map,
    position: locPosition,
    image: markerImage // 마커이미지 설정
  });
  // 지도 중심좌표를 접속위치로 변경합니다
  map.setCenter(locPosition);
}

// 현재 접속한 사용자의 주소로 부드럽게 이동
function setCurrentLocation() {
  var moveLatLon = new kakao.maps.LatLng(lat, lon);
  // 지도 중심을 부드럽게 이동
  // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동
  map.panTo(moveLatLon);
}

function handleConfirm() {
  let confirmButton = document.getElementById('confirmButton');
  confirmButton.innerHTML = '거래 장소 등록';
  confirmButton.style.background = '#5B626A';
  confirmButton.style.color = 'black';
  let inputSection = document.getElementById('inputSection');
  inputSection.style.display = 'block';
  kakao.maps.event.removeListener(map, 'center_changed', updateMarkerPosition);
  confirmButton.onclick = handleRegistration; // 새로운 함수로 설정
}

function handleRegistration() {
  let locationName = document.getElementById('locationInput').value;
  let confirmButton = document.getElementById('confirmButton');
  let inputSection = document.getElementById('inputSection');

  if (locationName) {
    alert('등록된 장소: ' + locationName); // 예시로 알림창으로 출력
    // 추가적인 기능 구현 가능 (예: 서버에 전송)

    // 인포윈도우 생성
    var iwContent = `<div style="padding:5px;" > ${locationName}</div>`, // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        iwPosition = new kakao.maps.LatLng(lat, lon); //인포윈도우 표시 위치입니다
    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
      position: iwPosition,
      content: iwContent
    });
    // 마커 위에 인포윈도우를 표시.
    infowindow.open(map, marker);

    confirmButton.style.display = 'none'; // 버튼 숨기기
    inputSection.style.display = 'none'; // 입력 섹션 숨기기

    const lastMakerLat = marker.getPosition().Ma;
    const lastMakerLon = marker.getPosition().La;

    console.log("마지막 마커의 위도 : " + lastMakerLat);
    console.log("마지막 마커의 경도 : " + lastMakerLon);

    var geocoder = new kakao.maps.services.Geocoder();

    var callback = function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        for (let i = 0; i < result.length; i++) {
          console.log(result); // 객체 2개 반환
        }
      }
    };
    geocoder.coord2RegionCode(lastMakerLon, lastMakerLat, callback);
  } else {
    alert('장소명을 입력해주세요.'); // 입력하지 않았을 경우 경고
  }
}

