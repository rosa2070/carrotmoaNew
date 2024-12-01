let geoCoder;

function initMap() {
  geoCoder = new kakao.maps.services.Geocoder();
}

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

function sample4_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      document.getElementById('roadAddress').value = data.roadAddress;
      document.getElementById('lotAddress').value = data.jibunAddress;

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
  }).open();
}

function validateField(fieldId, message, isNumber = false) {
  const value = document.getElementById(fieldId).value.trim();
  if (!value) {
    alert(message);
    document.getElementById(fieldId).focus(); // 안 채워진 필드 포커스
    return false;
  }

  // 숫자만 입력해야 하는 경우 추가 검증
  if (isNumber && (isNaN(value)) || value <= 0) {
    alert('입력 값은 숫자여야 하며, 0보다 커야 합니다.');
    return false;
  }
  return true;
}

function validateForm() {
  // 각 필드 유효성 검사
  if (!validateField('title', '숙소 이름은 필수 입력입니다.')) {
    return false;
  }
  if (!validateField('roadAddress', '도로명 주소는 필수 입력입니다.')) {
    return false;
  }
  if (!validateField('lotAddress', '지번 주소는 필수 입력입니다.')) {
    return false;
  }
  if (!validateField('detailAddress', '상세 주소는 필수 입력입니다.')) {
    return false;
  }
  if (!validateField('floor', '층 수는 필수 입력입니다.', true)) {
    return false;
  } // 숫자 검증 추가
  if (!validateField('totalFloor', '총 층 수는 필수 입력입니다.', true)) {
    return false;
  } // 숫자 검증 추가
  if (!validateField('totalArea', '총 면적은 필수 입력입니다.', true)) {
    return false;
  } // 숫자 검증 추가
  if (!validateField('price', '가격은 필수 입력입니다.', true)) {
    return false;
  } // 숫자 검증 추가
  if (!validateField('content', '상세 설명은 필수 입력입니다.')) {
    return false;
  }
  if (!validateField('transportationInfo', '교통 정보는 필수 입력입니다.')) {
    return false;
  }

  // 이미지 파일 개수 검사
  const imageInput = document.getElementById('images');
  const files = imageInput.files;
  if (files.length < 4) {
    alert('방 사진은 최소 4장 이상 등록해야 합니다.');
    return false;
  }

  // 체크박스 중 하나 이상 선택 검사
  const checkboxes = document.querySelectorAll('input[type="checkbox"]');
  const isChecked = Array.from(checkboxes).some(checkbox => checkbox.checked);
  if (!isChecked) {
    alert('기본 옵션을 하나 이상 선택해야 합니다.');
    return false;
  }

  return true; // 모든 검사를 통과하면 true 반환
}

function handleFormSubmission(event) {
  if (!validateForm()) {
    return;
  } // 유효성 검사 실패 시 함수 종료

  const confirmSubmission = confirm('입력하신 내용으로 등록하시겠습니까?');
  if (!confirmSubmission) {
    return; // 사용자가 취소한 경우 함수 종료
  }

  const formData = new FormData(event.target);
  fetch('/api/host/room/register', {
    method: 'POST',
    body: formData
  })
  .then(response => {
    if (!response.ok) {
      // 서버에서 반환된 오류 메시지를 확인
      return response.json().then(err => {
        // 오류 메시지 처리
        const errorMessages = Array.isArray(err) ? err : [err.message
        || "알 수 없는 오류"];
        alert('유효성 검사 오류:\n' + errorMessages.join('\n'));
        throw new Error('유효성 검사 실패');
      });
    }
    return response.json();
  })
  .then(data => {
    console.log('숙소 등록 성공:', data);
    window.location.href = '/host/room/manage';
  })
  .catch(error => {
    console.error('서버 오류:', error);
    alert('서버와의 통신 중 오류가 발생했습니다. 다시 시도해주세요.');
  });
}

document.addEventListener('DOMContentLoaded', () => {
  // Kakao SDK가 로드된 후 initMap 호출
  if (typeof kakao !== 'undefined' && kakao.maps) {
    initMap();
  } else {
    console.error('Kakao Maps SDK가 로드되지 않았습니다.');
  }
  document.getElementById('btn_search_addr').addEventListener('click',
      sample4_execDaumPostcode);

  const roadAddressInput = document.getElementById('roadAddress');
  const lotAddressInput = document.getElementById('lotAddress');

  roadAddressInput.addEventListener('click', () => {
    alert('주소 찾기를 하시면 자동으로 주소가 입력됩니다.');
  });

  lotAddressInput.addEventListener('click', () => {
    alert('주소 찾기를 하시면 자동으로 주소가 입력됩니다.');
  });

  const registerForm = document.getElementById('registerForm');
  registerForm.addEventListener('submit', function (event) {
    event.preventDefault(); // 기본 제출 동작 방지
    handleFormSubmission(event, this);
  });

  document.getElementById('images').addEventListener('change',
      function (event) {
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

});
