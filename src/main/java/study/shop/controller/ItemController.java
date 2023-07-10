package study.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import study.shop.dto.ItemFormDto;
import study.shop.service.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/admin/items/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "items/itemForm";
    }

    @PostMapping("/admin/items/new")
    public String itemForm(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                           @RequestParam("itemImgFile")List<MultipartFile> multipartFiles) {
        if(bindingResult.hasErrors())
            return "items/itemForm";

        if(multipartFiles.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값  입니다.");
            return "items/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품등록 중 에러가 발생하였습니다.");
            return "items/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/admin/items/{itemId}")
    public String getItemDetail(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto);

        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            //존재하지 않는 상품은 신규등록으로
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "items/itemForm";
        }
        return "items/itemForm";
    }
}
