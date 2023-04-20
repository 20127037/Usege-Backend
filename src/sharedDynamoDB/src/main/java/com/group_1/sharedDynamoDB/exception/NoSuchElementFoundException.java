package com.group_1.sharedDynamoDB.exception;


/**
 * exception
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:20 PM
 * Description: ...
 */

public class NoSuchElementFoundException extends RuntimeException {

    public NoSuchElementFoundException(String elementId, String collection) {
        super(String.format("%s was not found in %s", elementId, collection));
    }
    public NoSuchElementFoundException(String elementId) {
        super(String.format("%s was not found", elementId));
    }
}

