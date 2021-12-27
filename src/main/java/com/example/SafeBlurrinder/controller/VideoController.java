package com.example.SafeBlurrinder.controller;

import com.example.SafeBlurrinder.domain.ProcessedVideoFile;
import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import com.example.SafeBlurrinder.service.ProcessedVideoService;
import com.example.SafeBlurrinder.service.VideoService;
import com.example.SafeBlurrinder.util.GenerateHash;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.json.simple.parser.JSONParser;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class VideoController {
    private VideoService videoService;
    private ProcessedVideoService processedVideoService;

    @Autowired
    public VideoController(VideoService videoService,ProcessedVideoService processedVideoService) {
        this.videoService = videoService;
        this.processedVideoService=processedVideoService;
    }

    //root 페이지 -> localhost:8080 -> 비디오 업로드 화면 등장~
    @GetMapping("/")
    public String uploadVideo() {
        return "uploadVideoForm";
    }

    //파일 업로드 후, submit 클릭 시 여기로 이동
    @PostMapping("/uploadVideo")
    public String saveVideo(@RequestParam("file") MultipartFile files, Model model) throws IOException {
        if (files.isEmpty())
            return "redirect:/"; // 아무파일도 안올리고 업로드 누를경우 (별도 처리 필요)

        String origName = files.getOriginalFilename();
        String[] splited = origName.split("\\.");
        String saveName = new GenerateHash(origName).out() + "." + splited[splited.length - 1];  //hash값.mp4 로 파일 저장, splited[splited.length-1]이 "mp4"임
        String path = System.getProperty("user.home") + "\\GaoriVideos"; //여기서 user.home: 사용자의 홈 디렉토리(~)
        // -> 저같은 경우에는 User/users 라서  Users/user/GaoriVideos 폴더안에 비디오가 저장됨니다

        if (!new File(path).exists()) {  //위의 폴더가 없다면 새로 생성해줌
            try {
                new File(path).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        String filePath = path + "\\" + saveName; //해당 경로에 hash값 + mp4 의 파일을 새로 생성해서
        files.transferTo(new File(filePath));//실제로 저장시켜줌

        Long id = 0L;
        try {
            id = videoService.saveUploadedVideo(origName, saveName, filePath); //업로드 하고 성공 시, DB에 저장된 id값 반환
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/upload"; //DB에 저장 실패시, upload로 redirect
        }

        return "redirect:/upload/" + id;
    }

    //여기서 영상 재업로드 or 처리 진행 화면으로 넘어감
    @GetMapping("/upload/{id}")
    public String uploadResult(@PathVariable("id") Long id, Model model){
        UploadedVideoFile video=videoService.findUploadedVideoById(id); //id값으로 video객체 찾아와서
        if(video!=null) {
            model.addAttribute("video",video); //model에 저장 -> 나중에 html파일에서 이름 표시!
            return "uploadSuccess";
        }
        else//DB에서 해당 id값으로 저장된 data를 찾을 수 없다면, upload로 redirect
            return "redirect:/";
    }

    @GetMapping("/loading/{id}") //localhost:8080/loading하면 화면나옴
    public String loading(Model model, @PathVariable("id") Long id)
    {
        UploadedVideoFile video=videoService.findUploadedVideoById(id);
        if(video!=null) {
            model.addAttribute("video",video); //model에 저장 -> 나중에 html파일에서 이름 표시!
            return "loading";
        }
        else//DB에서 해당 id값으로 저장된 data를 찾을 수 없다면, upload로 redirect
            return "redirect:/";

    }

    // "처리 진행" 버튼 선택하면 여기서 FLASK로 데이터 보내고, 결과 받으면 selectFace로 넘어감
    @GetMapping("/processVideo/{id}")
    public String processVideo(Model model, @PathVariable("id") Long id){
        // mysql에 저장한 비디오 id를 json 형태로 저장하고 flask로 보내는 코드 시작입니다
        String url = "http://127.0.0.1:5000/getVideoId"; // flask로 보낼 url
        StringBuffer stringBuffer=new StringBuffer();
        String sb = "";
        String tempObject= "";
        try {
            JSONObject reqParams = new JSONObject();
            reqParams.put("videoId", id); // json object에 videoId를 담는다

            // Java 에서 지원하는 HTTP 관련 기능을 지원하는 URLConnection
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true); //Post인 경우 데이터를 OutputStream으로 넘겨 주겠다는 설정

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            //데이터 전송
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(reqParams.toString());
            System.out.println("reqParams = " + reqParams.toString());

            os.flush();

            System.out.println("waiting");
            // 전송된 결과를 읽어옴
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line = null;

            System.out.println("before while");
            while ((line = br.readLine()) != null) {
                sb = sb + line + "\n";
            }
            System.out.println("========br======\n" + sb.toString());
            if (sb.toString().contains("ok")) {
                System.out.println("test");
            }
            br.close();

            tempObject = sb.toString();

            model.addAttribute("cropImages", tempObject);
            model.addAttribute("_id",id);
            return "selectFace";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public void download(HttpServletResponse response, @PathVariable("id") Long id) throws Exception {
        try {
            ProcessedVideoFile video=processedVideoService.findProcessedVideoById(id);
            String path=video.getVideoPath();
            String name=video.getProcessedVideoName();

            File file = new File(path);
            response.setHeader("Content-Disposition", "attachment;filename=" + name);

            FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기
            OutputStream out = response.getOutputStream();

            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

        } catch (Exception e) {
            throw new Exception("download error");
        }

    }
}
