let communitySearchCurrentPage = 0; // 커뮤니티 페이지 번호
let accommodationSearchCurrentPage = 0; // 숙소 페이지 번호
let fleamarketSearchCurrentPage = 0;
let searchCurrentPage = 0; // 현재 페이지 번호
const searchPageSize = 6; // 페이지당 항목 수

document.addEventListener("DOMContentLoaded", function () {
    const integratedSearchInput = document.getElementById(
        "integrated-search-input");

    // 엔터 키 감지 이벤트 추가
    integratedSearchInput.addEventListener("keypress", function (event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 폼 제출 방지
            const keyword = event.target.value.trim(); // 검색어 가져오기
            if (validateSearchInput(keyword)) {
                searchCurrentPage = 0; // 페이지를 초기화
                searchIntegratedResults(keyword, searchCurrentPage, searchPageSize);
            }
        }
    });
});

// 검색 api 호출
function searchIntegratedResults(keyword, page, size) {
    // . 동네생활 API 호출
    const communityFetch = fetch(
        `/api/integrated-search/community?keyword=${keyword}&page=${page}&size=${size}`)
        .then(response => response.json());

    // 2. 숙소 API 호출
    const accommodationFetch = fetch(
        `/api/integrated-search/accommodation?keyword=${keyword}&page=${page}&size=${size}`)
        .then(response => response.json());

    // 3. 중고거래 API 호출 예정
    const fleamarketFetch = fetch(
        `/api/integrated-search/fleamarket?keyword=${keyword}&page=${page}&size=${size}`)
    .then(response => response.json());

    Promise.all([communityFetch, accommodationFetch, fleamarketFetch])
        .then(([communityData, accommodationData, fleamarketData]) => {
            console.log("동네생활 검색 결과: ", communityData)
            console.log("숙소정보 검색 결과: ", accommodationData)
            console.log("숙소정보 검색 결과: ", fleamarketData)
            if (page === 0) {
                clearPreviousResults(); // 첫 페이지일 경우 기존 결과를 지움
            }
            renderCommunityResults(communityData);  // 동네생활 데이터 렌더링
            renderAccommodationResults(accommodationData);  // 숙소 데이터 렌더링
            renderFleamarketResults(fleamarketData);  // 동네생활 데이터 렌더링
        })
        .catch(error => console.error('Error fetching search results:', error));
}

function renderFleamarketResults(data) {
    if (data.content.length === 0) return;

    createSearchResultSection();
    const resultContainer = createResultContainer('fleamarket', '중고거래', 'product-list');
    const articlesWrap = resultContainer.querySelector('#product-list');

    data.content.forEach(item => {
        const article = document.createElement('article');
        article.className = 'flea-market-article';

        const postLink = `/fleamarket/posts/${item.id}`;

        // 기본 이미지 설정
        let imageUrl = '/images/sample.png';

        // 이미지 가져오기 API 호출
        fetch(`/api/fleamarket/images/${item.id}`)
        .then(response => response.json())
        .then(images => {
            if (images.length > 0) {
                imageUrl = images[0].imageUrl;  // 첫 번째 이미지 URL 사용
            }
            // 이미지와 게시물 내용 설정
            article.innerHTML = `
                    <a class="post-link" href="${postLink}">
                        <div class="article-image">
                            <img alt="${item.title}" src="${imageUrl}" onerror="this.src='/images/sample.png'">
                        </div>
                        <div class="article-info">
                            <div class="title article-title">${item.title}</div>
                            <div class="price article-price">${Number(item.price).toLocaleString()} 원</div>
                            <div class="address article-address">${item.region1DepthName} ${item.region2DepthName}</div>
                            <div>
                                <span class="article-like">관심 10</span>
                                <span>ㅤ</span>
                                <span class="article-chat">채팅 10</span>
                            </div>
                        </div>
                    </a>`;
            articlesWrap.appendChild(article);
        })
        .catch(error => {
            console.error('이미지 로드 실패:', error);
            article.innerHTML = `
                    <a class="post-link" href="${postLink}">
                        <div class="article-image">
                            <img alt="${item.title}" src="${imageUrl}">
                        </div>
                        <div class="article-info">
                            <div class="title article-title">${item.title}</div>
                            <div class="price article-price">${Number(item.price).toLocaleString()} 원</div>
                            <div class="address article-address">${item.region1DepthName} ${item.region2DepthName}</div>
                            <div>
                                <span class="article-like">관심 10</span>
                                <span>ㅤ</span>
                                <span class="article-chat">채팅 10</span>
                            </div>
                        </div>
                    </a>`;
            articlesWrap.appendChild(article);
        });
    });

    manageMoreButton(data, resultContainer); // "더보기" 버튼 처리
}

