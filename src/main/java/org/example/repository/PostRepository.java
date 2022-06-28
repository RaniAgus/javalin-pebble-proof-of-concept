package org.example.repository;

import org.example.data.Post;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  
  public PostRepository(UserRepository users) {
    persist(
        new Post(users.getById(1L), LocalDate.now(), 2,
            "Ugh. I'm so hungry. Does anyone want to play a game?"));
    persist(new Post(users.getById(2L), LocalDate.now(), 0,
        "Is it okay to hate your family? Like are you serious nephew? Seriously serious?"));
    persist(new Post(users.getById(3L), LocalDate.now(), 0,
        "Hey does anyone on here know a W.W? I'm working on a case."));
    persist(new Post(users.getById(4L), null, 45, "STEVE HOLT"));
    persist(new Post(users.getById(5L), LocalDate.now(), 1,
        "IT LOOKS TO ME LIKE YOU'RE ON THE WRONG SIDE OF THE RIVER"));
    persist(new Post(users.getById(6L), null, 7,
        "Welp. I don't think I'm going to be home for awhile."));
    persist(new Post(users.getById(7L), LocalDate.now(), 3,
        "You're through, Soup Nazi. Pack it up. No more soup for you. Next!"));
    persist(
        new Post(users.getById(8L), LocalDate.now(), -1,
            "Anyone want a kid? I'm done with this. F*** this noise."));
  }
  
  public List<Post> getAll() {
    return posts;
  }
  
  private void persist(Post post) {
    posts.add(post);
  }
}
