const communityPostId = window.location.pathname.split('/').pop();

const currentUserId = (user && user.userProfile) ? user.userProfile.userId : null;
let postUserId;

document.addEventListener("DOMContentLoaded", function () {
    const commentInput = document.getElementById("commentInput");
    if (user) {
        commentInput.placeholder = "댓글을 남겨보세요.";
    } else {
        commentInput.placeholder = "로그인 후 이용 가능합니다.";
    }

    fetch(`/api/community/posts/${communityPostId}/likes/${currentUserId}`)
        .then(response => response.json())
        .then(data => {
            let isLiked = !data;
            console.log("로그인한 유저가 게시글에 좋아요 눌렀던적 있어? -> {}", isLiked);
            if (isLiked) {
                // 좋아요를 한 경우
                likeIcon.src = '/images/community/like.svg'; // 눌린 상태
                fetch(`/api/community/posts/${communityPostId}/likes`)
                    .then(response => response.json())
                    .then(count => {
                        likeCount.textContent = count; // 좋아요 개수 업데이트
                    });
            } else {
                // 좋아요를 누르지 않은 경우
                likeIcon.src = '/images/community/unlike.svg'; // 눌리지 않은 상태
                likeCount.textContent = "공감하기"; // 초기 상태 표시
            }
        })
        .catch(error => console.error("좋아요 상태 조회 오류:", error));


    const nickname = document.getElementById("nickname");
    const userProfileImage = document.getElementById("userProfileImage");
    const region2DepthName = document.getElementById("region2DepthName");
    const region3DepthName = document.getElementById("region3DepthName");
    const title = document.getElementById("title");
    const communityCategory = document.getElementById("communityCategory");
    const content = document.getElementById("content");
    const createdAt = document.getElementById("createdAt");

    fetch(`/api/community/posts/${communityPostId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("해당 게시글을 불러올 수 없습니다.");
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            postUserId = data.userId;
            nickname.innerText = data.nickname;
            userProfileImage.src = data.picUrl;
            region2DepthName.innerText = data.region2DepthName;
            region3DepthName.innerText = data.region3DepthName;
            title.innerHTML = data.title;
            content.innerHTML = data.content;
            createdAt.innerHTML = data.formattedCreatedAt;
            communityCategory.innerHTML = data.communityCategoryName;
            communityCategory.href = `/community/subCategories/${data.communityCategoryId}`;
        })
        .catch(error => console.log("에러 발생: ", error));
});

function createCommentHtml(data) {
    const isPostWriter = postUserId === data.userId;
    const isCommentWriter = currentUserId === data.userId;
    const childTag = data.parentId ? '<div class="child-comment"></div>' : '';
    return `
<div class="detail-comment-wrap-replytag" >
 ${childTag}
    <div class="detail-comment-wrap" data-comment-id="${data.id}" data-comment-depth="${data.depth}" data-comment-order="${data.orderInGroup}">
      <div class="detail-comment-header">
        <div class="detail-comment-profile">
          <img src="${data.picUrl}" alt="유저 프로필 사진" id="commentUserProfile">
        </div>
        <div class="comment-user-info">
          <div class="detail-comment-nickname">
            <a href="/" id="commentUserNickname">${data.nickname}</a>
            ${isPostWriter ? `<img src="/images/community/post-writer.svg" alt="게시글 작성자 댓글" id="postWriter" class="post-writer">` : ''}
          </div>
          
          <div class="detail-comment-region-name" id="commentUserRegion">
            <span>${data.region2DepthName}</span>
            <span>${data.region3DepthName}</span>
            <div class="detail-comment-time-wrap">
                <time class="detail-comment-time" id="commentCreatedAt"> · ${data.formattedCreatedAt}</time>
            </div>
          </div>
        </div>
        
        <button class="moreOptionsButton">
          <img src="/images/community/more-options.svg" alt="더보기 옵션">
        </button>
        <div class="overlay comment-overlay"></div>
        <div class="dropdown-content">
         ${!isCommentWriter ? `<button class="reportComment">댓글 신고</button>` : ''} 
         ${isCommentWriter ? `<button class="editComment">댓글 수정</button>` : ''} 
          ${isCommentWriter ? `<button class="deleteComment">댓글 삭제</button>` : ''} 
        </div>
      </div>
      <div class="detail-comment-content">
        <p id="commentContent">${data.content}</p>
      </div>
      <div class="reply-button-wrapper" id ="replyBtnWrapper">
        <button class="comment-like-button" id="commentLikeButton">
          <img src="/images/community/unlike.svg" alt="댓글 좋아요" id="commentLikeIcon"/>
          <span id="commentLikeCount">좋아요</span>
        </button>
        <button class="reply-button" data-comment-id="${data.id}">
          <img src="/images/community/detail-comment.svg" alt="답글" id="replyIcon"/>
          <span>답글쓰기</span>
        </button>
      </div>
      <section class="reply-input-section">
        <div class="reply-input">
          <form id="replyCommentForm">
                      <textarea placeholder class="reply-input-field" id="replyInput"
                                name="replyContent" placeholder = "답글을 입력하세요.."></textarea>
            <button class="reply-submit-button" id="submitReplyBtn">답글 등록</button>
          </form>
        </div>
      </section>
    </div>
</div>
  `;
}

document.addEventListener("DOMContentLoaded", function () {
    const commentsList = document.getElementById("commentsList");
    commentsList.addEventListener("click", function (event) {
        if (event.target && event.target.closest(".reply-button")) {
            const commentId = event.target.closest(".reply-button").getAttribute("data-comment-id");
            const replySection = event.target.closest(".detail-comment-wrap").querySelector(".reply-input-section");

            // display가 none이면 block으로, block이면 none으로 전환
            replySection.style.display = replySection.style.display === "none" || replySection.style.display === "" ? "block" : "none";
            alert(`클릭한 댓글의 ID: ${commentId}`);
            console.log(commentId);
        }
    });
    commentsList.addEventListener("submit", function (e) {
        e.preventDefault();
        if (e.target && e.target.id === "replyCommentForm") {
            const replyForm = e.target;
            const replyContent = replyForm.querySelector("#replyInput").value;
            const commentId = replyForm.closest(".detail-comment-wrap").querySelector(".reply-button").getAttribute("data-comment-id");
            // 답글 등록 API 호출
            fetch(`/api/community/posts/${communityPostId}/comments/${commentId}/replies`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    content: replyContent,
                    userId: currentUserId,
                })
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error("댓글 등록에 실패했습니다.");
                    }
                })
                .then(data => {
                    replyForm.querySelector("#replyInput").value = ""; // 입력 필드 초기화
                    // 댓글 리스트를 다시 불러옵니다.
                    return fetch(`/api/community/posts/${communityPostId}/comments`, {method: "GET"});
                })
                .then(response => {
                    if (!response.ok) throw new Error("댓글 리스트를 불러올 수 없습니다.");
                    return response.json();
                })
                .then(data => {
                    const commentCount = data.commentCount;
                    const comments = data.commentList;
                    document.getElementById("commentsList").innerHTML = "";

                    comments.forEach(comment => {
                        const commentHtml = renderCommentHtml(comment);
                        document.getElementById("commentsList").insertAdjacentHTML('beforeend', commentHtml);
                    });
                    updateCommentCount(commentCount);
                })
                .catch(error => console.error("답글 등록 오류:", error));
        } else {
            alert("답글 내용을 입력해주세요.");
        }
    });
});


function updateCommentCount(count) {
    document.getElementById("commentCount").innerText = `댓글 ${count}`;
}

document.addEventListener("DOMContentLoaded", function () {
    document.addEventListener("click", function (event) {
        const target = event.target;

        if (target.id === "moreOptionsButton" || target.closest("#moreOptionsButton")) {
            const dropdownMenu = document.getElementById("dropdownMenu");
            const overlay = document.getElementById("postOverlay");

            if (dropdownMenu.style.display === "block") {
                dropdownMenu.style.display = "none";
                overlay.style.display = "none";
            } else {
                dropdownMenu.style.display = "block";
                overlay.style.display = "block";
            }

            const reportButton = document.getElementById("reportPost");
            const editButton = document.getElementById("editPost");
            const deleteButton = document.getElementById("deletePost");

            if (currentUserId === postUserId) {
                editButton.style.display = "block";
                deleteButton.style.display = "block";
                reportButton.style.display = "none";
            } else {
                editButton.style.display = "none";
                deleteButton.style.display = "none";
                reportButton.style.display = "block";
            }
        } else if (target.classList.contains("moreOptionsButton") || target.closest(".moreOptionsButton")) {
            const dropdownMenuComment = target.closest(".detail-comment-header").querySelector(".dropdown-content");
            const overlayComment = target.closest(".detail-comment-header").querySelector(".comment-overlay");

            if (dropdownMenuComment.style.display === "block") {
                dropdownMenuComment.style.display = "none";
                overlayComment.style.display = "none";
            } else {
                dropdownMenuComment.style.display = "block";
                overlayComment.style.display = "block";
            }
        } else {
            const allDropdowns = document.querySelectorAll(".dropdown-content");
            const allOverlays = document.querySelectorAll(".overlay");

            allDropdowns.forEach(menu => menu.style.display = "none");
            allOverlays.forEach(overlay => overlay.style.display = "none");
        }
    });
});

document.getElementById("submitCommentBtn").addEventListener("click", function (e) {
    e.preventDefault();
    const content = document.getElementById("commentInput").value;
    // 댓글 입력값이 1자 이상인지 확인
    if (content.trim().length < 1) {
        alert("댓글은 1자 이상 입력해야 합니다."); // 경고 메시지
        return; // 함수 종료
    }
    const data = {
        content: content,
        userId: currentUserId,
        depth: 0,
        orderInGroup: 1
    };

    fetch(`/api/community/posts/${communityPostId}/comments`, {
        method: "post",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) throw new Error("댓글 등록 실패");
            return response.json();
        })
        .then(data => {
            document.getElementById("commentInput").value = "";
            return fetch(`/api/community/posts/${communityPostId}/comments`, {method: "get"});
        })
        .then(response => {
            if (!response.ok) throw new Error("댓글 리스트를 불러올 수 없습니다.");
            return response.json();
        })
        .then(data => {
            const commentCount = data.commentCount;
            const comments = data.commentList;
            document.getElementById("commentsList").innerHTML = "";
            comments.forEach(comment => {
                const commentHtml = renderCommentHtml(comment);
                document.getElementById("commentsList").insertAdjacentHTML('beforeend', commentHtml);
            });
            updateCommentCount(commentCount);
        })
        .catch(error => console.error("에러 발생:", error));
});

document.addEventListener("DOMContentLoaded", function () {
    fetch(`/api/community/posts/${communityPostId}/comments`, {method: "get"})
        .then(response => {
            if (!response.ok) throw new Error("댓글 리스트를 불러올 수 없습니다.");
            return response.json();
        })
        .then(data => {
            const commentCount = data.commentCount;
            const comments = data.commentList;
            comments.forEach(comment => {
                const commentHtml = renderCommentHtml(comment);
                document.getElementById("commentsList").insertAdjacentHTML('beforeend', commentHtml);
            });
            updateCommentCount(commentCount);
        })
        .catch(error => console.log("에러 발생: ", error));
});

document.getElementById("deletePost").addEventListener("click", function () {
    document.getElementById("deleteModalOverlay").style.display = "flex";
});

document.getElementById("cancelDeleteButton").addEventListener("click", function () {
    document.getElementById("deleteModalOverlay").style.display = "none";
});

document.getElementById("deleteModalOverlay").addEventListener("click", function (event) {
    if (event.target === this) {
        document.getElementById("deleteModalOverlay").style.display = "none";
    }
});

document.getElementById("confirmDeleteButton").addEventListener("click", function () {
    fetch(`/api/community/posts/${communityPostId}`, {method: "delete"})
        .then(response => {
            if (!response.ok) throw new Error("유효하지 않은 요청입니다.");
            window.location.href = "/community";
        })
        .catch(error => console.log("에러 발생: ", error));
});

document.addEventListener("DOMContentLoaded", function () {
    const commentsList = document.getElementById("commentsList");
    commentsList.addEventListener("click", function (event) {
        const target = event.target;
        // 삭제 버튼 클릭 이벤트 확인
        if (target && target.classList.contains("deleteComment")) {
            const commentWrap = target.closest(".detail-comment-wrap");
            const commentId = target.closest(".detail-comment-wrap").getAttribute("data-comment-id");
            fetch(`/api/community/posts/${communityPostId}/comments/${commentId}`, {method: "DELETE"})
                .then(response => {
                    if (!response.ok) throw new Error("삭제할 댓글이 존재하지 않습니다.");
                    return response.json();
                })
                .then(data => {
                    console.log(data);
                    return fetch(`/api/community/posts/${communityPostId}/comments`, {method: "GET"});
                })
                .then(response => {
                    if (!response.ok) throw new Error("댓글 리스트를 불러올 수 없습니다.");
                    return response.json();
                })
                .then(data => {
                    const commentCount = data.commentCount;
                    const comments = data.commentList;
                    // 댓글 리스트를 새로 고침합니다.
                    commentsList.innerHTML = "";
                    comments.forEach(comment => {
                        const commentHtml = renderCommentHtml(comment);
                        commentsList.insertAdjacentHTML('beforeend', commentHtml);
                    });
                    updateCommentCount(commentCount);
                })
                .catch(error => console.error("에러 발생:", error));
        }
    });
});


document.getElementById("editPost").addEventListener("click", function () {
    window.location.href = `/community/write/${communityPostId}`;

})

function renderCommentHtml(data) {
    const isPostWriter = postUserId === data.userId;
    const isCommentWriter = currentUserId === data.userId;
    const childTag = data.depth > 0 ? '<div class="child-comment"></div>' : '';

    // 기본 댓글 HTML 템플릿
    let commentHtml = `
    <div class="detail-comment-wrap-replytag" >
        ${childTag}
        <div class="detail-comment-wrap" data-comment-id="${data.id}" data-comment-depth="${data.depth}" data-comment-order="${data.orderInGroup}">
            <div class="detail-comment-header">
                <div class="detail-comment-profile">
                    <img src="${data.picUrl}" alt="유저 프로필 사진" id="commentUserProfile">
                </div>
                <div class="comment-user-info">
                    <div class="detail-comment-nickname">
                        <a href="/" id="commentUserNickname">${data.nickname}</a>
                        ${isPostWriter ? `<img src="/images/community/post-writer.svg" alt="게시글 작성자 댓글" id="postWriter" class="post-writer">` : ''}
                    </div>
                    <div class="detail-comment-region-name" id="commentUserRegion">
                        <span>${data.region2DepthName} </span>
                        <span>${data.region3DepthName}</span>
                        <div class="detail-comment-time-wrap">
                            <time class="detail-comment-time" id="commentCreatedAt"> · ${data.formattedCreatedAt}</time>
                        </div>
                    </div>
                </div>
                <button class="moreOptionsButton">
                    <img src="/images/community/more-options.svg" alt="더보기 옵션">
                </button>
                <div class="overlay comment-overlay"></div>
                <div class="dropdown-content">
                    ${!isCommentWriter ? `<button class="reportComment">댓글 신고</button>` : ''} 
                    ${isCommentWriter ? `<button class="editComment">댓글 수정</button>` : ''} 
                    ${isCommentWriter ? `<button class="deleteComment">댓글 삭제</button>` : ''} 
                </div>
            </div>
            <div class="detail-comment-content">
                <p id="commentContent">${data.content}</p>
            </div>
            <div class="reply-button-wrapper" id="replyBtnWrapper">
                <button class="comment-like-button" id="commentLikeButton">
                    <img src="/images/community/unlike.svg" alt="댓글 좋아요" id="commentLikeIcon"/>
                    <span id="commentLikeCount">좋아요</span>
                </button>
                <button class="reply-button" data-comment-id="${data.id}">
                    <img src="/images/community/detail-comment.svg" alt="답글" id="replyIcon"/>
                    <span>답글쓰기</span>
                </button>
            </div>
            <section class="reply-input-section" style="display: none;">
                <div class="reply-input">
                    <form id="replyCommentForm">
                        <textarea class="reply-input-field" id="replyInput" name="replyContent" placeholder="답글을 입력하세요.."></textarea>
                        <button class="reply-submit-button" id="submitReplyBtn">답글 등록</button>
                    </form>
                </div>
            </section>
        </div>
    </div>`;

    // 답글이 있을 경우, 재귀적으로 답글들을 추가
    if (data.replies && data.replies.length > 0) {
        data.replies.forEach(reply => {
            commentHtml += renderCommentHtml(reply); // 중첩된 댓글에 대한 HTML을 추가
        });
    }

    return commentHtml;
}

document.getElementById('likeButton').addEventListener('click', function () {
    const likeIcon = document.getElementById('likeIcon');
    const likeCount = document.getElementById('likeCount');
    // 게시글 좋아요 불러오기.

    // currentUserId가 null인 경우 API 호출을 막고 경고 메시지 출력
    if (!currentUserId) {
        alert("로그인이 필요합니다.");  // 로그인 유도 메시지
        return;
    }

    fetch(`/api/community/posts/${communityPostId}/like/${currentUserId}`, {
        method: "post"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("해당 게시글의 좋아요 상태 불러오기 실패");
            }
            return response.json();
        })
        .then(isLiked => {
            console.log(isLiked);
            if (isLiked) {
                likeIcon.src = '/images/community/like.svg';
                fetch(`/api/community/posts/${communityPostId}/likes`)
                    .then(response => response.json())
                    .then(count => {
                        console.log("해당 게시글의 좋아요 개수 -> {}",count);
                     likeCount.textContent = count

                    });
            } else {
                likeIcon.src = '/images/community/unlike.svg';
                likeCount.textContent = "공감하기";
            }
        })
        .catch(error => console.error("좋아요 상태 업데이트 실패:", error));
});