// 커뮤니티 게시판 렌더링 함수
function renderCommunityResults(data) {
    if (data.content.length === 0) {
        return;
    } // 검색 결과가 없으면 종료

    createSearchResultSection(); // 통합 섹션 생성
    const resultContainer = createResultContainer('community', '동네생활',
        'community-wrap'); // 커뮤니티용 컨테이너 생성

    // community-wrap에 데이터 추가
    const articlesWrap = resultContainer.querySelector('#community-wrap');
    articlesWrap.appendChild(createCommunityArticles(data.content,
        document.getElementById("integrated-search-input").value.trim()));

    manageMoreButton(data, resultContainer); // "더보기" 버튼 처리
}

function renderAccommodationResults(data) {
    if (data.content.length === 0) {
        return;
    } // 검색 결과가 없으면 종료

    createSearchResultSection(); // 통합 섹션 생성
    const resultContainer = createResultContainer('accommodation', '숙소 정보',
        'accommodation-wrap'); // 숙소용 컨테이너 생성

    // accommodation-wrap에 데이터 추가
    const articlesWrap = resultContainer.querySelector('#accommodation-wrap');
    data.content.forEach(item => {
        const article = document.createElement('article');
        article.className = 'accommodation-article flat-card';

        article.innerHTML = `
            <a class="accommodation-article-link" href="${item.accommodationUrl}">
                <div class="card-photo">
                    <img alt="${item.title}" src="${item.imageUrl}">
                </div>
                <div class="article-info">
                    <div class="article-title-content">${item.title}</div>
                    <p class="article-region-name">${item.roadAddress}</p>
                    <p class="article-price">${item.price.toLocaleString()} 원 / 1박</p>
                    <section class="article-sub-info">
                        <span>방 ${item.roomCount}</span>
                        <span>화장실 ${item.bathRoomCount}</span>
                        <span>거실 ${item.livingRoomCount}</span>
                        <span>주방 ${item.kitchenCount}</span>
                    </section>
                </div>
            </a>`;
        articlesWrap.appendChild(article);
    });

    manageMoreButton(data, resultContainer); // "더보기" 버튼 처리
}

// 커뮤니티 게시글 생성 함수
function createCommunityArticles(communityResults, keyword) {
    const fragment = document.createDocumentFragment();
    const regex = new RegExp(`(${keyword})`, 'gi');

    communityResults.forEach(result => {
        const article = document.createElement('div');
        article.className = 'community-article';

        // 이미지가 있는 경우에만 이미지 태그 추가
        let imageHtml = '';
        if (result.postImageUrl) {
            imageHtml = `
                <div class="community-article-image">
                    <img alt="더미데이터" src="${result.postImageUrl}">
                </div>`;
        }

        // HTML에서 순수 텍스트만 추출하는 함수
        const extractPlainText = (html) => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            return doc.body.textContent || "";
        };

        // 순수 텍스트 추출
        const plainTextContent = extractPlainText(result.content);

        // 내용 길이 제한 (예: 100자)
        const MAX_LENGTH = 100;
        const truncatedContent = plainTextContent.length > MAX_LENGTH ?
            plainTextContent.substring(0, MAX_LENGTH) + '...' :
            plainTextContent;

        // 검색어 강조 처리
        const highlightedContent = truncatedContent.replace(regex,
            '<mark>$1</mark>');

        article.innerHTML = `
            <a href="${result.postUrl}" class="article-link">
                ${imageHtml} <!-- 이미지 HTML 추가 -->
                <div class="community-article-text">
                    <p class="community-article-content">${highlightedContent}</p>
                    <p class="community-region-name">${result.addressName}</p>
                </div>
            </a>`;
        fragment.appendChild(article);
    });

    return fragment;
}

