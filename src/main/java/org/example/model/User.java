package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
  private String id;
  private String name;
  private String email;
  private UserDetails userDetails;
}
