package com.example.explorecalijpa.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.explorecalijpa.model.TourRating;
import com.example.explorecalijpa.recommendation.TourSummary;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends JpaRepository<TourRating, Integer>, CrudRepository<TourRating, Integer> {

  List<TourRating> findByTourId(Integer tourId);

  Optional<TourRating> findByTourIdAndCustomerId(Integer tourId, Integer customerId);

  @Query("""
         select tr.tour.id as tourId,
                tr.tour.title as title,
                avg(tr.score)  as avgScore,
                count(tr.id)   as reviewCount
         from TourRating tr
         group by tr.tour.id, tr.tour.title
         order by avg(tr.score) desc, count(tr.id) desc, tr.tour.title asc
      """)
  List<TourSummary> findTopTours(Pageable pageable);

  @Query("""
     select tr.tour.id as tourId,
            tr.tour.title as title,
            avg(tr.score)  as avgScore,
            count(tr.id)   as reviewCount
     from TourRating tr
     where tr.tour.id not in (
         select r.tour.id from TourRating r where r.customerId = :customerId
     )
     group by tr.tour.id, tr.tour.title
     order by avg(tr.score) desc, count(tr.id) desc, tr.tour.title asc
  """)
  List<TourSummary> findRecommendedForCustomer(int customerId, Pageable pageable);
}
