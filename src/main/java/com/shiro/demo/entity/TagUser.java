package com.shiro.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TagUser implements Serializable {

    @JsonProperty("tag_id")
    private String tagId;

    @JsonProperty("user_id")
    private String userId;


    @JsonProperty("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
