package com.shiro.demo.entity;

import com.shiro.demo.annotations.DocumentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTag implements Serializable {
    @DocumentId
    private String tagId;
    private  String userIds;
    private int age;
}
