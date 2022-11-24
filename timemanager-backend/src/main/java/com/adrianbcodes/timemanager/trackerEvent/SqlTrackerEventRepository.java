package com.adrianbcodes.timemanager.trackerEvent;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface SqlTrackerEventRepository extends TrackerEventRepository, JpaRepository<TrackerEvent, Long>, QuerydslPredicateExecutor {

    Page<TrackerEvent> findAll(Predicate predicate, Pageable pageable);
    List<TrackerEvent> findAll(Predicate predicate);
    List<TrackerEvent> findAll(Predicate predicate, Sort sort);

    @Override
    default Page<TrackerEvent> getAllTrackerEventsPaged(Predicate predicate, Pageable pageable){
        return this.findAll(predicate, pageable);
    }
    @Override
    default List<TrackerEvent> getAllTrackerEvents(Predicate predicate) {
        return this.findAll(predicate);
    }

    @Override
    default List<TrackerEvent> getAllTrackerEvents(Predicate predicate, Sort sort) {
        return this.findAll(predicate, sort);
    }

    @Override
    default Optional<TrackerEvent> getTrackerEventById(Long id) {
        return this.findById(id);
    }

    @Override
    default TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent) {
        return this.save(trackerEvent);
    }

    @Override
    default void deleteTrackerEvent(TrackerEvent trackerEvent){
        this.delete(trackerEvent);
    }
}
