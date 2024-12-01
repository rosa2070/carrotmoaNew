package carrotmoa.carrotmoa.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pension")
@Controller
public class PensionViewController {

    @GetMapping
    public String getFleaMarket() {
        return "pension";
    }
}
