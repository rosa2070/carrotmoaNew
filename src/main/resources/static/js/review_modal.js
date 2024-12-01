const modal = document.getElementById("reviewModal");

// 팝업 열기 함수
function openReviewPopup() {
  modal.style.display = "flex";
}

// 팝업 닫기 함수
function closeReviewPopup() {
  modal.style.display = "none";
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
  if (event.target === modal) {
    closeReviewPopup();
  }
}