let jhtaLat = 37.572927; // lat = 위도, y
let jhtaLon = 126.992337; // lon = 경도, x

// 거래 장소를 표시할 마커 관련 변수
var marker;
var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(jhtaLat, jhtaLon), // 기본 위치값
      level: 2 // 지도의 확대 레벨
    };

// TODO: 여기 아래 변수  3개의 값을 api로 받아오면 마커에 표시됨!
// 초기 위도, 경도 설정 (여기서는 예시로 설정)
let locationName = "여기서 거래해요."; // 예시로 입력한 장소명
let lastMakerLat = 37.651838298118726; // 예시 위도
let lastMakerLon = 127.06643920700701; // 예시 경도

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 거래 장소 마커 생성 및 인포윈도우 표시
function displayTradeLocation() {
  var latLng = new kakao.maps.LatLng(lastMakerLat, lastMakerLon);

  // 마커 생성
  var imageSrc = '/images/icons/marker.svg', // 마커 이미지 경로
      imageSize = new kakao.maps.Size(64, 69), // 마커 이미지 크기
      imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커 이미지 옵션
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
      imageOption);

  marker = new kakao.maps.Marker({
    position: latLng,
    image: markerImage,
    map: map
  });

  // 인포윈도우 생성
  var iwContent = `<div style="padding:5px;">${locationName}</div>`, // 인포윈도우 내용
      iwPosition = latLng; // 인포윈도우 표시 위치
  var infowindow = new kakao.maps.InfoWindow({
    position: iwPosition,
    content: iwContent
  });

  // 인포윈도우를 마커 위에 표시
  infowindow.open(map, marker);

  // 지도 중심을 거래 장소로 이동
  map.setCenter(latLng);

  fetchAddress(lastMakerLat, lastMakerLon);
}

// 페이지가 로드되면 거래 장소를 표시
window.onload = function () {
  displayTradeLocation();
};

// 현재 접속한 사용자의 주소로 부드럽게 이동
function setCurrentLocation() {
  var moveLatLon = new kakao.maps.LatLng(lat, lon);
  // 지도 중심을 부드럽게 이동
  // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동
  map.panTo(moveLatLon);
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

// 행정동 주소를 가져와서 HTML에 표시하는 함수
function fetchAddress(latitude, longitude) {
  var geocoder = new kakao.maps.services.Geocoder();
  var addressDisplay = document.getElementById('centerAddr');

  // 좌표를 행정 구역 주소로 변환
  geocoder.coord2RegionCode(longitude, latitude, function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
      for (var i = 0; i < result.length; i++) {
        if (result[i].region_type === 'H') { // 행정동일 경우
          addressDisplay.innerHTML = result[i].address_name; // 행정동 주소 표시
          break;
        }
      }
    }
  });
}