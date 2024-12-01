package carrotmoa.carrotmoa.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/community")
@Controller
public class CommunityViewController {

    @GetMapping
    public String getCommunityMain() {
        return "community";
    }

    @GetMapping("/write")
    public String getCommunityWrite() {
        return "community-write";
    }

    @GetMapping("/write/{communityPostId}")
    public String getCommunityWriteWithId(@PathVariable("communityPostId") Long communityPostId) {
        return "community-write";
    }

    @GetMapping("/posts/{postId}")
    public String getPostsDetail(@PathVariable("postId") Long postId) {
        return "community-detail";
    }
}
