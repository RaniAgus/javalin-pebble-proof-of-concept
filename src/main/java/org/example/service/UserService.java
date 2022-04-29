package org.example.service;

import io.javalin.core.util.FileUtil;
import io.javalin.http.UploadedFile;
import org.example.data.User;
import org.example.repository.UserRepository;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

public class UserService implements WithGlobalEntityManager, TransactionalOps {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUser(Long id) {
    return this.userRepository.getUser(id);
  }

  public void editUserProfile(User user, UploadedFile photo) {
    entityManager().getTransaction().begin();
    this.userRepository.putUser(user);
    if (photo.getSize() > 0) {
      FileUtil.streamToFile(
          photo.getContent(),
          "static/images/" + user.getId() + ".jpg"
      );
    }
    entityManager().getTransaction().commit();
  }

}
