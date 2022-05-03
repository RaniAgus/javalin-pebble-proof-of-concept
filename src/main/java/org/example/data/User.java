package org.example.data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class User {
  private @Id @GeneratedValue Long id;
  private String firstName;
  private String lastName;
  private LocalDate birthday;
  private String gender;
  private String email;
  private @Enumerated Role role;

  public User() {
  }

  public User(String firstName, String lastName, LocalDate birthday, String gender, String email, Role role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthday = birthday;
    this.gender = gender;
    this.email = email;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isAdmin() {
    return this.role == Role.ADMIN;
  }

}
