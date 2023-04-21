package com.group_1.uploadFile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFileDto {
    private String fileId;
    private List<String> tags;
    private String description;
    private String date;
    private long size;
    private String location;
    private String uri;
}
