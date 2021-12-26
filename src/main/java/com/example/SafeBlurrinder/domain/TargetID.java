package com.example.SafeBlurrinder.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TargetID")
@Getter
@Setter
@NoArgsConstructor
public class TargetID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long _id;

    @Column
    @NotNull
    private long targetVideoId;

    @Column
    @NotNull
    private String targetList;

    @Builder
    public TargetID(long id,String target) {
        this.targetVideoId=id;
        this.targetList=target;
    }
}
