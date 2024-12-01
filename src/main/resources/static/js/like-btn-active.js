document.getElementById("likeButton").addEventListener("click", function () {
  const button = this;
  const accommodationId = button.getAttribute("data-accommodation-id");

  // 버튼의 active 상태 토글
  button.classList.toggle("is_active");

  // AJAX 요청 보내기
  fetch("/wishList/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams({
      "id": accommodationId
    })
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    // 성공적으로 완료된 경우
    console.log('Success: Wishlist updated');
  })
  .catch(error => {
    console.error('Error:', error);
  });
});