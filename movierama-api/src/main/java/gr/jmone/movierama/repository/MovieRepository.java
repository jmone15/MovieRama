package gr.jmone.movierama.repository;

import gr.jmone.movierama.model.Movie;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository
    extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
  Movie findByTitle(String title);

  Movie findByExternalId(UUID externalId);

  @Query(
      "select mv from Movie mv "
          + "LEFT JOIN mv.likes lk "
          + "LEFT JOIN mv.hates ht "
          + "where lk.id = :id "
          + "or ht.id=:id")
  List<Movie> findByRelation(Long id);

  Optional<Movie> findByExternalIdAndPublisher_ExternalId(UUID movieId, UUID userId);
}
