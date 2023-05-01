package com.group_1.uploadFile.exception;

import com.group_1.uploadFile.dto.NotEnoughSpaceResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * com.group_1.uploadFile.exception
 * Created by NhatLinh - 19127652
 * Date 5/1/2023 - 11:44 PM
 * Description: ...
 */
@ControllerAdvice
public class UploadFileExceptionController {
    @ExceptionHandler(ExceedSpaceException.class)
    public ResponseEntity<NotEnoughSpaceResponseDto> handleNotEnoughSpaceException(
            ExceedSpaceException exception
    ) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(new NotEnoughSpaceResponseDto(
                exception.getMaxSpace(),
                exception.getUsedSpace(),
                exception.getNeedSpace()
        ));
    }
    @ExceptionHandler(IOException.class)
    @ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleInvalidFileException(IOException exception
    ) {

    }

}
