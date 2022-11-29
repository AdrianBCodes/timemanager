package com.adrianbcodes.timemanager.trackerEvent;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackerEventRepository {
    Page<TrackerEvent> getAllTrackerEventsPagedAndFiltered(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, Pageable pageable);

    List<TrackerEvent> getAllTrackerEventsFilteredByUsernameAndDate(String username, LocalDateTime date);

    List<TrackerEvent> getAllTrackerEventsFilteredAndSorted(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, Sort sort);

    Optional<TrackerEvent> getTrackerEventById(Long id);
    TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent);
    void deleteTrackerEvent(TrackerEvent trackerEvent);
}
