package study.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.shop.dto.MemberFormDto;
import study.shop.service.MemberService;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFromDto", new MemberFormDto());
        return "members/memberForm";
    }

    @PostMapping("/new")
    public String memberForm(MemberFormDto memberFormDto){
        memberService.createMember(memberFormDto);
        return "redirect:/";
    }
}
