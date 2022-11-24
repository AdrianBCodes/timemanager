package com.adrianbcodes.timemanager.trackerEvent;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface TrackerEventRepository {
    Page<TrackerEvent> getAllTrackerEventsPaged(Predicate predicate, Pageable pageable);

    List<TrackerEvent> getAllTrackerEvents(Predicate predicate);

    List<TrackerEvent> getAllTrackerEvents(Predicate predicate, Sort sort);

    Optional<TrackerEvent> getTrackerEventById(Long id);
    TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent);
    void deleteTrackerEvent(TrackerEvent trackerEvent);
}
