document.getElementById('btn_search').addEventListener('click', function () {
  const keyword = document.getElementById('txt_search_keyword').value;

  if (keyword.trim()) {
    const url = `/guest/search?keyword=${encodeURIComponent(keyword)}`;
    window.location.href = url;
  } else {
    alert('검색어를 입력해주세요.');
  }
});