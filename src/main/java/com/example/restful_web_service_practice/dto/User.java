package com.example.restful_web_service_practice.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;


//DTO : Data Transfer Object
//데이터 전송 목적

//@JsonIgnoreProperties(value = {"password","joinDate"})
//@JsonFilter("UserInfo")
//@ApiModel(description="all details about the user.")


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    @Size(min=2,message = "두 글자 이상 입력해 주세요.")
    private String name;

    @Past
    private Date joinDate;

    private String password;

    private String ssn;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public User(int id, String name, Date joinDate, String password, String ssn) {
        this.id  = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }

}
