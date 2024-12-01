// 수정할 때 사용되는 JS
document.addEventListener("DOMContentLoaded", function () {
  const postId = getPostIdFromUrl(); // URL에서 게시글 ID를 추출
  getPost(postId); // 게시글 정보 가져오기

  const updateButton = document.getElementById('submit-btn');
  updateButton.addEventListener('click', function (event) {
    event.preventDefault(); // 폼의 기본 제출 동작을 막음
    updatePost(postId); // 수정된 게시글 데이터를 업데이트
  });
});

function getPostIdFromUrl() {
  const pathParts = window.location.pathname.split('/');
  return pathParts[pathParts.length - 1]; // URL 경로에서 마지막 부분 (게시글 ID) 추출
}

function getPost(id) {
  fetch(`/api/fleamarket/posts/${id}`)
  .then(response => response.json())
  .then(data => {
    // 서버에서 받은 데이터를 폼에 채우기
    document.getElementById('post-title').value = data.title;
    document.getElementById('post-category').value = data.productCategoryName;
    document.getElementById('post-price').value = data.price;
    document.getElementById('post-content').value = data.content;

    // 추가적인 데이터가 있다면 여기에 추가
  })
  .catch(error => console.error("게시글 불러오기 실패", error));
}

function updatePost(id) {
  const editorContent = editor.getData(); // CKEditor에서 입력된 내용 가져오기
  const title = document.getElementById("post-title").value;
  const price = document.getElementById("post-price").value;
  const categoryId = document.getElementById("post-category").value;
  const content = editorContent;
  const isFree = document.getElementById("isFree").checked;
  const isPriceOffer = document.getElementById("isPriceOffer").checked;

  // 카테고리가 유효한지 체크
  if (!categoryId || isNaN(categoryId)) {
    alert("카테고리를 선택해주세요.");
    return;
  }

  const postData = {
    title,
    price: price ? parseInt(price) : 0,
    productCategoryId: parseInt(categoryId),
    content,
    isFree,
    isPriceOffer
  };

  fetch(`/api/fleamarket/posts/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(postData) // 수정된 데이터 전송
  })
  .then(response => {
    if (!response.ok) {
      throw new Error("글 수정에 실패했습니다.");
    }
    return response.json();
  })
  .then(data => {
    alert("글이 성공적으로 수정되었습니다.");
    window.location.href = "/fleamarket"; // 수정 후 목록 페이지로 이동
  })
  .catch(error => {
    alert(error.message);
    console.error("글 수정 중 오류가 발생했습니다:", error);
  });
}

document.getElementById("post-price")
.addEventListener("input", function (event) {
  const value = event.target.value;
  if (!/^\d*$/.test(value)) {
    event.target.value = value.replace(/\D/g, ''); // 숫자만 입력 가능하게 처리
  }
});

// 카테고리 목록을 가져와서 셀렉트 박스에 추가하는 부분
document.addEventListener("DOMContentLoaded", function () {
  fetch("/api/fleamarket/category")
  .then(response => response.json())
  .then(data => {
    const categorySelect = document.getElementById("post-category");

    data.forEach(category => {
      const option = document.createElement("option");
      option.value = category.id;
      option.textContent = category.name;
      categorySelect.appendChild(option);
    });
  })
  .catch(error => console.error("카테고리 데이터를 불러오지 못했습니다: ", error));
});