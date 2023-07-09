package study.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.shop.dto.ItemFormDto;

@Controller
public class ItemController {

    @GetMapping("/admin/items/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "items/itemForm";
    }
}
