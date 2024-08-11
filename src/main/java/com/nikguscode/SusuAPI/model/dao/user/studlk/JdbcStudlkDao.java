//package com.nikguscode.SusuAPI.model.dao.user.studlk;
//
//import com.nikguscode.SusuAPI.model.entities.StudentInfo;
//import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
//import org.springframework.stereotype.Service;
//import static com.nikguscode.SusuAPI.constants.StudentConstants.*;
//
//@Service
//public class JdbcStudlkDao implements StudlkDao {
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//    public JdbcStudlkDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
//        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
//    }
//
//    public void addInfo(StudentInfo studentInfo) {
//        String query = "INSERT INTO " + STUDLK_INFO_TABLE + "(id, " + STUDENT_GROUP_BD + ")" +
//                "VALUES (:id, :studentGroup)";
//        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(studentInfo);
//        namedParameterJdbcTemplate.update(query, namedParameters);
//    }
//}
