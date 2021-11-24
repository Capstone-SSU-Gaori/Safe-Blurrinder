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

    //root 페이지 -> localhost:8080 -> 비디오 업로드 화면 등장~
    @GetMapping("/")
    public String uploadVideo(){
        return "uploadVideoForm";
    }

    //파일 업로드 후, submit 클릭 시 여기로 이동
    @PostMapping("/uploadVideo")
    public String saveVideo(@RequestParam("file") MultipartFile files) throws IOException {
        String origName=files.getOriginalFilename();
//        System.out.println("original name: "+origName);
        String[] splited=origName.split("\\.");
        String saveName=new GenerateHash(origName).out()+"."+splited[splited.length-1];  //hash값.mp4 로 파일 저장, splited[splited.length-1]이 "mp4"임
//        System.out.println("saved Name: "+saveName);
        String path=System.getProperty("user.home")+"\\GaoriVideos"; //여기서 user.home: 사용자의 홈 디렉토리(~)
        // -> 저같은 경우에는 User/users 라서  Users/user/GaoriVideos 폴더안에 비디오가 저장됨니다

        if(!new File(path).exists()){  //위의 폴더가 없다면 새로 생성해줌
            try{
                new File(path).mkdir();
            }
            catch (Exception e){
                e.getStackTrace();
            }
        }
        String filePath = path + "\\" + saveName; //해당 경로에 hash값+mp4 의 파일을 새로 생성해서
        files.transferTo(new File(filePath));//실제로 저장시켜줌

        Long id;
        try{
            id =videoService.saveUploadedVideo(origName,saveName,filePath); //업로드 하고 성공 시, DB에 저장된 id값 반환
        }catch (Exception e){
            System.out.println(e);
            return "redirect:/upload"; //DB에 저장 실패시, upload로 redirect
        }
//        System.out.println("saved file id: "+id);
        return "redirect:/upload/"+id.toString(); //업로드 한 영상 제목 보여주는 페이지 (바로 아래)로 redirect
    }

    @GetMapping("/upload/{id}")
    public String uploadResult(@PathVariable("id") Long id, Model model){
        UploadedVideoFile video=videoService.findUploadedVideoById(id); //id값으로 video객체 찾아와서
        if(video!=null) {
            model.addAttribute("video",video); //model에 저장 -> 나중에 html파일에서 이름 표시!
            return "uploadSuccess";
        }
        else//DB에서 해당 id값으로 저장된 data를 찾을 수 없다면, upload로 redirect
            return "redirect:/upload";
    }
}
