package org.example.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.model.User;

@Data
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class UserDTO {
  private String id;
  private String name;
  private String email;

  public static UserDTO of(User user) {
    return builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .build();
  }
}
