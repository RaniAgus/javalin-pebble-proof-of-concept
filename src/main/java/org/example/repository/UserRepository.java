package org.example.repository;

import org.example.data.User;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.Optional;

public class UserRepository implements WithGlobalEntityManager {
  public User getUser(Long id) {
    return Optional.ofNullable(entityManager().find(User.class, id))
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public User getUserByEmail(String email) {
    return entityManager()
        .createQuery("from User u where u.email like :email", User.class)
        .setParameter("email", "%" + email + "%")
        .getResultList().stream()
        .findFirst()
        .orElseThrow(() -> new UserNotFoundException(email));
  }

  public void putUser(User user) {
    if (entityManager().find(User.class, user.getId()) == null) {
      throw new UserNotFoundException(user.getId());
    }
    entityManager().merge(user);
  }
}
