package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.exceptions.UnauthorizedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrackerEventService {
    private final TrackerEventRepository trackerEventRepository;

    public TrackerEventService(TrackerEventRepository trackerEventRepository) {
        this.trackerEventRepository = trackerEventRepository;
    }
    Page<TrackerEvent> getAllTrackerEventsPagedAndFiltered(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, int page, int size, String sort){
        List<Sort.Order> orders = new ArrayList<>();
        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));

        return trackerEventRepository.getAllTrackerEventsPagedAndFiltered(description, projectsIds, clientsIds, tasksIds, duration, date, usersIds, pageable);
    }

    List<TrackerEvent> getAllTrackerEventsFilteredAndSorted(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, String sort){
        List<Sort.Order> orders = new ArrayList<>();
        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

        return trackerEventRepository.getAllTrackerEventsFilteredAndSorted(description, projectsIds, clientsIds, tasksIds, duration, date, usersIds, Sort.by(orders));
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

    public List<TrackerEvent> getAllTrackerEventsForCurrentUserByDate(LocalDateTime date) {
        UserDetails userDetails;
        try{
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            throw new UnauthorizedException("Unauthorized");
        }

        return trackerEventRepository.getAllTrackerEventsFilteredByUsernameAndDate(userDetails.getUsername(), date);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
