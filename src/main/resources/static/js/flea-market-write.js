const communityPostId = window.location.pathname.split('/').pop();

function isValidPostId(id) {
  return !isNaN(id) && id.trim() !== "";
}

document.getElementById("fleamarket-post-form").addEventListener("submit",
    function (event) {
      event.preventDefault();
      const editorContent = editor.getData();

      const title = document.getElementById("post-title").value;
      const price = document.getElementById("post-price").value;
      const categoryId = document.getElementById("post-category").value;
      const content = editorContent;
      const isFree = document.getElementById("isFree").checked;
      const isPriceOffer = document.getElementById("isPriceOffer").checked;

      if (!categoryId || isNaN(categoryId)) {
        alert("카테고리를 선택해주세요.");
        return;
      }

      const form = document.getElementById('fleamarket-post-form');

      const postData = {
        title,
        userId: form.userId.value,
        price: price ? parseInt(price) : 0,
        productCategoryId: parseInt(categoryId),
        content,
        isFree,
        isPriceOffer
      };

      fetch("/api/fleamarket/save", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(postData)
      })
      .then(response => {
        if (!response.ok) {
          throw new Error("글 저장에 실패했습니다.");
        }
        return response.json();
      })
      .then(data => {
        alert("글이 성공적으로 저장되었습니다.");
        window.location.href = "/fleamarket";
      })
      .catch(error => {
        alert(error.message);
        console.error("글 저장 중 오류가 발생했습니다:", error);
      });
    });

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
})

document.getElementById("post-price")
.addEventListener("input", function (event) {
  const value = event.target.value;
  if (!/^\d*$/.test(value)) {
    event.target.value = value.replace(/\D/g, '');
  }
});