package com.group_1.album.rabbitmq;

import com.group_1.amqp.dto.ImagesInAlbumRequest;
import com.group_1.album.service.AlbumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * com.group_1.album.rabbitmq
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 4:29 PM
 * Description: ...
 */
@Component
@AllArgsConstructor
@Slf4j
public class AlbumConsumer {

    private final AlbumService albumService;
    @RabbitListener(queues = "${rabbitmq.queue.album-add.name}")
    public void consumerAdd(ImagesInAlbumRequest request)
    {
        log.info("{} has add images to album {}: {}", request.userId(), request.albumName(), request.fileNames());
        albumService.addImagesToAlbum(request.userId(), request.albumName(), request.fileNames());
    }
    @RabbitListener(queues = "${rabbitmq.queue.album-del.name}")
    public void consumerDel(ImagesInAlbumRequest request)
    {
        log.info("{} has remove images from album {}: {}", request.userId(), request.albumName(), request.fileNames());
        albumService.deleteImagesFromAlbum(request.userId(), request.albumName(), request.fileNames());
    }
}
