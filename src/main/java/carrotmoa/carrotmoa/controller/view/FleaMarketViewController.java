package carrotmoa.carrotmoa.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/fleamarket")
@Controller
public class FleaMarketViewController {

    @GetMapping
    public String getFleaMarket() {
        return "fleamarket/flea-market";
    }

    @GetMapping("/posts/{postId}")
    public String getPost(@PathVariable("postId") Long postId) {
        return "fleamarket/flea-market-post";
    }

    @GetMapping("/write")
    public String savePost() {
        return "fleamarket/flea-market-write";
    }

    @GetMapping("/update/{postId}")
    public String updatePost(@PathVariable("postId") Long postId) {
        return "fleamarket/flea-market-update";
    }
}
