// 전체 게시글 전용
let currentPage = 0;
let isLastPage = false;
const communityPostsSize = 10;
let isLoading = false; // 데이터를 불러오는 중인지 확인하는 플래그

// 서브 카테고리 전용
let subcategoryCurrentPage = 0;      // 서브 카테고리 전용 현재 페이지
let subcategoryIsLastPage = false;   // 서브 카테고리 전용 마지막 페이지 여부
let selectedSubcategoryId = null;    // 선택된 서브 카테고리 ID

const SCROLL_THRESHOLD = 600;  // 스크롤 트리거 임계값

document.addEventListener("DOMContentLoaded", function () {
  fetch("/api/community/sub-categories")
  .then(response => {
    if (!response.ok) {
      throw new Error("에러!!")
    }
    return response.json();
  })
  .then(data => {
    const categoryList = document.getElementById("community-category-list");
    console.log(data);
    data.categories.forEach(data => {
      const li = document.createElement('li');
      const link = document.createElement('a');
      link.href = "#"; // 기본 링크는 없애고
      link.innerText = data.name;
      link.dataset.id = data.id; // 서브 카테고리 ID를 데이터 속성으로 추가

      // 클릭 이벤트 추가
      link.addEventListener('click', function (event) {
        event.preventDefault(); // 기본 링크 클릭 이벤트 방지
        const subcategoryId = this.dataset.id; // 클릭한 링크의 서브 카테고리 ID 가져오기
        selectedSubcategoryId = subcategoryId;
        subcategoryCurrentPage = 0;            // 서브 카테고리 전용 페이지 초기화
        subcategoryIsLastPage = false;         // 마지막 페이지 상태 초기화
        fetchPostsBySubCategory(subcategoryId); // 게시글 가져오기
      });
      li.appendChild(link);
      categoryList.appendChild(li);
    });
  })
  .catch(error => console.error('에러가 있어요', error));
})

// 게시글을 서브 카테고리 ID에 따라 가져오는 함수
async function fetchPostsBySubCategory(subcategoryId) {
  if (subcategoryIsLastPage || isLoading) {
    return;
  } // 마지막 페이지거나 로딩 중이면 중복 호출 방지
  isLoading = true;
  const postListContainer = document.getElementById('community-post-list');
  if (subcategoryCurrentPage === 0) {
    postListContainer.innerHTML = ''; // 새로운 서브 카테고리일 경우 기존 게시글 초기화
  }

  try {
    const response = await fetch(
        `/api/community/posts/subCategories/${subcategoryId}?page=${subcategoryCurrentPage}&size=${communityPostsSize}`); // 초기 페이지 요청
    const data = await response.json();
    console.log(data);
    // 게시글 데이터 처리
    data.content.forEach(post => {
      const postItem = document.createElement('a');
      postItem.href = `/community/posts/${post.communityPostId}`;
      postItem.classList.add('community-post-item');
      postItem.innerHTML = createPostHTML(post);
      postListContainer.appendChild(postItem);
    });
    // 페이지 상태 업데이트
    subcategoryIsLastPage = data.last;
    subcategoryCurrentPage++;
    if (subcategoryIsLastPage) {
      // 마지막 페이지임을 알리는 메시지가 이미 없다면 생성
      if (!document.getElementById('subcategory-no-more-posts')) {
        const noMorePostsMessage = document.createElement('div');
        noMorePostsMessage.id = 'subcategory-no-more-posts';
        noMorePostsMessage.className = 'no-more-posts-message';
        const categoryName = data.content.length > 0
            ? data.content[0].categoryName : '이 카테고리';
        console.log(data.content);
        noMorePostsMessage.innerHTML = `
                    <p>
                        <strong>${categoryName}</strong> 게시글을 모두 확인하셨어요! 다른 카테고리도 둘러보세요 🥕
                    </p>
                `;
        postListContainer.appendChild(noMorePostsMessage);
      }
    }
  } catch (error) {
    console.error('서브 카테고리 게시글을 불러오는 중 오류가 발생했습니다:', error);
  } finally {
    isLoading = false;
  }
}

// 게시글 HTML을 생성하는 함수
function createPostHTML(post) {
  const tempDiv = document.createElement('div');
  tempDiv.innerHTML = post.content;
  const plainText = tempDiv.textContent || tempDiv.innerText || '';
  const previewText = plainText.length > 30 ? plainText.substring(0, 30) + "..."
      : plainText;

  const postHTML = `
        <div class="community-post-body">
            <div class="community-post-content" >
                <div class="community-post-category">${post.categoryName}</div>
                <div class="community-post-title">${post.title}</div>
                <div class="community-post-content">${previewText}</div>
            </div>
            <div class="community-post-thumbnail">
                ${post.imageUrl ? `<img src="${post.imageUrl}" alt="썸네일 이미지"/>`
      : ''}
            </div>
        </div>
        <div class="community-post-footer">
            <div class="community-post-info">
                ${post.region3DepthName} · ${post.formattedCreatedAt}  
            </div>
            <div class="community-post-stats">
                 <div class="community-post-like" data-post-id="${post.communityPostId}">
                 </div>
                <div class="community-post-comment">
                    <span>
                        <img src="/images/community/comment.svg" alt="댓글 아이콘">
                        <span class="community-post-comment-count">${post.commentCount}</span>
                    </span>
                </div>
            </div>
        </div>
    `;
  return postHTML;
}