// 통합검색을 담을 섹션 태그 생성
function createSearchResultSection() {
    const mainContainer = document.querySelector('main'); // 메인 컨테이너 선택
    let section = mainContainer.querySelector('#integrated-search-result');

    // 섹션이 이미 생성되어 있지 않으면 새로 생성
    if (!section) {
        section = document.createElement('section');
        section.id = 'integrated-search-result';
        mainContainer.appendChild(section);
    }
}

function createResultContainer(featureName, displayName, wrapId) {
    const section = document.querySelector('#integrated-search-result');
    let resultContainer = section.querySelector(`.${featureName}-container`);

    // 해당 기능의 result-container가 없으면 생성
    if (!resultContainer) {
        resultContainer = document.createElement('div');
        resultContainer.className = 'result-container ' + featureName
            + '-container';

        // 기능 이름을 담은 p.article-kind 추가
        const articleKind = document.createElement('p');
        articleKind.className = 'article-kind';
        articleKind.textContent = displayName; // 예: 동네생활, 숙소 정보
        resultContainer.appendChild(articleKind);

        const articlesWrap = document.createElement('div');
        articlesWrap.id = wrapId;  // 기능명-articles-wrap 형식
        articlesWrap.className = featureName + '-articles-wrap';

        resultContainer.appendChild(articlesWrap);
        section.appendChild(resultContainer);
    }

    return resultContainer;
}

function manageMoreButton(data, resultContainer) {
    let moreButton = resultContainer.querySelector('.more-btn');

    if (!data.last) { // 다음 페이지가 있는 경우
        if (!moreButton) {
            moreButton = document.createElement('div');
            moreButton.className = 'more-btn';
            moreButton.innerHTML = `
                <span class="more-text">더보기</span>
                <div class="more-loading">
                    <div class="loader"></div>
                </div>`;

            // 클릭 이벤트 추가
            moreButton.addEventListener('click', () => loadMoreResults());
            resultContainer.appendChild(moreButton);
        }
    } else if (moreButton) { // 마지막 페이지면 더보기 버튼 제거
        moreButton.remove();
    }
}

// ---------------------------------

// 더보기 버튼 클릭 시 호출되는 함수
function loadMoreResults() {
    searchCurrentPage++; // 페이지 번호 증가
    const keyword = document.getElementById(
        "integrated-search-input").value.trim(); // 현재 검색어 가져오기
    searchIntegratedResults(keyword, searchCurrentPage, searchPageSize); // 다음 페이지 데이터 요청
}

// 기존 검색 결과를 지우는 함수
function clearPreviousResults() {
    const mainContainer = document.querySelector('main');
    mainContainer.innerHTML = ''; // 모든 내용을 지우기
}

function showToast(message) {
    const toast = document.getElementById("integrated-search-toast");
    toast.textContent = message;
    toast.style.display = "block";

    // 3초 후에 자동으로 숨김
    setTimeout(() => {
        toast.style.display = "none";
    }, 3000);
}

// 예시: 검색어가 두 글자 미만일 때 호출
function validateSearchInput(keyword) {
    if (keyword.length < 2) {
        showToast("두 글자 이상 입력해주세요.");
        return false;
    }
    return true;
}