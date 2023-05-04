package com.group_1.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFileUploadDto {
    private List<String> tags;
    private String description;
    private String date;
    private String location;
    private String uri;
}
