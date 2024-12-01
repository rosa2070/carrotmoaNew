let uploadFilesCount = 0;
const maxFiles = 3;

class UploadAdapter {
  constructor(loader) {
    this.loader = loader;
  }

  upload() {
    // 업로드된 파일 수가 최대 수를 초과하면 reject
    // TODO: 이미지 3개 초과 시, 에러메시지 알림창이 나오지만 매끄럽지 못함. 추후 개선할 방법 고민하기.
    return this.loader.file.then(file => new Promise(((resolve, reject) => {
      this._initRequest();
      this._initListeners(resolve, reject, file);
      this._sendRequest(file);
    })));
  }

  delete() {

  }

  _initRequest() {
    const xhr = this.xhr = new XMLHttpRequest();
    xhr.open('POST', '/images/upload', true);
    xhr.responseType = 'json';
  }

  _initListeners(resolve, reject, file) {
    const xhr = this.xhr;
    const loader = this.loader;
    const genericErrorText = '파일을 업로드 할 수 없습니다.'

    xhr.addEventListener('error', () => {
      reject(genericErrorText)
    })
    xhr.addEventListener('abort', () => reject())
    xhr.addEventListener('load', () => {
      const response = xhr.response
      if (!response || response.error) {
        return reject(response && response.error ? response.error.message
            : genericErrorText);
      }
      uploadFilesCount++;
      console.log(`업로드되는 파일 수 실시간 체크 : ${uploadFilesCount}`);
      resolve({
        default: response.url
      })
    })
  }

  _sendRequest(file) {
    const data = new FormData()
    data.append('upload', file)
    this.xhr.send(data)
  }
}

// 이미지 목록을 관리하는 배열

// 이미지 업로드 및 삭제를 처리하는 함수
function handleImageUpload(adapter, file) {
  adapter.upload().then(response => {
    uploadedImages.push(response.default);
    renderUploadedImages();
  }).catch(error => {
    alert(error);
  });
}

// 업로드된 이미지를 렌더링하는 함수
function renderUploadedImages() {
  const imageContainer = document.getElementById('imageContainer');
  imageContainer.innerHTML = ''; // 기존 이미지 클리어

  uploadedImages.forEach((url, index) => {
    const imgElement = document.createElement('img');
    imgElement.src = url;
    imgElement.alt = 'Uploaded Image';
    imgElement.style.width = '100px'; // 이미지 크기 조정
    imgElement.style.margin = '5px';

    const deleteButton = document.createElement('button');
    deleteButton.innerText = '삭제';
    // 삭제 버튼 클릭 시 handleImageDelete 호출
    deleteButton.onclick = () => handleImageDelete(uploadAdapter, index);

    const div = document.createElement('div');
    div.appendChild(imgElement);
    div.appendChild(deleteButton);

    imageContainer.appendChild(div);
  });
}

// Adapter 인스턴스 생성
const uploadAdapter = new UploadAdapter(/* loader 인스턴스 */);

// 예시로 파일 업로드 버튼 클릭 시
document.getElementById('uploadButton').onclick = () => {
  const fileInput = document.getElementById('fileInput');
  const file = fileInput.files[0]; // 선택된 파일
  handleImageUpload(uploadAdapter, file);
};