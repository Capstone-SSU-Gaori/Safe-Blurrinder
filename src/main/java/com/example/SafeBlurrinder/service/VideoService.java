package com.example.SafeBlurrinder.service;

import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import com.example.SafeBlurrinder.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class VideoService {
    private VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository repository){
        this.videoRepository=repository;
    }

    public Long saveUploadedVideo(String originName, String saveName, String path){
        Long id=-1L;
        UploadedVideoFile video=new UploadedVideoFile(originName,saveName,path);
        try{
            id=videoRepository.save(video).get_id();
        }catch (Exception e){
            System.out.println(e);
            return -1L;
        }

        return id;
    }

    public UploadedVideoFile findUploadedVideoById(Long id){
        Optional<UploadedVideoFile> result=videoRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }else{
            return null;
        }
    }
}

