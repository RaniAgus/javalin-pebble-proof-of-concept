package org.example;

import org.example.data.Post;
import org.example.data.User;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import java.time.LocalDate;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps, Runnable {
  @Override
  public void run() {
    withTransaction(this::persistUsers);
    withTransaction(this::persistPosts);
  }

  private void persistUsers() {
    persist(new User("Katniss", "Everdeen", LocalDate.now(), "Female", "keverdeen@gmail.com"));
    persist(new User("Tyrion", "Lannister", LocalDate.now(), "Male", "lann_the_man@gmail.com"));
    persist(new User("Hank", "Schrader", LocalDate.now(), "Male", "minerals@marie.com"));
    persist(new User("Steve", "Holt", null, "Male", "steve@holt.com"));
    persist(new User("Rick", "O'Connell", LocalDate.now(), "Male", "themummy@gmail.com"));
    persist(new User("Samwise", "Gamgee", null, "Male", "sammie_3@gmail.com"));
    persist(new User("Elaine", "Benes", LocalDate.now(), "Female", "e_ben@hotmail.com"));
    persist(new User("Kenny", "Powers", LocalDate.now(), "Male", "f_this_noise@aol.com"));
  }

  private void persistPosts() {
    persist(
        new Post(entityManager().find(User.class, 1L), LocalDate.now(), 2,
            "Ugh. I'm so hungry. Does anyone want to play a game?"));
    persist(new Post(entityManager().find(User.class, 2L), LocalDate.now(), 0,
        "Is it okay to hate your family? Like are you serious nephew? Seriously serious?"));
    persist(new Post(entityManager().find(User.class, 3L), LocalDate.now(), 0,
        "Hey does anyone on here know a W.W? I'm working on a case."));
    persist(new Post(entityManager().find(User.class, 4L), null, 45, "STEVE HOLT"));
    persist(new Post(entityManager().find(User.class, 5L), LocalDate.now(), 1,
        "IT LOOKS TO ME LIKE YOU'RE ON THE WRONG SIDE OF THE RIVER"));
    persist(new Post(entityManager().find(User.class, 6L), null, 7,
        "Welp. I don't think I'm going to be home for awhile."));
    persist(new Post(entityManager().find(User.class, 7L), LocalDate.now(), 3,
        "You're through, Soup Nazi. Pack it up. No more soup for you. Next!"));
    persist(
        new Post(entityManager().find(User.class, 8L), LocalDate.now(), -1,
            "Anyone want a kid? I'm done with this. F*** this noise."));
  }
}
