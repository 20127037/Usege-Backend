package com.group_1.album.rabbitmq;

import com.group_1.amqp.dto.ImagesInAlbumRequest;
import com.group_1.album.service.AlbumService;
import lombok.AllArgsConstructor;
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
public class AlbumConsumer {

    private final AlbumService albumService;
    @RabbitListener(queues = "${rabbitmq.queue.album-add.name}")
    public void consumerAdd(ImagesInAlbumRequest request)
    {
        albumService.addImagesToAlbum(request.userId(), request.albumName(), request.fileNames());
    }
    @RabbitListener(queues = "${rabbitmq.queue.album-del.name}")
    public void consumerDel(ImagesInAlbumRequest request)
    {
        albumService.addImagesToAlbum(request.userId(), request.albumName(), request.fileNames());
    }
}
