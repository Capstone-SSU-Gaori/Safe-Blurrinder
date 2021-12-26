package com.example.SafeBlurrinder.repository;

import com.example.SafeBlurrinder.domain.ProcessedVideoFile;
import org.apache.tomcat.jni.Proc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedVideoRepository extends JpaRepository<ProcessedVideoFile,Long> {

}
