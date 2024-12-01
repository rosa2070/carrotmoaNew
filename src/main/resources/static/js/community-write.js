const communityPostId = window.location.pathname.split('/').pop(); // 게시글 ID 가져오기
console.log("게시글 아이디: ", communityPostId);

function isValidPostId(id) {
    return !isNaN(id) && id.trim() !== "";
}

document.addEventListener("DOMContentLoaded", function () {
    if (isValidPostId(communityPostId)) {
        document.getElementById("page-type").textContent = "동네생활 글 수정하기";
        document.getElementById("submit-btn").textContent = "수정완료";
        fetch(`/api/community/posts/${communityPostId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("해당 게시글을 불러올 수 없습니다.");
                } else {
                    return response.json();
                }
            })
            .then(data => {
                console.log(data);
                document.getElementById("community-post-title").value = data.title;
                editor.setData(data.content);
                document.getElementById(
                    "community-post-category").value = data.communityCategoryId;
            })
            .catch(error => console.error("에러 발생: ", error));
        document.getElementById("submit-btn").addEventListener('click',
            function (e) {
                e.preventDefault();
                const updatePost = {
                    title: document.getElementById("community-post-title").value,
                    content: editor.getData(),
                    communityCategoryId: document.getElementById("community-post-category").value
                };
                fetch(`/api/community/posts/${communityPostId}`, {
                    method: 'put',
                    headers: {
                        'Content-type': 'application/json'
                    },
                    body: JSON.stringify(updatePost)
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("게시글 수정 실패")
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("게시글 수정 완료 : ", data);
                        window.location.href = `/community/posts/${data}`;
                    })
                    .catch(error => console.log("에러 발생: ", error));
            });
    } else {
        document.getElementById("page-type").textContent = "동네생활 글쓰기";
        document.getElementById("submit-btn").textContent = "작성완료";
        document.getElementById("community-post-title").value = "";
        editor.setData("");

        document.getElementById("submit-btn").addEventListener('click',
            function (e) {
                e.preventDefault();
                const editorContent = editor.getData();
                const form = document.getElementById('community-post-form');

                const formData = {
                    userId: form.userId.value,
                    communityCategoryId: form.communityCategoryId.value,
                    title: form.title.value,
                    content: editorContent
                };

                // 제목 길이 체크
                if (formData.title.trim().length < 2) {
                    alert("제목은 2자 이상 입력해야 합니다."); // 경고 메시지
                    return; // 함수 종료
                }

                // 내용 길이 체크
                const textOnlyContent = new DOMParser().parseFromString(editorContent, 'text/html').body.textContent || "";
                if (textOnlyContent.trim().length < 5) {
                    alert("내용은 5자 이상 입력해야 합니다."); // 경고 메시지
                    return; // 함수 종료
                }



                fetch('/api/community/posts', {
                    method: 'post',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(formData)
                })
                    .then(response => response.json())
                    .then(postId => {
                        window.location.href = `/community/posts/${postId}`;

                    })
                    .catch(error => console.error('Error 발생: ', error));
            });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/community/categories")
        .then(response => response.json())
        .then(data => {
            console.log(data);
            const categorySelect = document.getElementById("community-post-category");
            const parentCategories = data.categories.filter(category => !category.parentId);

            parentCategories.forEach(parentCategory => {
                const optgroup = document.createElement("optgroup");
                optgroup.label = parentCategory.name;

                const childCategories = data.categories.filter(
                    category => category.parentId === parentCategory.id);
                childCategories.forEach(childCategory => {
                    const option = document.createElement("option");
                    option.value = childCategory.id;
                    option.textContent = childCategory.name;
                    optgroup.appendChild(option);
                });

                categorySelect.appendChild(optgroup);
            });
        })
        .catch(error => console.error("카테고리 데이터를 불러오지 못했습니다 : ", error));
})
