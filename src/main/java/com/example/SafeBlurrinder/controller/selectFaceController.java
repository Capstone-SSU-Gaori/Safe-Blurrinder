package com.example.SafeBlurrinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
//    @GetMapping("/getVideoId") // flask -> spring 테스트 코드
//    public ModelAndView sendVideoId() {
//        ModelAndView mav = new ModelAndView();
//
//        String url = "http://127.0.0.1:5000/getVideoId"; // flask에서 데이터를 보낸 url
//        String sb = "";
//        try {
//            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
//
//            String line = null;
//
//            while ((line = br.readLine()) != null) {
//                sb = sb + line + "\n";
//            }
//            System.out.println("========br======" + sb.toString());
//            if (sb.toString().contains("ok")) {
//                System.out.println("test");
//            }
//            br.close();
//            System.out.println("" + sb.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mav.addObject("cropImages", sb.toString()); //
//        mav.setViewName("selectFace");   // templates 이름
//        return mav;
//    }