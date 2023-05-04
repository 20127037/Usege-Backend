package com.group_1.amqp.dto;

/**
 * com.group_1.album.dto
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 4:30 PM
 * Description: ...
 */
public record ImagesInAlbumRequest(String userId, String albumName, String[] fileNames) {
}