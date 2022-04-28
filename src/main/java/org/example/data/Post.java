package org.example.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
  private String userId;
  private String userName;
  private LocalDate date;
  private Integer likes;
  private String details;
}
