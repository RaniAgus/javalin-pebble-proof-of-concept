package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
  private String id;
  private String firstName;
  private String lastName;
  private LocalDate birthday;
  private String gender;
  private String email;
}
