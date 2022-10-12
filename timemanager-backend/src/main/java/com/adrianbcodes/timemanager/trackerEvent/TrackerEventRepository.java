package com.adrianbcodes.timemanager.trackerEvent;

import java.util.List;
import java.util.Optional;

public interface TrackerEventRepository {
    List<TrackerEvent> getAllTrackerEvents();
    Optional<TrackerEvent> getTrackerEventById(Long id);
    TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent);
    void deleteTrackerEvent(TrackerEvent trackerEvent);
}
