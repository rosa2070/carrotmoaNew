let currentPage = 0;
const pageSize = 9;

document.addEventListener("DOMContentLoaded", function () {
  const writePostButton = document.getElementById("write-post-btn");
  writePostButton?.addEventListener("click", function () {
    window.location.href = "/fleamarket/write";
  });

  getPosts();

  const loadMoreButton = document.getElementById("load-more");
  loadMoreButton.addEventListener("click", function () {
    currentPage++;
    getPosts();
  });
});

function getPosts() {
  fetch(`/api/fleamarket/list?page=${currentPage}&size=${pageSize}`)
  .then(response => {
    if (!response.ok) {
      throw new Error('에러 발생!!!');
    }
    return response.json();
  })
  .then(data => {
    if (data.content.length > 0) {
      renderPosts(data.content);
    } else {
      document.getElementById("load-more").style.display = 'none';
    }
  })
  .catch(error => console.error('에러 발생!!!', error));
}

function renderPosts(data) {
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

