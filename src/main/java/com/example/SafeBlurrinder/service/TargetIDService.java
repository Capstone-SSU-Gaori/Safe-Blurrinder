package com.example.SafeBlurrinder.service;

import com.example.SafeBlurrinder.domain.ProcessedVideoFile;
import com.example.SafeBlurrinder.domain.TargetID;
import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import com.example.SafeBlurrinder.repository.ProcessedVideoRepository;
import com.example.SafeBlurrinder.repository.TargetIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TargetIDService {
    private TargetIDRepository targetRepository;

    @Autowired
    public TargetIDService(TargetIDRepository repository){
        this.targetRepository=repository;
    }

    public Long saveTargets(Long id, String targets){
        Long _id=-1L;
        TargetID target=new TargetID(id,targets);
        try{
            _id=targetRepository.save(target).get_id();
        }catch (Exception e){
            System.out.println(e);
            return -1L;
        }

        return _id;
    }

    public int[] findTargetListById(Long id){
        Optional<TargetID> result=targetRepository.findById(id);
        if(result.isPresent()){
            TargetID target=result.get();
            String stringTarget=target.getTargetList();
            String[] temp=stringTarget.split(",");
            int[] targets=new int[temp.length];
            for (int i=0;i<targets.length;i++){
                targets[i]=Integer.parseInt(temp[i]);
            }
            return targets;
        }else{
            return null;
        }
    }

    public Long findTargetVideoById(Long id){
        Optional<TargetID> result=targetRepository.findById(id);
        if(result.isPresent()){
            TargetID target=result.get();
            Long videoId=target.getTargetVideoId();
            return videoId;
        }else{
            return null;
        }
    }
}

