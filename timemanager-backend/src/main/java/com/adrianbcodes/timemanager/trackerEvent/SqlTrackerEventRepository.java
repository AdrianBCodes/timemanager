package com.adrianbcodes.timemanager.trackerEvent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SqlTrackerEventRepository extends TrackerEventRepository, JpaRepository<TrackerEvent, Long> {


    @Override
    default List<TrackerEvent> getAllTrackerEvents() {
        return this.findAll();
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
