package com.example.SafeBlurrinder.repository;

import com.example.SafeBlurrinder.domain.UploadedVideoFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<UploadedVideoFile,Long> {

}
