package com.take2.take2.Info;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class customUser {
    @Id
    private Integer id;
    private String firstname;
    private String lastname;
    private String mail;

}
