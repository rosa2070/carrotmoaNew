package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.model.response.CommunityCommentResponse;
import java.util.List;

public interface CommunityCommentCustomRepository {
    List<CommunityCommentResponse> findCommentsByPostIdOrdered(Long communityPostId);
}
