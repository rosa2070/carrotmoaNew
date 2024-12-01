// 1. 동네 인증하기 -> 버튼 x,

// 기본 위도 경도 설정
let jhtaLat = 37.572927;
let jhtaLon = 126.992337;
let userId;
let address;
let addressData;
if(document.getElementById("user-id")){
    userId = document.getElementById("user-id").value;
}

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(jhtaLat, jhtaLon), // 기본 위치값: 중앙hta 주소 표시
      level: 2 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

var locPosition;
var lat;
var lon;

// HTML5의 geolocation으로 사용할 수 있는지 확인 -> 만약 사용할 수 있다면 현재 위치와 메세지 변수를 저장해서 displayMarker 함수 실행.
if (navigator.geolocation) {
  // GeoLocation을 이용해서 접속 위치를 얻어옵니다
  navigator.geolocation.getCurrentPosition(function (position) {
    lat = position.coords.latitude; // 위도
    lon = position.coords.longitude; // 경도
    var locPosition = new kakao.maps.LatLng(lat, lon); // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
    // 마커와 인포윈도우를 표시합니다
    displayMarker(locPosition);

        var geocoder = new kakao.maps.services.Geocoder();
        // 위치 정보 좌표를 얻은 후 행정 구역 반환하기
        var addressDisplay = document.getElementById('addressDisplay');
        var centerAddr = document.getElementById('centerAddr');
        var callback = function (result, status) {
            if (status === kakao.maps.services.Status.OK) {
                for (var i = 0; i < result.length; i++) {
                    console.log(result); // 값이 행정동 값과 법정동 값 두개 들어오는데, 그 중 h로 행정동의 값만 출력함.
                    if (result[i].region_type === 'H') { // 행정동일 경우
                        console.log(userId);
                        console.log("행정동 이름: " + result[i].address_name); // 전체 주소 출력
                        addressDisplay.innerHTML = "현재 위치는 '" + result[i].region_3depth_name + "' 입니다."
                        centerAddr.innerHTML = result[i].address_name;
                        address = result[i];
                        address.userId = userId;
                        addressData = JSON.stringify(address);

                        // 행정동 이름 출력
                        break; // 첫 번째 행정동 이름만 출력
                    }
                }
            }
        };
        geocoder.coord2RegionCode(lon, lat, callback);

  });

} else { // HTML5의 GeoLocation을 사용할 수 없을때 설정.
  locPosition = new kakao.maps.LatLng(jhtaLat, jhtaLon);
  displayMarker(locPosition);
}

// 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition) {
  var imageSrc = '/images/icons/currentlocation.svg', // 마커이미지의 주소
      imageSize = new kakao.maps.Size(34, 39), // 마커이미지의 크기입니다
      imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
      imageOption);
  // 마커를 생성합니다
  var marker = new kakao.maps.Marker({
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

if(document.getElementById("confirmButton")){
    document.getElementById("confirmButton").addEventListener("click",function(){
        userAddressUpdate(addressData);

    })
}

function userAddressUpdate(addressData) {
    fetch("/api/user/address-update", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body:addressData,
    })
        .then(response => {
            if (!response.ok) {
                alert("에러");
            }
            return response.json();
        })
        .then(result => {
            if(result){
                alert("업데이트 성공");
                debugger;
                window.location.href = "/user/my-page";
            } else {
                alert("업데이트 실패");
            }
        })
}
