package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.ChatMessage;
import carrotmoa.carrotmoa.entity.ChatRoom;
import carrotmoa.carrotmoa.entity.ChatRoomUser;
import carrotmoa.carrotmoa.model.request.ChatMessageRequest;
import carrotmoa.carrotmoa.model.request.ChatRoomRequest;
import carrotmoa.carrotmoa.model.request.ChatRoomUserRequest;
import carrotmoa.carrotmoa.model.response.FindUserResponse;
import carrotmoa.carrotmoa.repository.ChatMessageRepository;
import carrotmoa.carrotmoa.repository.ChatRoomRepository;
import carrotmoa.carrotmoa.repository.ChatRoomUserRepository;
import carrotmoa.carrotmoa.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final String destination = "/chat/sub/";

    private final SimpMessagingTemplate template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserProfileRepository userProfileRepository;

    //저장과 동시에 보내는건 즉각적으로 해야하나 db에 저장하는건 묶어서 할 방법이 있으면 바꾸기
    @Transactional
    public ChatMessageRequest sendMessage(ChatMessageRequest message) {
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId())
            .orElseThrow(() -> new EntityNotFoundException("해당 채팅방을 찾을 수 없습니다. : " + message.getChatRoomId()));
        template.convertAndSend(destination + message.getChatRoomId(), message);
        ChatMessage chatMessageEntity = chatMessageRepository.save(message.toEntityChatMessage());
        return new ChatMessageRequest(chatMessageEntity);
    }

    public List<ChatRoomRequest> getAllChatRooms(long userId) {
        return chatRoomUserRepository.findByUserId(userId)
            .stream()
            .map(entity -> chatRoomRepository.findById(entity.getChatRoomId()))
            .filter(Optional::isPresent)
            .map(room -> {
                Long relativeUserId = chatRoomUserRepository.findRelativeUserId(userId, room.get().getId());
                String nickname = (relativeUserId != null)
                    ? userProfileRepository.findByUserId(relativeUserId).getNickname()
                    : null;
                return new ChatRoomRequest(room.get(), nickname);
            })
            .collect(Collectors.toList());
    }

    public List<ChatMessageRequest> getChatMessage(long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId)
            .stream().map(ChatMessageRequest::new).toList();
    }

    //채팅방 생성 메서드 (이미 상대와의 채팅방이 있다면 해당 채팅방의 id를 반환 , 없다면 채팅방을 만들고 만든 채팅방id를 반환)
    public long createChatRoom(long myUserId, long joinTargetUserId) {
        if (duplicatedChatRoom(myUserId, joinTargetUserId).isEmpty()) {
            long roomId = chatRoomRepository.save(new ChatRoomRequest().toEntityChatRoom()).getId();
            chatRoomUserRepository.save(new ChatRoomUserRequest(myUserId, roomId).toEntityChatRoomUser());
            chatRoomUserRepository.save(new ChatRoomUserRequest(joinTargetUserId, roomId).toEntityChatRoomUser());
            return roomId;
        } else {
            return duplicatedChatRoom(myUserId, joinTargetUserId).get().getChatRoomId();
        }
    }

    //두 유저사이에 채팅방이 있는지 검사 있다면 객체로 반환
    public Optional<ChatRoomUser> duplicatedChatRoom(long userId, long joinUserId) {
        return chatRoomUserRepository.duplicateChatRoomId(userId, joinUserId);
    }

    public HashMap<String, FindUserResponse> findChatUserNickname(long myUserId, long chatRoomId) {
        HashMap<String, FindUserResponse> map = new HashMap<>();
        map.put("myNickname", new FindUserResponse(userProfileRepository.findByUserId(myUserId)));
        map.put("joinNickname",
            new FindUserResponse(userProfileRepository.findByUserId(chatRoomUserRepository.findRelativeUserId(myUserId, chatRoomId))));
        return map;
    }


}
