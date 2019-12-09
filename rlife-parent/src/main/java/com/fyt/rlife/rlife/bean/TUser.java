package com.fyt.rlife.rlife.bean;

import lombok.*;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TUser {

    @Id
    private String id;
    private String username;
    private String userpswd;
}
