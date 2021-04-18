package com.one.demo.redis.database;

import java.io.Serializable;
import org.springframework.data.redis.core.RedisHash;
import io.swagger.annotations.ApiModel;

@ApiModel
@RedisHash("Employee")
public class Employee implements Serializable {
   /**
    * 
    */
   private static final long serialVersionUID = 2043095696091500981L;
   public enum Gender { 
        MALE, FEMALE
    }
 
    private String id;
    private String name;
    private Gender gender;
   public String getId() {
          return id;
   }
   public void setId(String id) {
          this.id = id;
   }
   public String getName() {
          return name;
   }
   public void setName(String name) {
          this.name = name;
   }
   public Gender getGender() {
          return gender;
   }
   public void setGender(Gender gender) {
          this.gender = gender;
   }
}