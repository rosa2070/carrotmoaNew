package carrotmoa.carrotmoa.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeViewController {

    @GetMapping
    public String getIndex() {
        return "pension";
    }

    @GetMapping("/search/{keyword}")
    public String getIntegratedSearchPage(@PathVariable(name = "keyword") String keyword) {
        return "integrated-search";
    }
}
