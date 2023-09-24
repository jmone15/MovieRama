package gr.jmone.movierama.repository.spec;

import gr.jmone.movierama.dto.MovieQueryDto;
import gr.jmone.movierama.model.Movie;
import gr.jmone.movierama.model.Movie_;
import gr.jmone.movierama.model.User_;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecifications {

  private MovieSpecifications() {
    // restricting object creation
  }

  public static Specification<Movie> fromQuery(MovieQueryDto movieQuery) {
    return (root, query, builder) -> {
      var predicate = builder.conjunction();

      query.groupBy(root.get(Movie_.id));

      if (movieQuery.getUserId() != null) {
        var user = root.join(Movie_.publisher, JoinType.LEFT);
        predicate =
            builder.and(
                predicate, builder.equal(user.get(User_.externalId), movieQuery.getUserId()));
      }

      if (movieQuery.getTerm().equals(OrderTerm.PUBLICATION_DATE)) {
        query.orderBy(
            sortByDirectionAndPath(
                builder, root.get(Movie_.publicationDate), movieQuery.getDirection()));
      } else if (movieQuery.getTerm().equals(OrderTerm.LIKES)) {
        var orderByLikesCount =
            sortByDirectionAndExpression(
                builder,
                builder.count(root.join(Movie_.likes, JoinType.LEFT)),
                movieQuery.getDirection());
        var orderByMovieId =
            sortByDirectionAndPath(builder, root.get(Movie_.id), movieQuery.getDirection());
        query.orderBy(orderByLikesCount, orderByMovieId);
      } else if (movieQuery.getTerm().equals(OrderTerm.HATES)) {
        var orderByLikesCount =
            sortByDirectionAndExpression(
                builder,
                builder.count(root.join(Movie_.hates, JoinType.LEFT)),
                movieQuery.getDirection());
        var orderByMovieId =
            sortByDirectionAndPath(builder, root.get(Movie_.id), movieQuery.getDirection());
        query.orderBy(orderByLikesCount, orderByMovieId);
      }

      return predicate;
    };
  }

  private static Order sortByDirectionAndPath(
      CriteriaBuilder builder, Path<?> path, SortingDirection direction) {
    if (direction.equals(SortingDirection.ASC)) {
      return builder.asc(path);
    }
    return builder.desc(path);
  }

  private static Order sortByDirectionAndExpression(
      CriteriaBuilder builder, Expression<?> expression, SortingDirection direction) {
    if (direction.equals(SortingDirection.ASC)) {
      return builder.asc(expression);
    }
    return builder.desc(expression);
  }
}
