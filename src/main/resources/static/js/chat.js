//userId 안들어감
window.onload = function () {
    let userNicknameMap = null;
    let stompClient = null;
    let chatRoomId = 0;

    if (document.getElementById('chat-modal-open')) {
        document.getElementById('chat-modal-open').addEventListener('click', function modalChat() {
            const modal = document.getElementById("chat-modal-content");
            const closeModalButtonId = 'chat-close-modal';
            fetch('/user/modal-content')
                .then(response => response.text())
                .then(data => {
                    modal.innerHTML = data; // 받아온 내용을 모달에 추가
                    modal.style.display = "flex"; // 모달 열기
                    const userId = document.getElementById('userId').value;
                    const inputMessage = document.getElementById('message');
                    findChatRoom(userId);

                    // 닫기 버튼 이벤트 추가
                    const closeModalButton = document.getElementById(closeModalButtonId);
                    closeModalButton.onclick = function () {
                        modal.style.display = "none"; // 모달 닫기
                    }

                    function connect(chatRoomId) {
                        const socket = new SockJS('/chat');
                        stompClient = Stomp.over(socket);
                        stompClient.connect({}, function (frame) {
                            console.log('Connected: ' + frame);
                            stompClient.subscribe('/sub/chat/' + chatRoomId, async function (message) {
                                userNicknameMap = await getNickname(userId, chatRoomId);
                                messageDiv(JSON.parse(message.body), userNicknameMap);
                            });
                        });
                    }


                    const chatMessage = document.getElementById('chat-messages');
                    //사용자가 들어가있는 채팅방 목록 호출
                    // document.getElementById("get-chat-room-list").addEventListener('click', function () {
                    //     findChatRoom(userId);
                    // })
                    //사용자가 선택한 채팅방 채팅목록 호출
                    document.getElementById('send-message-btn').addEventListener('click', function () {
                        sendMessage();
                    });
                    document.getElementById('send-message-btn').addEventListener('keydown', function (event) {
                        if (event.key === 'Enter') {  // Enter 키인지 확인
                            event.preventDefault(); // Enter 키로 기본 동작을 막기 (줄바꿈 방지)
                            sendMessage();
                        }
                    });

                    function sendMessage() {
                        document.getElementById('chat-room-id').value = chatRoomId;
                        inputMessage.value = document.getElementById('input-message').value;
                        const formData = new FormData(document.getElementById('chat-form'));

                        const data = {};
                        formData.forEach((value, key) => {
                            data[key] = value;
                        });
                        stompClient.send('/pub/chat/' + chatRoomId, {}, JSON.stringify(data));
                        inputMessage.value = "";
                    }

                    //선택한 채팅방 입장   -> websocket 구독 , db에서 채팅 목록 가져오기
                    async function getChatMessage(roomId) {
                        chatRoomId = roomId;
                        userNicknameMap = await getNickname(userId, chatRoomId);
                        if (stompClient && stompClient.connected) {
                            stompClient.disconnect(function () {
                                console.log('Disconnected from WebSocket.');
                            });
                        }
                        connect(chatRoomId);

                        fetch("/api/chat/get-message/" + roomId, {
                            method: "GET",
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(response => {
                                if (!response.ok) {
                                    alert("에러");
                                }
                                return response.json();
                            })
                            .then(async message => {
                                chatMessage.innerHTML = '';
                                document.getElementById('chat-room-id').value = message[0].chatRoomId;
                                //메시지에 넣어줄 닉네임 추가
                                userNicknameMap = await getNickname(userId, chatRoomId);
                                message.forEach(m => {
                                    messageDiv(m, userNicknameMap);
                                })
                                const chatMessages = document.getElementById('chat-messages');
                                chatMessages.scrollTop = chatMessages.scrollHeight;

                            })

                    }

                    function findChatRoom(userId) {
                        fetch("/api/chat/all-Room/" + userId, {
                            method: "GET",
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(response => {
                                if (!response.ok) {
                                    console.log("에러")
                                }
                                return response.json()
                            })
                            .then(result => {
                                const chatRoomList = document.getElementById('chat-room-list');
                                chatRoomList.innerHTML = '';

                                result.forEach(room => {
                                    const roomElement = document.createElement("div");
                                    const roomLinkElement = document.createElement("a")
                                    const roomNameElement = document.createElement("p")
                                    roomElement.classList.add('chat-room-item');
                                    // roomElement.id = "chat-room-item";
                                    roomLinkElement.classList.add('chat-room-item-link');
                                    roomLinkElement.onclick = function () {

                                        getChatMessage(room.roomId);
                                    }

                                    roomElement.appendChild(roomLinkElement);

                                    roomNameElement.classList.add('chat-room-item-title')
                                    roomNameElement.textContent = room.roomName;
                                    roomLinkElement.appendChild(roomNameElement);
                                    chatRoomList.appendChild(roomElement);
                                });
                            });
                    }

                    function messageDiv(message, userNicknameMap) {

                        const chatMessageElement = document.createElement("div");
                        const chatMessageTitleElement = document.createElement("p");
                        chatMessageTitleElement.className = 'chat-message-title';
                        const chatMessageContentElement = document.createElement("p");
                        chatMessageContentElement.className = 'chat-message-content';
                        const ChatMessageSendTimeElement = document.createElement('span');
                        ChatMessageSendTimeElement.className = 'chat-message-time';
                        // chatMessageContentElement.type = 'text'; // 타입을 text로 설정
                        // chatMessageContentElement.readOnly = true; // 수정 불가능하게 설정
                        chatMessageContentElement.textContent = message.message; // 초기 값 설정
                        if(userNicknameMap.myNickname.userId === message.userId){
                            chatMessageElement.className = 'chat-message-my';
                            chatMessageTitleElement.textContent = userNicknameMap.myNickname.nickname;
                        } else {
                            chatMessageElement.className = 'chat-message-opponent';
                            chatMessageTitleElement.textContent = userNicknameMap.joinNickname.nickname
                        }
                        // userNicknameMap.myNickname.userId === userId ?
                        //     chatMessageTitleElement.textContent = userNicknameMap.joinNickname.nickname :
                        //     chatMessageTitleElement.textContent = userNicknameMap.myNickname.nickname;
                        let createdAt = message.createdAt.split('T');
                        let createdAtDate = createdAt[0];
                        let createdAtTime = createdAt[1].split(':');
                        let morningAndAfternoon;
                        let hour = createdAtTime[0];
                        let minute = createdAtTime[1];
                        if (createdAtTime[0] - 11 >= 1) {
                            morningAndAfternoon = '오후'
                            hour = createdAtTime[0] - 12;
                        } else {
                            morningAndAfternoon = '오전'
                        }

                        let dateFormat = morningAndAfternoon + hour + ":" + minute;

                        ChatMessageSendTimeElement.textContent = dateFormat;
                        chatMessageElement.appendChild(chatMessageTitleElement);
                        chatMessageElement.appendChild(chatMessageContentElement);
                        chatMessageElement.appendChild(ChatMessageSendTimeElement);

                        chatMessage.appendChild(chatMessageElement);

                    }

                    document.getElementById('chat-find-user').addEventListener('click', function () {
                        let searchNickname = document.getElementById('chat-find-user-search').value;
                        chatFindUser(searchNickname);
                    });

                    function chatFindUser(searchKeyword) {
                        //userId  , nickname
                        fetch(`/api/user/find-user/nickname/${encodeURIComponent(searchKeyword)}`, {
                            method: "GET",
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(response => {
                                if (!response.ok) {
                                    console.log("찾을 수 없음")
                                }
                                return response.json()
                            })
                            .then(result => {
                                if (result.userId == null) {
                                    alert('해당 유저 없음');
                                }
                                document.getElementById('chat-find-result-id').value = result.userId;
                                document.getElementById('chat-find-result-nickname').textContent = result.nickname;
                            });
                    }

                    document.getElementById('chat-room-create').addEventListener('click', function () {
                        let joinTargetUserId = document.getElementById('chat-find-result-id').value;
                        createChatRoom(userId, joinTargetUserId);
                    })

                    //내 userId와 상대 userId를 보내서 채팅방 생성 중복시 이미 있는 채팅방 번호를 반환
                    function createChatRoom(myUserId, joinTargetUserId) {

                        fetch(`/api/chat/create-room?myUserId=${encodeURIComponent(myUserId)}&joinTargetUserId=${encodeURIComponent(joinTargetUserId)}`, {
                            method: 'GET',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                        })
                            .then(response => {
                                if (!response.ok) {
                                    console.log("채팅방 생성 실패")
                                }
                                return response.json()
                            })
                            .then(async result => {
                                console.log('채팅방 생성 , 입장');
                                if (chatRoomId === result) {
                                    await getChatMessage(chatRoomId);
                                } else {
                                    if (stompClient && stompClient.connected) {
                                        stompClient.disconnect(function () {
                                            chatMessage.innerHTML = '';
                                            console.log('Disconnected from WebSocket.');
                                        });
                                    }
                                    chatRoomId = result;
                                    userNicknameMap = await getNickname(userId, chatRoomId);
                                    connect(result);
                                    findChatRoom(userId);
                                }
                            })
                            .catch(error => {
                                console.error('채팅방 생성 에러:', error);
                            });
                    }


                }).catch(error => {
                console.error('모달 내용을 가져오는 데 오류가 발생했습니다:', error);
            });

        })
    }


    function simpleFetch(url, method, headers, errorMessage) {
        return fetch(url, {
            method: method,
            headers: headers,
        }).then(response => {
            if (!response.ok) {
                console.log("호출 실패 : " + url)
            }
            return response.json()
        }).then(result => {
            return result;
        }).catch(error => {
            if (errorMessage === undefined) {
                errorMessage = "simpleFetchError"
            }
            console.error(errorMessage + ":", error);
            throw error;
        });
    }

    async function getNickname(userId, chatRoomId) {
        try{
            return await simpleFetch(`/api/chat/find-chat-nickname/${userId}/${chatRoomId}`, "GET", {'Content-Type': 'application/json'});
        }catch(error){
            console.error("Error fetching nickname:", error);
            return null;
        }
    }
    function chat(myUserId, joinUserId){
        modalChat();
    }


}





