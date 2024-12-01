const notificationIcon = document.getElementById("notificationIcon");
const notificationModal = document.getElementById("notificationModal");
const notificationCloseModal = document.querySelector(".notification-close");
const notificationList = document.getElementById("notificationList");
const notificationToast = document.getElementById("notificationToast");

// 알림 조회 상태를 저장하는 플래그 변수 (로컬 스토리지 활용)
const isNotificationInitialized = localStorage.getItem("isNotificationInitialized") === "true";
let storedNotifications = JSON.parse(localStorage.getItem('notifications')) || [];
let modifiedNotifications = JSON.parse(localStorage.getItem("modifiedNotifications")) || [];

// sse 연결 설정
document.addEventListener("DOMContentLoaded", async function () {
// login이 된 상태면 -> SSE 연결하기.
    if (userObject) {
        const userId = userObject.userProfile.userId;
        const sse = new EventSource(`/sse/notifications/${userId}`);
        sse.onopen = function () {
            console.log('SSE 연결이 성공적으로 설정되었습니다.');
        };

        // 알림 DB를 처음만 조회하는 로직.

        if (!isNotificationInitialized) {
            storedNotifications = await fetchLoginUserNotifications(); // 데이터를 비동기로 가져오기
            localStorage.setItem("isNotificationInitialized", "true");
        }
        notificationStorageList(storedNotifications);
        sse.onmessage = function (event) {
            const notification = JSON.parse(event.data);
            console.log('새로운 알림:', notification);
            // 알림 UI 업데이트 로직 추가
            addNotificationToListSse(notification);
            saveNotificationToStorage(notification); // 로컬 스토리지에 저장
            updateNotificationIcon();
            showNotificationToast();
        };


        sse.onerror = function (err) {
            console.error('SSE 연결 오류:', err);
        };
        // 이벤트는 이름이고 이벤트.data로해서 값을 불러오는듯.
        // 작성해준 이벤트의 이름(connect)은 클라이언트가 이벤트를 불러올 때 사용!!!
        sse.addEventListener("connect", function (e) {
            let datatest = e.data;
            console.log("더미데이터 값을 받아서 사용이 가능할까? -> ", datatest)
        });

    } else {
        console.log('로그인되지 않은 사용자입니다.');
        localStorage.setItem("isNotificationInitialized", "false");
        clearNotificationsOnLogout();
        clearModifiedNotificationsOnLogout()
    }
    const allNotificationsDeleted = localStorage.getItem("allNotificationsDeleted") === "true";
    if (allNotificationsDeleted) {
        displayNoNotificationsMessage();
    }


});


