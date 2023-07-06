package study.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    @GetMapping("/admin/items/new")
    public String itemForm() {
        return "items/itemForm";
    }
}
