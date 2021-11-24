package com.example.SafeBlurrinder.controller;

import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import com.example.SafeBlurrinder.service.VideoService;
import com.example.SafeBlurrinder.util.GenerateHash;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class VideoController {
    private VideoService videoService;
    @Autowired
    public VideoController(VideoService videoService){
        this.videoService=videoService;
    }

    @GetMapping("/")
    public String uploadVideo(){
        return "uploadVideoForm";
    }

    @PostMapping("/uploadVideo")
    public String saveVideo(@RequestParam("file") MultipartFile files) throws IOException {
        String origName=files.getOriginalFilename();
        System.out.println("original name: "+origName);
        String[] splited=origName.split("\\.");
        String saveName=new GenerateHash(origName).out()+"."+splited[splited.length-1];
        System.out.println("saved Name: "+saveName);
        String path=System.getProperty("user.home")+"\\GaoriVideos";

        if(!new File(path).exists()){
            try{
                new File(path).mkdir();
            }
            catch (Exception e){
                e.getStackTrace();
            }
        }
        String filePath = path + "\\" + saveName;
        files.transferTo(new File(filePath));

        Long id;
        try{
            id =videoService.saveUploadedVideo(origName,saveName,filePath);
        }catch (Exception e){
            System.out.println(e);
            return "redirect:/upload";
        }
        System.out.println("saved file id: "+id);
        return "redirect:/upload/"+id.toString();
    }

    @GetMapping("/upload/{id}")
    public String uploadResult(@PathVariable("id") Long id, Model model){
        UploadedVideoFile video=videoService.findUploadedVideoById(id);
        if(video!=null) {
            model.addAttribute("video",video);
            return "uploadSuccess";
        }
        else
            return "redirect:/upload";
    }
}
