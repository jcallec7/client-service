package com.api.client.service;

public interface MessageService {

    String getMessage(String key);
    String getMessageWithParams(String key, Object[] params);

}
