document.addEventListener("DOMContentLoaded", function () {
  setMannerTemperature();
  const id = getPostIdFromUrl();
  getPost(id);
  getImages(id);
});

function setMannerTemperature() {
  const temperatureElement = document.querySelector(".manner-temperature");
  const mannerBar = document.querySelector(".manner-bar");

  if (!temperatureElement || !mannerBar) {
    console.error("온도 요소를 찾을 수 없습니다!!!");
    return;
  }

  const randomTemperature = generateRandomTemperature();
  temperatureElement.textContent = `${randomTemperature}°C`;
  mannerBar.style.width = `${(randomTemperature / 99.9) * 100}%`;

  const color = getTemperatureColor(randomTemperature);
  temperatureElement.style.color = color;
  mannerBar.style.backgroundColor = color;
}

function generateRandomTemperature() {
  return (Math.random() * 99.9).toFixed(1);
}

function getTemperatureColor(temperature) {
  if (temperature >= 50.0 && temperature < 80.0) {
    return '#30C795';
  } else if (temperature > 80.0) {
    return '#FF6F10';
  }
}

function getPostIdFromUrl() {
  const pathParts = window.location.pathname.split('/');
  return pathParts[pathParts.length - 1];
}

function getPost(id) {
  if (!id) {
    console.error("게시글 ID를 URL에서 찾을 수 없습니다!!!");
    return;
  }

  fetch(`/api/fleamarket/posts/${id}`)
  .then(response => {
    if (!response.ok) {
      throw new Error('오류 발생!!!');
    }
    return response.json();
  })
  .then(data => renderPost(data))
  .catch(error => console.error('에러 발생!!!', error));
}

function renderPost(data) {
  if (!data) {
    console.error("게시글 데이터가 없습니다!!!");
    return;
  }
  getPosts2(data.userId);


  document.querySelector(
      ".flea-market-product-category").textContent = data.productCategoryName
      || '카테고리 없음';
  document.querySelector(".seller-info-nickname").textContent = data.nickname
      || '닉네임 없음';
  document.querySelector(".seller-image img").src = data.picUrl
      || '/default-image.png';
  document.querySelector(
      ".seller-info-address").textContent = `${data.region1DepthName
  || ''} ${data.region2DepthName || ''} ${data.region3DepthName || ''}`.trim();
  document.querySelector(".flea-market-post-title").textContent = data.title
      || '제목 없음';
  document.querySelector(
      ".flea-market-product-created-at").textContent = getRelativeTime(
      data.createdAt);
  document.querySelector(".flea-market-product-price").textContent = `${Number(
      data.price || 0).toLocaleString()}원`;
  const postContent = filterImagesFromContent(data.content);
  document.querySelector(".flea-market-product-text").innerHTML = postContent
      || '내용 없음';

  const loggedInUserId = document.querySelector("#logged-in-user").getAttribute(
      "data-user-id");
  console.log(loggedInUserId)
  if (loggedInUserId === String(data.userId)) {
    document.querySelector(".update-button").style.display = "block";
    document.querySelector(".delete-button").style.display = "block";
  } else {
    document.querySelector(".update-button").style.display = "none";
    document.querySelector(".delete-button").style.display = "none";
  }
}

function getRelativeTime(createdAt) {
  const createdDate = new Date(createdAt);
  const now = new Date();
  const diffInSeconds = Math.floor((now - createdDate) / 1000);

  if (diffInSeconds < 60) {
    return `${diffInSeconds}초 전`;
  }
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) {
    return `${diffInMinutes}분 전`;
  }
  const diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) {
    return `${diffInHours}시간 전`;
  }
  const diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 7) {
    return `${diffInDays}일 전`;
  }
  const diffInWeeks = Math.floor(diffInDays / 7);
  if (diffInWeeks < 4) {
    return `${diffInWeeks}주 전`;
  }
  const diffInMonths = Math.floor(diffInDays / 30);
  if (diffInMonths < 12) {
    return `${diffInMonths}개월 전`;
  }
  const diffInYears = Math.floor(diffInMonths / 12);
  return `${diffInYears}년 전`;
}

function filterImagesFromContent(content) {
  const div = document.createElement('div');
  div.innerHTML = content;

  const images = div.querySelectorAll('img');
  images.forEach(img => img.remove());

  return div.innerHTML;
}

let currentImageIndex = 0;
let images = [];

