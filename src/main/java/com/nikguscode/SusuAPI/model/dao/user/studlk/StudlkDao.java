package com.nikguscode.SusuAPI.model.dao.user.studlk;

import org.springframework.stereotype.Service;

@Service
public interface StudlkDao {
    void addInfo(String info);
    void deleteInfo();
    String getInfo();
}
