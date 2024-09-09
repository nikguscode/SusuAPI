package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;

import com.nikguscode.SusuAPI.dto.iternal.StudentDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationRequestSender {
    String send(StudentDto studentDto);
}