function getImages(id) {
  fetch(`/api/fleamarket/images/${id}`)
  .then(response => response.json())
  .then(data => {
    images = data.map(image => image.imageUrl);
    if (images.length > 0) {
      displayImage(currentImageIndex);
    } else {
      document.getElementById('flea-market-image').src = '/images/sample.png';
      console.log('이미지가 없습니다!!!');
    }
  })
  .catch(error => {
    console.error('이미지 로드 실패!!!', error);
    document.getElementById('flea-market-image').src = '/images/sample.png';
  });
}

function displayImage(index) {
  const imageElement = document.getElementById('flea-market-image');
  if (images.length > 0 && index >= 0 && index < images.length) {
    imageElement.src = images[index];
  } else {
    imageElement.src = '/images/sample.png';

    console.error('유효하지 않은 이미지 인덱스');
  }
}

function changeImage(direction) {
  if (images.length === 0) {
    return;
  }

  currentImageIndex += direction;

  if (currentImageIndex < 0) {
    currentImageIndex = images.length - 1;
  } else if (currentImageIndex >= images.length) {
    currentImageIndex = 0;
  }

  displayImage(currentImageIndex);
}

document.addEventListener("DOMContentLoaded", function () {
  setMannerTemperature();
  const id = getPostIdFromUrl();
  getPost(id);
  getImages(id);

  const updateButton = document.getElementById('update-post-btn');
  if (updateButton) {
    updateButton.addEventListener('click', function () {
      window.location.href = `/fleamarket/update/${id}`;
    });
  }

  const deleteButton = document.getElementById('delete-post-btn');
  if (deleteButton) {
    deleteButton.addEventListener('click', function () {
      if (confirm("게시글을 삭제하시겠습니까?")) {
        deletePost(id);
      }
    });
  }
});


function deletePost(id) {
  fetch(`/api/fleamarket/posts/${id}`, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok) {
      alert("게시글이 삭제되었습니다.");
      window.location.href = '/fleamarket';
    } else {
      alert("삭제에 실패했습니다.");
    }
  })
  .catch(error => {
    console.error("삭제 오류 발생", error);
    alert("삭제 중 오류가 발생했습니다.");
  });
}


let currentPage = 0;
const pageSize = 3;

document.addEventListener("DOMContentLoaded", function () {
  getPosts();

  const loadMoreButton = document.getElementById("load-more");
  loadMoreButton.addEventListener("click", function () {
    currentPage++;
  });
});

function getPosts2(id) {

  fetch(`/api/fleamarket/other-product?page=${currentPage}&size=${pageSize}?userId=${id}`)
  .then(response => {
    if (!response.ok) {
      throw new Error('에러 발생!!!');
    }
    return response.json();
  })
  .then(data => {
    if (data.content.length > 0) {
      renderPosts2(data.content);
    } else {
      document.getElementById("load-more").style.display = 'none';
    }
  })
  .catch(error => console.error('에러 발생!!!', error));
}

function renderPosts2(data) {
  const tableBody = document.querySelector("#product-list");
  const template = document.getElementById('article-template');

  data.forEach(post => {
    const clone = document.importNode(template.content, true);
    clone.querySelector(".title").textContent = post.title;
    clone.querySelector(".price").textContent = `${Number(
        post.price).toLocaleString()}원`;
    clone.querySelector(
        ".address").textContent = `${post.region1DepthName} ${post.region2DepthName} ${post.region3DepthName}`;
    const postLink = clone.querySelector(".post-link");

    postLink.href = `/fleamarket/posts/${post.id}`;

    fetch(`/api/fleamarket/images/${post.id}`)
    .then(response => response.json())
    .then(images => {
      const imgElement = clone.querySelector(".article-image img");

      if (images.length > 0) {
        imgElement.src = images[0].imageUrl;
      } else {
        imgElement.src = '/images/sample.png';
      }

      tableBody.appendChild(clone);
    })
    .catch(error => {
      console.error('이미지 로드 실패!!!', error);
      const imgElement = clone.querySelector(".article-image img");
      imgElement.src = '/images/sample.png';
      tableBody.appendChild(clone);
    });
  });
}

document.addEventListener("DOMContentLoaded", function() {
  const chatBtn = document.querySelector('.chat-btn');

  if (chatBtn) {
    chatBtn.addEventListener('click', function() {
      alert('현중님 제발 채팅 만들어주세요');
    });
  }
});