// 게시글 로드 후 좋아요 개수 표시 업데이트
async function updateLikeCountsForPosts(posts) {
  posts.forEach(post => {
    const likeCountElement = document.querySelector(
        `.community-post-like[data-post-id="${post.communityPostId}"]`);
    if (likeCountElement) {
      fetchLikeCount(post.communityPostId, likeCountElement);
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  fetchPosts();
  // 스크롤 이벤트 리스너 추가
  window.addEventListener("scroll", throttle(function () {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight
        - SCROLL_THRESHOLD) {
      if (selectedSubcategoryId) {
        // 서브 카테고리가 선택된 상태에서 마지막 페이지가 아닐 경우에만 요청
        if (!subcategoryIsLastPage && !isLoading) {
          fetchPostsBySubCategory(selectedSubcategoryId);
        }
      } else {
        // 서브 카테고리가 선택되지 않은 경우 전체 게시글 로딩
        if (!isLastPage && !isLoading) {
          fetchPosts();
        }
      }
    }
  }, 300)); // throttle: 300ms 동안 한 번만 실행되도록 제한
});

async function fetchPosts() {
  isLoading = true;  // 로딩 중 상태로 변경
  const postListContainer = document.getElementById('community-post-list');
  // 마지막 페이지면 더 이상 요청하지 않음
  console.log(isLastPage);

  try {
    // 서버로부터 데이터를 가져오는 부분
    const response = await fetch(
        `/api/community/posts?page=${currentPage}&size=${communityPostsSize}`);
    // JSON 형식으로 변환
    const data = await response.json();
    console.log(data);
    // 게시글 리스트와 마지막 페이지 여부
    const posts = data.content;
    isLastPage = data.last;
    // 게시글 HTML 요소 생성 및 추가
    posts.forEach(post => {
      const postItem = document.createElement('a');
      postItem.href = `/community/posts/${post.communityPostId}`;
      postItem.classList.add('community-post-item');
      postItem.innerHTML = createPostHTML(post);
      postListContainer.appendChild(postItem);
    });
    // 현재 페이지 증가
    currentPage++;
    // 각 게시글의 좋아요 개수 업데이트
    updateLikeCountsForPosts(posts);
    console.log(updateLikeCountsForPosts(posts));
    if (isLastPage) {
      if (!document.getElementById('no-more-posts')) {
        const noMorePostsMessage = document.createElement('div');
        noMorePostsMessage.id = 'no-more-posts';
        noMorePostsMessage.className = 'no-more-posts-message';
        noMorePostsMessage.innerHTML = `
                <p>
                    더 이상 게시글이 없어요! 다른 게시글을 찾아보세요. 🥕
                </p>
            `;
        postListContainer.appendChild(noMorePostsMessage);
      }

    }
  } catch (error) {
    // 에러 발생 시 처리
    console.error('게시글을 불러오는 중 오류가 발생했습니다:', error);
  } finally {
    // 데이터 로딩이 완료되었으므로 로딩 상태 해제
    isLoading = false;
  }
}

// 좋아요 개수를 불러와 표시하는 함수
async function fetchLikeCount(postId, element) {
  try {
    const response = await fetch(`/api/community/posts/${postId}/likes`);
    if (response.ok) {
      const likeCount = await response.json();
      if (likeCount > 0) {
        element.innerHTML = `
                        <span>
                        <img src="/images/community/like.svg" alt="좋아요 아이콘">
                        <span class="community-post-like-count" >${likeCount}</span>
                        </span>
                    `
      }
    } else {
      console.error(`Error fetching like count for post ID: ${postId}`);
    }
  } catch (error) {
    console.error('좋아요 개수를 불러오는 중 오류가 발생했습니다:', error);
  }
}

// throttle 함수 정의 (시간 간격 동안 여러 번 호출 방지)
function throttle(func, limit) {
  let lastFunc;
  let lastRan;
  return function () {
    const context = this;
    const args = arguments;
    if (!lastRan) {
      func.apply(context, args);
      lastRan = Date.now();
    } else {
      clearTimeout(lastFunc);
      lastFunc = setTimeout(function () {
        if (Date.now() - lastRan >= limit) {
          func.apply(context, args);
          lastRan = Date.now();
        }
      }, limit - (Date.now() - lastRan));
    }
  };
}


