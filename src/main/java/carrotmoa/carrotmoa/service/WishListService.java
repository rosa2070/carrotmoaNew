package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.repository.AccommodationRepository;
import carrotmoa.carrotmoa.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    @Autowired
    private AccommodationRepository accommodationRepository;
    private WishListRepository wishListRepository;

    public void updateWishList(Long userId, Long id) { // 두번째는 accommodationId
        Long postId = accommodationRepository.findPostIdById(id);
        if (postId != null) {
            wishListRepository.updateWishListByUserIdAndPostId(userId, postId);
        }
    }
}
