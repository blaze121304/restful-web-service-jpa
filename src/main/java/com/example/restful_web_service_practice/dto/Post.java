package com.example.restful_web_service_practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Data
//1. @Data 어노테이션 대신 @Getter @ToString
//setter 방지
//@Getter
//@ToString
//@RequiredArgsConstructor
//@EqualsAndHashCode

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    //User : Post -> 1 : N (0~N), Main : Sub -> Parent : Child

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

}
