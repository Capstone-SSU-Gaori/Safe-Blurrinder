package com.example.SafeBlurrinder.service;

import com.example.SafeBlurrinder.domain.ProcessedVideoFile;
import com.example.SafeBlurrinder.repository.ProcessedVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProcessedVideoService {
    private ProcessedVideoRepository videoRepository;

    @Autowired
    public ProcessedVideoService(ProcessedVideoRepository repository){
        this.videoRepository=repository;
    }


    public ProcessedVideoFile findProcessedVideoById(Long id){
        Optional<ProcessedVideoFile> result=videoRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }else{
            return null;
        }
    }
}

