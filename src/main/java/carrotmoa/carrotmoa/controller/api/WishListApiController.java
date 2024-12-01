package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/wishList")
public class WishListApiController {
    @Autowired
    private WishListService wishListService;

    @PostMapping("/wishList/add")
    @ResponseBody
    public ResponseEntity<?> addWishList(@AuthenticationPrincipal CustomUserDetails user, @RequestParam("id") Long id) { // accommodationId
        Long userId = user.getUserProfile().getUserId();
        wishListService.updateWishList(userId, id);
        return ResponseEntity.ok().build();
    }
}
