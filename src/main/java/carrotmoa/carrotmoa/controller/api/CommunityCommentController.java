package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.SaveCommunityCommentRequest;
import carrotmoa.carrotmoa.model.request.SaveCommunityReplyRequest;
import carrotmoa.carrotmoa.service.CommunityCommentService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@Slf4j
public class CommunityCommentController {
    final private CommunityCommentService communityCommentService;

    @PostMapping("/posts/{communityPostId}/comments")
    public ResponseEntity<Long> createCommunityComment(@PathVariable("communityPostId") Long communityPostId,
        @RequestBody SaveCommunityCommentRequest saveCommunityCommentRequest) {
        Long communityCommentId = communityCommentService.createCommunityComment(communityPostId, saveCommunityCommentRequest);
        return new ResponseEntity<>(communityCommentId, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{communityPostId}/comments")
    public ResponseEntity<Map<String, Object>> findActiveCommentsByCommunityPostId(@PathVariable("communityPostId") Long communityPostId) {
        Map<String, Object> activeCommentsByCommunityPostId = communityCommentService.findActiveCommentsByCommunityPostId(communityPostId);
        return new ResponseEntity<>(activeCommentsByCommunityPostId, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{communityPostId}/comments/{commentId}")
    public ResponseEntity<Long> softDeleteComment(@PathVariable("communityPostId") Long communityPostId, @PathVariable("commentId") Long commentId) {
        Long softDeletedId = communityCommentService.softDeleteCommentById(commentId, communityPostId);
        return new ResponseEntity<>(softDeletedId, HttpStatus.OK);
    }


    @PostMapping("/posts/{communityPostId}/comments/{commentId}/replies")
    public ResponseEntity<Long> createCommunityReply(@PathVariable("communityPostId") Long communityPostId, @PathVariable("commentId") Long commentId,
        @RequestBody SaveCommunityReplyRequest saveCommunityCommentRequest) {
        Long replyId = communityCommentService.createCommunityReply(communityPostId, commentId, saveCommunityCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(replyId);
    }
}
