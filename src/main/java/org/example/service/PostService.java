package org.example.service;

import org.example.data.Post;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class PostService implements WithGlobalEntityManager {
  public List<Post> getPosts() {
    return entityManager()
        .createQuery("from Post", Post.class)
        .getResultList();
  }
}
