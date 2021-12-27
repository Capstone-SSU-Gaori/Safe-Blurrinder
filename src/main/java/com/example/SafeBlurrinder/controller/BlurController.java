package com.example.SafeBlurrinder.controller;

import com.example.SafeBlurrinder.domain.ProcessedVideoFile;
import com.example.SafeBlurrinder.domain.TargetID;
import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import com.example.SafeBlurrinder.service.ProcessedVideoService;
import com.example.SafeBlurrinder.service.TargetIDService;
import com.example.SafeBlurrinder.service.VideoService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class BlurController {
    private TargetIDService targetIDService;
    private ProcessedVideoService processedVideoService;

    @Autowired
    public BlurController(TargetIDService service,ProcessedVideoService videoService) {
        this.targetIDService=service;
        this.processedVideoService=videoService;
    }

    @GetMapping("/select") //localhost:8080/select하면 화면나옴
    public String selectFace() {
        return "selectFace";
    }

    @GetMapping("/loading") //localhost:8080/loading하면 화면나옴
    public String loading() {
        return "loading";
    }

    @RequestMapping(value="/sendTarget")
    @ResponseBody
    public String sendBlur(HttpServletRequest request, Model model){
        String[] received=request.getParameterValues("clickedIds");
        System.out.println("process video id: "+received[0]);
        System.out.println("received data");
        String testData="";
        for(int i=1;i<received.length;i++){
            System.out.println(received[i]);
        }
        for(int i=1;i<received.length;i++){
            testData=testData+received[i].toString()+",";
        }

        Long id=Long.parseLong(received[0]);

        Long saved_id=targetIDService.saveTargets(id,testData); //targetVideoID, target list in string
        System.out.println("saved id: "+saved_id.toString());

        return saved_id.toString();

    }

    @GetMapping("/loading2/{id}")
    public String loading2(Model model,@PathVariable("id") Long id){

        model.addAttribute("targetId",id); //model에 저장 -> 나중에 html파일에서 이름 표시!
        return "loading2";

    }

    @GetMapping("/sendBlur/{id}")
    public String afterBlur(Model model, @PathVariable("id") Long id){
        int[] targets=targetIDService.findTargetListById(id);
        Long videoId=targetIDService.findTargetVideoById(id);
        int[] sendData=new int[targets.length+1];
        sendData[0]=videoId.intValue();
        for(int i=0;i<targets.length;i++){
            sendData[i+1]=targets[i];
        }

        String url = "http://34.125.188.227:5000/applyBlur"; // flask로 보낼 url
        StringBuffer stringBuffer=new StringBuffer();
        String sb = "";
        String processedId= "";
        try {
            JSONObject reqParams = new JSONObject();
            reqParams.put("targetAndId", sendData); // json object에 videoId를 담는다 -> 블러 적용도 똑같겠죠? 은주언니 코드 훔쳐가는 괴도소민

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

            // 전송된 결과를 읽어옴
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line = null;

            while ((line = br.readLine()) != null) {
                sb = sb + line;
            }
            System.out.println("========br======\n" + sb.toString());
            if (sb.toString().contains("ok")) {
                System.out.println("test");
            }
            br.close();

            processedId = sb.toString();
            System.out.println("processed id: "+processedId);
            Long _id=Long.parseLong(processedId);

            ProcessedVideoFile video=processedVideoService.findProcessedVideoById(_id);
            model.addAttribute("video", video);

            return "displayProcessedVideo";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";




    }
}
