package org.example.data;

import java.time.LocalDate;

public class Post {
  private String userId;
  private String userName;
  private LocalDate date;
  private Integer likes;
  private String details;

  public Post() {
  }

  public Post(String userId, String userName, LocalDate date, Integer likes, String details) {
    this.userId = userId;
    this.userName = userName;
    this.date = date;
    this.likes = likes;
    this.details = details;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
