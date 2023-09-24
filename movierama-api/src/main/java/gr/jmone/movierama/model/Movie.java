package gr.jmone.movierama.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MOVIE")
public class Movie implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private UUID externalId;

  private String title;

  private String description;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private User publisher;

  private ZonedDateTime publicationDate;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "MOVIE_LIKES_ASSOCIATIONS",
      joinColumns = {@JoinColumn(name = "MOVIE_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
  public Set<User> likes;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "MOVIE_HATES_ASSOCIATIONS",
      joinColumns = {@JoinColumn(name = "MOVIE_ID", referencedColumnName = "ID")},
      inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
  public Set<User> hates;

  public void addLike(User user) {
    if (this.likes == null) {
      this.likes = new HashSet<>();
    }

    this.likes.add(user);
  }

  public void addHate(User user) {
    if (this.hates == null) {
      this.hates = new HashSet<>();
    }

    this.hates.add(user);
  }

  public void removeLike(User user) {
    if (this.likes == null) {
      this.likes = new HashSet<>();
    }

    this.likes.remove(user);
  }

  public void removeHate(User user) {
    if (this.hates == null) {
      this.hates = new HashSet<>();
    }

    this.hates.remove(user);
  }
}
