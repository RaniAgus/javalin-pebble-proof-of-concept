package org.example.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Post {
  private @Id @GeneratedValue Long id;

  @ManyToOne
  private User user;
  private LocalDate date;
  private Integer likes;
  private String details;

  public Post() {
  }

  public Post(User user, LocalDate date, Integer likes, String details) {
    this.user = user;
    this.date = date;
    this.likes = likes;
    this.details = details;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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
