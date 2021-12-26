package com.example.SafeBlurrinder.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="processedVideo2")
@Getter
@Setter
@NoArgsConstructor
public class ProcessedVideoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long _id;

    @Column
    private String processedVideoName;

    @Column
    private String savedVideoName;

    @Column
    private String videoPath;

    @Builder
    public ProcessedVideoFile(String origFilename, String filename, String filePath) {
        this.processedVideoName = origFilename;
        this.savedVideoName = filename;
        this.videoPath = filePath;
    }
}
