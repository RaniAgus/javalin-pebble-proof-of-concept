package org.example.repository;

import org.example.data.Post;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class PostRepository implements WithGlobalEntityManager {
  public List<Post> getPosts() {
    return entityManager()
        .createQuery("from Post", Post.class)
        .getResultList();
  }
}
