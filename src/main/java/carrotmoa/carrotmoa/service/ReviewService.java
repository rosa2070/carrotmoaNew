package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Review;
import carrotmoa.carrotmoa.model.request.SaveReviewRequest;
import carrotmoa.carrotmoa.repository.ReservationRepository;
import carrotmoa.carrotmoa.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    @Autowired
    //postId
    ReservationRepository reservationRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public void saveReview(Long reservationId, Long userId, String comment) {
        Long getPostId = reservationRepository.findPostIdByReservationId(reservationId);
        SaveReviewRequest reviewRequest = new SaveReviewRequest();
        reviewRequest.setPostId(getPostId);
        reviewRequest.setUserId(userId);
        reviewRequest.setComment(comment);
        Review reviewEntity = reviewRequest.toEntity();

        reviewRepository.save(reviewEntity);
    }

    @Transactional(readOnly = true)
    public Long getReservationId(Long reservationId) {
        return reviewRepository.getReviewId(reservationId);
    }
}
