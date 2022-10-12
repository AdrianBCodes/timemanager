package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.exceptions.NotFoundException;

import java.util.List;

public class TrackerEventService {
    private final TrackerEventRepository trackerEventRepository;

    public TrackerEventService(TrackerEventRepository trackerEventRepository) {
        this.trackerEventRepository = trackerEventRepository;
    }

    List<TrackerEvent> getAllTrackerEvents(){
        return trackerEventRepository.getAllTrackerEvents();
    }
    TrackerEvent getTrackerEventById(Long id){
        return trackerEventRepository.getTrackerEventById(id).orElseThrow(() -> new NotFoundException("TrackerEvent with id: " + id + " not found"));
    }
    TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent){
        return trackerEventRepository.saveTrackerEvent(trackerEvent);
    }

    void deleteTrackerEventById(Long id){
        TrackerEvent toDelete = getTrackerEventById(id);
        trackerEventRepository.deleteTrackerEvent(toDelete);
    }
}
