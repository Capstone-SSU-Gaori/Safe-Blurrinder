package com.example.SafeBlurrinder.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="UploadedVideo")
@Getter
@Setter
@NoArgsConstructor
public class UploadedVideoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long _id;

    @Column
    @NotNull
    private String originVideoName;

    @Column
    @NotNull
    private String savedVideoName;

    @Column
    @NotNull
    private String videoPath;

    @Builder
    public UploadedVideoFile(String origFilename, String filename, String filePath) {
        this.originVideoName = origFilename;
        this.savedVideoName = filename;
        this.videoPath = filePath;
    }
}
