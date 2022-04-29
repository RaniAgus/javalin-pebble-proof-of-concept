package org.example.service;

import org.example.data.Post;
import org.example.repository.PostRepository;

import java.util.List;

public class PostService {
  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public List<Post> getPosts() {
    return this.postRepository.getPosts();
  }
}
