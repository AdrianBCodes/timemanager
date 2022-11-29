package com.adrianbcodes.timemanager.trackerEvent;

import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FakeTrackerEventRepository implements TrackerEventRepository{
    private final Map<Long, TrackerEvent> trackerEvents = new HashMap<>();

    @Override
    public Page<TrackerEvent> getAllTrackerEventsPagedAndFiltered(String description,
                                                                  List<Long> projectsIds,
                                                                  List<Long> clientsIds,
                                                                  List<Long> tasksIds,
                                                                  Long duration,
                                                                  String date,
                                                                  List<Long> usersIds,
                                                                  Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime formattedDate = LocalDateTime.parse(date, formatter);

        List<TrackerEvent> filteredTrackerEvents = trackerEvents.values()
                .stream()
                .filter(trackerEvent ->
                        trackerEvent.getDescription().contains(description) &&
                        projectsIds.contains(trackerEvent.getProject().getId()) &&
                        clientsIds.contains(trackerEvent.getProject().getClient().getId()) &&
                        trackerEvent.getProject().getTasks().stream().anyMatch(task -> tasksIds.contains(task.getId())) &&
                        trackerEvent.getDuration().equals(duration) &&
                        trackerEvent.getDate().equals(formattedDate) &&
                        usersIds.contains(trackerEvent.getUser().getId()))
                .toList();
        return new PageImpl<>(filteredTrackerEvents, pageable, filteredTrackerEvents.size());
    }

    @Override
    public List<TrackerEvent> getAllTrackerEventsFilteredByUsernameAndDate(String username, LocalDateTime date) {
        return trackerEvents.values()
                .stream()
                .filter(trackerEvent ->
                        trackerEvent.getUser().getUsername().equals(username) &&
                        trackerEvent.getDate().equals(date))
                .toList();
    }


    @Override
    public List<TrackerEvent> getAllTrackerEventsFilteredAndSorted(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, Sort sort) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime formattedDate = LocalDateTime.parse(date, formatter);
        List<TrackerEvent> filteredTrackerEvents = trackerEvents.values()
                .stream()
                .filter(trackerEvent ->
                        trackerEvent.getDescription().contains(description) &&
                                projectsIds.contains(trackerEvent.getProject().getId()) &&
                                clientsIds.contains(trackerEvent.getProject().getClient().getId()) &&
                                trackerEvent.getProject().getTasks().stream().anyMatch(task -> tasksIds.contains(task.getId())) &&
                                trackerEvent.getDuration().equals(duration) &&
                                trackerEvent.getDate().equals(formattedDate) &&
                                usersIds.contains(trackerEvent.getUser().getId()))
                .toList();
        // TODO - Better sorting
        Pageable pageable = PageRequest.of(1, filteredTrackerEvents.size(), sort);
        Page<TrackerEvent> page = new PageImpl<>(filteredTrackerEvents, pageable, filteredTrackerEvents.size());
        return page.getContent();
    }

    @Override
    public Optional<TrackerEvent> getTrackerEventById(Long id) {
        return Optional.ofNullable(trackerEvents.get(id));
    }

    @Override
    public TrackerEvent saveTrackerEvent(TrackerEvent trackerEvent) {
        // TODO Generate ID
        trackerEvents.put(trackerEvent.getId(), trackerEvent);
        return trackerEvents.get(trackerEvent.getId());
    }

    @Override
    public void deleteTrackerEvent(TrackerEvent trackerEvent) {
        trackerEvents.remove(trackerEvent.getId());
    }
}
