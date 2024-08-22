package com.nikguscode.SusuAPI.model.service.querymanager;

import org.springframework.stereotype.Service;

/**
 * An interface that provides methods for executing http requests.
 */
@Service
public interface Request {

    /**
     * A method designed to send a http request.
     *
     * @param cookie
     * @param link
     * @return <code>String</code>
     */
    String send(String cookie, String link);
}
