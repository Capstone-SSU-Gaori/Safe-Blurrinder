package com.example.SafeBlurrinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class selectFaceController {

    @GetMapping("/select") //localhost:8080/select하면 화면나옴
    public String selectFace() {
        return "selectFace";
    }

    @GetMapping("/loading") //localhost:8080/loading하면 화면나옴
    public String loading() {
        return "loading";
    }
}