// 로그인 유저의 알림 리스트 DB에서 조회
function fetchLoginUserNotifications() {
    const receiverId = userObject.userProfile.userId; // 로그인한 사용자 ID

    return fetch(`/api/notifications/${receiverId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('알림 리스트를 불러올 수 없습니다.');
            }
            return response.json();
        })
        .then(data => {
            console.log(`로그인 시, 첫 알림 데이터는 DB에서 가져오기`);
            console.log(data);
            localStorage.setItem('notifications', JSON.stringify(data));
            // 알림이 하나라도 있는지 & 전부 다, read가 false인지 확인.
            console.log(data.every(notification => notification.read === true));
            if (data.length > 0 &&  !data.every(notification => notification.read === true)) {
                updateNotificationIcon(); // 알림 아이콘 변경
            }
            return data; // 데이터 반환
        })
        .catch(error => {
            console.error('Fetch 오류:', error);
            throw error; // 에러를 상위로 전달
        });
}

// DB 알림 목록 -> 동적으로 html 생성
function notificationStorageList(notifications) {
    // 알림 목록이 비어있는지 확인
    // 기존 메시지 제거
    const existingMessage = document.querySelector(".no-notifications-message");
    if (existingMessage) {
        existingMessage.remove(); // 메시지가 존재하면 제거
    }

    if (notifications.length === 0) {
        displayNoNotificationsMessage();
    }

    notifications.forEach(notification => {
        if (notification.deleted) {
            return; // 삭제된 알림은 무시하고 다음 알림으로 넘어감
        }
        const notificationEntry = document.createElement("div");
        notificationEntry.className = "notification-entry";
        const notificationClass = notification.read ? 'notification-url read' : 'notification-url';
        notificationEntry.setAttribute("data-id", notification.id);
        notificationEntry.setAttribute("data-read", notification.read); // isRead 값 추가
        notificationEntry.setAttribute("data-deleted", notification.deleted); // isDeleted 값 추가
        // userName의 줄바꿈 문자를 제거
        const cleanUserName = notification.userName.replace(/\n/g, '').trim();
        ;
        notificationEntry.innerHTML = `
            <a class="${notificationClass}" href="${notification.url}">
                <div class="notification-user-pic">
                    <img src="${notification.picUrl}" alt="유저 프로필 이미지">
                </div>
                <div class="notification-details">
                    <div class="notification-title">${notification.title}</div>
                    <div class="notification-message">
                        ${cleanUserName}님: ${notification.message}
                    </div>
                    <div class="notification-createdAt">${formatElapsedTime(notification.createdAt)}</div>
                </div>
            </a>
                <div class="notification-delete-container">
                    <img src="/images/notification-delete-option.svg" class="notification-delete-icon" alt="삭제 아이콘">
                    <button class="notification-delete-button" >알림 삭제</button>
                </div>
        `;
        notificationList.appendChild(notificationEntry); // 알림 목록에 추가서부터 누적

        // 실시간 경과 시간 업데이트
        setInterval(() => {
            const timeElement = notificationEntry.querySelector(".notification-createdAt");
            timeElement.textContent = formatElapsedTime(notification.createdAt);
        }, 60000);  // 1분마다 업데이트
    });
}


// 단일 알림 SSE ->  단일 html 생성
function addNotificationToListSse(notification) {
    const noNotificationsMessage = document.querySelector(".no-notifications-message");
    if (noNotificationsMessage) {
        noNotificationsMessage.remove(); // 메시지를 DOM에서 제거
    }


    const notificationEntry = document.createElement("div");
    notificationEntry.className = "notification-entry";
    const notificationClass = notification.read ? 'notification-url read' : 'notification-url';
    notificationEntry.setAttribute("data-id", notification.id);
    notificationEntry.setAttribute("data-read", notification.read); // isRead 값 추가
    notificationEntry.setAttribute("data-deleted", notification.deleted); // isDeleted 값 추가
    notificationEntry.innerHTML = `
        <a class="${notificationClass}" href="${notification.url}">
            <div class="notification-user-pic">
                <img src="${notification.picUrl}" alt="유저 프로필 이미지">
            </div>
            <div class="notification-details">
                <div class="notification-title">${notification.title}</div>
                <div class="notification-message">
                    ${notification.userName}님: ${notification.message}
                </div>
                <div class="notification-createdAt">${formatElapsedTime(notification.createdAt)}</div>
            </div>
        </a>
            <div class="notification-delete-container">
                <img src="/images/notification-delete-option.svg" class="notification-delete-icon" alt="삭제 아이콘">
                <button class="notification-delete-button" >알림 삭제</button>
            </div>
    `;
    notificationList.insertBefore(notificationEntry, notificationList.firstChild); // SSE알림은 최상단에서부터 누적
    // 실시간 경과 시간 업데이트
    setInterval(() => {
        const timeElement = notificationEntry.querySelector(".notification-createdAt");
        timeElement.textContent = formatElapsedTime(notification.createdAt);
    }, 60000);  // 1분마다 업데이트
}


// 새로운 변경사항을 추가하는 스토리지
function addModifiedNotification(id, isRead = null, isDeleted = null) {
    const existingNotification = modifiedNotifications.find(notif => notif.id === id);

    if (existingNotification) {
        if (isRead !== null) existingNotification.isRead = isRead;
        if (isDeleted !== null) existingNotification.isDeleted = isDeleted;
    } else {
        modifiedNotifications.push({id, isRead, isDeleted});
    }

    localStorage.setItem("modifiedNotifications", JSON.stringify(modifiedNotifications));
}


// 버튼 외 다른 영역 클릭 시 버튼 숨기기
document.addEventListener("click", function (event) {
    const allDeleteButtons = document.querySelectorAll(".notification-delete-button");
    allDeleteButtons.forEach(button => {
        if (!button.closest(".notification-entry").contains(event.target)) {
            button.style.display = "none";
        }
    });
});


function updateNotificationIcon() {
    // 알림 아이콘을 변경
    notificationIcon.querySelector("img").src = "/images/notification-dot.svg"; // 기본 아이콘으로 복원
}

function showNotificationToast() {
    notificationToast.style.display = "block"; // 모달 보이기
    setTimeout(() => {
        notificationToast.style.opacity = 1; // 불투명도 설정
        notificationToast.style.transform = "translateY(0)"; // 기본 위치로 이동
    }, 10); // 약간의 지연 후 애니메이션 시작

    // 10초 후에 자동으로 사라지게 설정
    setTimeout(function () {
        notificationToast.style.opacity = 0; // 사라짐 효과
        setTimeout(function () {
            notificationToast.style.display = "none"; // 완전히 숨기기
        }, 500); // 사라짐 효과가 끝난 후 숨김
    }, 5000); // 5초 후
}


// 새 알림을 로컬 스토리지에 저장
function saveNotificationToStorage(notification) {
    // createdAt이 문자열이면 Date 객체로 변환
    if (typeof notification.createdAt === 'string') {
        notification.createdAt = new Date(notification.createdAt);
    }
    // 이미 Date 객체일 때만 toISOString() 호출
    notification.createdAt = notification.createdAt.toISOString();
    storedNotifications.unshift(notification); // 새로운 알림을 가장 앞에 추가
    localStorage.setItem('notifications', JSON.stringify(storedNotifications));
    localStorage.removeItem("allNotificationsDeleted"); // 새 알림이 추가되었으므로 상태 제거
}

// 로그아웃 시 로컬 스토리지 초기화
function clearNotificationsOnLogout() {
    localStorage.removeItem('notifications');
}

function clearModifiedNotificationsOnLogout() {
    console.log(JSON.stringify(modifiedNotifications));
    if (modifiedNotifications.length > 0) {
        // 서버로 modifiedNotifications 배열 전송
        fetch('/api/notifications', {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(modifiedNotifications) // 데이터를 JSON 형식으로 전송
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('서버에서 알림 업데이트 성공:', data);
                // 전송 후 localStorage에서 modifiedNotifications 제거
                localStorage.removeItem("modifiedNotifications");
            })
            .catch(error => {
                console.error('알림 업데이트 실패:', error);
            });
    } else {
        console.log('변경된 알림이 없습니다.');
    }

    localStorage.removeItem('modifiedNotifications');
}

// SSE 전용 경과 시간 포맷 함수 (매초마다 업데이트용)
function formatElapsedTime(createdAt) {
    const createdDate = new Date(createdAt);
    const now = new Date();
    const seconds = Math.floor((now - createdDate) / 1000);

    if (seconds < 60) {
        if(seconds === 0) {
            return '1초 전';
        }
        return `${seconds}초 전`;
    }
    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) return `${minutes}분 전`;
    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `${hours}시간 전`;
    const days = Math.floor(hours / 24);
    if (days < 30) return `${days}일 전`;
    const months = Math.floor(days / 30);
    if (months < 12) return `${months}개월 전`;
    return createdDate.toLocaleDateString();  // 1년 이상이면 날짜 표시
}

document.addEventListener("DOMContentLoaded", function () {
    if (notificationIcon) {
        notificationIcon.addEventListener("click", function () {
            notificationModal.style.display = "block"; // 모달 보이기
            notificationIcon.querySelector("img").src = "/images/notification.svg"; // 기본 아이콘으로 복원
        });
    }

    if (notificationCloseModal) {
        notificationCloseModal.addEventListener("click", function () {
            notificationModal.style.display = "none"; // 모달 숨기기
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    notificationList.addEventListener("click", function (event) {
        // 삭제 아이콘 클릭시 -> 삭제 버튼 화면 표시
        const deleteIcon = event.target.closest(".notification-delete-icon");
        if (deleteIcon) {
            const notificationEntry = deleteIcon.closest(".notification-entry");
            const deleteButton = notificationEntry.querySelector(".notification-delete-button");
            const isButtonVisible = deleteButton.style.display === "inline-block";
            deleteButton.style.display = isButtonVisible ? "none" : "inline-block";
        }
        // 삭제 버튼 클릭 시, 상태감지 스토리지에 변경 사항 저장.
        const deleteButton = event.target.closest(".notification-delete-button");
        if (deleteButton) {
            const notificationEntry = deleteButton.closest(".notification-entry");
            const notificationId = Number(notificationEntry.getAttribute("data-id"));
            console.log(notificationId);
            // storedNotifications에서 해당 알림의 isRead 상태 찾기
            const storedNotification = storedNotifications.find(
                (notif) => notif.id === notificationId
            );
            console.log(storedNotification);
            console.log(storedNotifications);
            console.log(storedNotification.read);
            // 현재 isRead 상태가 있는 경우 가져오고, 없으면 false로 초기화
            const notificationRead = storedNotification ? storedNotification.read : false;
            // 삭제 상태를 true로 설정하고 스토리지에 저장
            addModifiedNotification(notificationId, notificationRead, true);

            // notifications 배열에서 해당 알림의 deleted 값을 true로 변경

            if (storedNotification) {
                storedNotification.deleted = true; // deleted 값을 true로 변경
                localStorage.setItem('notifications', JSON.stringify(storedNotifications));
            }

            // UI 업데이트 - 해당 알림을 숨기거나 삭제된 상태로 표시
            notificationEntry.style.display = "none";
            // 알림이 모두 삭제된 경우 "알림이 없습니다" 메시지 추가
            if (!storedNotifications.some(notif => !notif.deleted)) {
                // 모든 알림이 삭제된 경우
                localStorage.setItem("allNotificationsDeleted", "true");
                displayNoNotificationsMessage();
            } else {
                // 알림이 남아 있는 경우
                localStorage.removeItem("allNotificationsDeleted");
            }
        }

        // 알림 링크 클릭 처리
        const notificationLink = event.target.closest(".notification-url");
        if (notificationLink) {
            const notificationEntry = notificationLink.closest(".notification-entry");
            const notificationId = Number(notificationEntry.getAttribute("data-id"));
            const notificationIsDeleted = notificationEntry.getAttribute("data-deleted");

            const isDeletedBoolean = notificationIsDeleted === 'true';

            const storedNotification = storedNotifications.find(
                (notif) => notif.id === notificationId
            );
            console.log(storedNotification);

            if (storedNotification) {
                // 알림을 읽음 상태로 변경
                storedNotification.read = true; // 읽음 상태로 변경
                localStorage.setItem('notifications', JSON.stringify(storedNotifications)); // 업데이트된 데이터를 localStorage에 저장
            }
            // TODO: 알림 읽는 것 추가.
            addModifiedNotification(notificationId, true, isDeletedBoolean);
        }
    });
});

// "알림이 없습니다" 메시지를 표시하는 함수
function displayNoNotificationsMessage() {
    const existingMessage = document.querySelector(".no-notifications-message");
    if (existingMessage) return; // 이미 메시지가 존재하면 함수 종료

    const noNotificationsMessage = document.createElement("div");
    noNotificationsMessage.className = "no-notifications-message";
    noNotificationsMessage.innerHTML = `<p>아직 알림이 없어요!</p>`;
    notificationList.appendChild(noNotificationsMessage);
}