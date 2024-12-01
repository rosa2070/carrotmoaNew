let geoCoder; // 지오코더 변수 선언
// let originalData = {}; // 원본 데이터 저장

// 지도 초기화 함수
function initMap() {
  geoCoder = new kakao.maps.services.Geocoder(); // 카카오 지오코더 인스턴스 생성
}

// 주소로부터 좌표를 가져오는 함수
function getAddressCoords(address) {
  return new Promise((resolve, reject) => {
    if (geoCoder) {
      geoCoder.addressSearch(address, (result, status) => {
        if (status === kakao.maps.services.Status.OK && result.length > 0) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          resolve(coords);
        } else {
          reject('주소 검색 실패: ' + status);
        }
      });
    } else {
      reject('geoCoder가 정의되지 않았습니다.');
    }
  });
}

// 주소 찾기 기능을 수행하는 함수
function sample4_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      document.getElementById('roadAddress').value = data.roadAddress; // 도로명 주소 입력
      document.getElementById('lotAddress').value = data.jibunAddress; // 지번 주소 입력

      const mainAddress = data.roadAddress || data.jibunAddress;
      getAddressCoords(mainAddress)
      .then(coords => {
        document.getElementById('latitude').value = coords.getLat();
        document.getElementById('longitude').value = coords.getLng();
      })
      .catch(error => {
        console.error('좌표를 가져오는 중 오류 발생:', error);
        alert('좌표를 가져오는 데 문제가 발생했습니다. 다시 시도해주세요.');
      });
    }
  }).open(); // 주소 검색 창 열기
}

function showImagePreviews(imageUrls) {
  const imageContainer = document.getElementById('div_added_pictures');
  imageContainer.innerHTML = ''; // 기존 미리보기 이미지 제거

  imageUrls.forEach(imageUrl => {
    const imageItem = document.createElement('div');
    imageItem.className = 'image_item';
    imageItem.innerHTML = `<img class="room_image" src="${imageUrl}" alt="미리보기 이미지">`;
    imageContainer.appendChild(imageItem);
  });
}

document.getElementById('images').addEventListener('change', function (event) {
  const files = Array.from(event.target.files);
  const imageContainer = document.getElementById('div_added_pictures');
  imageContainer.innerHTML = '';

  // 순서를 보장하기 위해 Promise.all 사용
  const imagePromises = files.map((file, index) => {
    return new Promise((resolve) => {
      const reader = new FileReader();
      reader.onload = function (e) {
        const imageItem = document.createElement('div');
        imageItem.className = 'image_item';
        imageItem.innerHTML = `<img class="room_image" src="${e.target.result}" alt="미리보기 이미지">`;
        resolve({index, element: imageItem});
      };
      reader.readAsDataURL(file);
    });
  });

  // 모든 이미지가 로드되면, index 순서대로 정렬하여 추가
  Promise.all(imagePromises).then(imageResults => {
    imageResults.sort((a, b) => a.index - b.index);
    imageResults.forEach(result => {
      imageContainer.appendChild(result.element);
    });
  });
});

let originalData = {};
document.addEventListener("DOMContentLoaded", function () {
  initMap();
  document.getElementById('btn_search_addr').addEventListener('click',
      sample4_execDaumPostcode);

  const accommodationId = window.location.pathname.split('/').pop();
  fetch(`/api/host/room/${accommodationId}`)
  .then(response => {
    if (!response.ok) {
      console.error('응답 상태:', response.status);
      throw new Error("업데이트 실패: " + response.statusText);
    }
    return response.json();
  })
  .then(data => {
    originalData = data;
    populateFormFields(data);
    showImagePreviews(data.imageUrls);
    checkAmenities(data.amenityIds);
  })
  .catch(error => {
    console.error('숙소 정보를 가져오는 중 오류 발생: ', error);
  });

  document.getElementById('updateForm').addEventListener('submit',
      createHandleSubmit(accommodationId));
});

function populateFormFields(data) {
  document.getElementById('title').value = data.title;
  document.getElementById('roadAddress').value = data.roadAddress;
  document.getElementById('lotAddress').value = data.lotAddress;
  document.getElementById('detailAddress').value = data.detailAddress;
  document.getElementById('floor').value = data.floor;
  document.getElementById('totalFloor').value = data.totalFloor;
  document.getElementById('totalArea').value = data.totalArea;
  document.getElementById('room_cnt').value = data.spaceCounts[0];
  document.getElementById('bathroom_cnt').value = data.spaceCounts[1];
  document.getElementById('livingRoom_cnt').value = data.spaceCounts[2];
  document.getElementById('kitchen_cnt').value = data.spaceCounts[3];
  document.getElementById('price').value = data.price;
  document.getElementById('content').value = data.content;
  document.getElementById('transportationInfo').value = data.transportationInfo;
}

function checkAmenities(amenityIds) {
  amenityIds.forEach(amenityId => {
    const checkbox = document.getElementById(`opt_basic_${amenityId}`);
    if (checkbox) {
      checkbox.checked = true; // 체크 상태로 설정
    }
  });
}

function createHandleSubmit(accommodationId) {
  return function (event) {
    event.preventDefault();
    const updatedData = new FormData(); // 다 빈상태로 만들어줌
    const fields = ['title', 'roadAddress', 'lotAddress', 'detailAddress',
      'floor', 'totalFloor', 'totalArea', 'price', 'content',
      'transportationInfo', 'latitude', 'longitude'];

    fields.forEach(field => {
      const value = document.getElementById(field).value;
      if (value) {
        updatedData.append(field, value);
        console.log(`변경된 필드: ${field} => ${value}`); // 변경된 필드 로깅
      }
    });

    const imageInput = document.getElementById('images');
    const files = imageInput.files;
    for (let i = 0; i < files.length; i++) {
      updatedData.append('images', files[i]); // MultipartFile로 추가
    }

    const existingImageUrls = originalData.imageUrls || [];
    console.log('기존 이미지 URL:', existingImageUrls); // 이미지 URL 출력
    existingImageUrls.forEach(url => {
      updatedData.append('existingImageUrls', url); // 각 URL을 개별 항목으로 추가
    });

    const spaceIds = [1, 2, 3, 4];
    const spaceCounts = [
      parseInt(document.getElementById('room_cnt').value),
      parseInt(document.getElementById('bathroom_cnt').value),
      parseInt(document.getElementById('livingRoom_cnt').value),
      parseInt(document.getElementById('kitchen_cnt').value)
    ];

    spaceIds.forEach((spaceId, index) => {
      updatedData.append(`accommodationSpaces[${index}].spaceId`, spaceId);
      updatedData.append(`accommodationSpaces[${index}].count`,
          spaceCounts[index]);
    });

    const currentAmenities =
        Array.from(
            document.querySelectorAll('input[type="checkbox"]:checked')).map(
            checkbox => Number(checkbox.value));
    currentAmenities.forEach(amenityId => {
      updatedData.append('amenityIds', amenityId);
    });

    fetch(`/api/host/room/edit/${accommodationId}`, {
      method: 'PATCH',
      body: updatedData
    })
    .then(response => {
      alert('숙소 정보가 수정되었습니다.');
      window.location.href = `/host/room/manage`;
    })
    .catch(error => {
      console.error('수정 중 오류 발생:', error);
    });
  };
}

