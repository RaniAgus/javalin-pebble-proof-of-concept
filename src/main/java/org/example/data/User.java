package org.example.data;

import java.time.LocalDate;

public class User {
  private String id;
  private String firstName;
  private String lastName;
  private LocalDate birthday;
  private String gender;
  private String email;

  public User() {
  }

  public User(String id, String firstName, String lastName, LocalDate birthday, String gender, String email) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.gender = gender;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
