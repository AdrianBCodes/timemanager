package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TrackerEventService {
    private final TrackerEventRepository trackerEventRepository;

    public TrackerEventService(TrackerEventRepository trackerEventRepository) {
        this.trackerEventRepository = trackerEventRepository;
    }
    Page<TrackerEvent> getAllTrackerEventsPaged(String description, List<Long> projectsIds, List<Long> tasksIds, Long duration, LocalDateTime date, List<Long> usersIds, int page, int size, String sort){
        QTrackerEvent trackerEvent = QTrackerEvent.trackerEvent;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(trackerEvent.description.contains(description));

        if(!projectsIds.isEmpty()){
            builder.and(trackerEvent.project.id.in(projectsIds));
        }
        if(!tasksIds.isEmpty()){
            builder.and(trackerEvent.task.id.in(tasksIds));
        }
        if(duration != null){
            builder.and(trackerEvent.duration.eq(duration));
        }
        if(date != null){
            builder.and(trackerEvent.date.eq(date));
        }
        if(!usersIds.isEmpty()){
            builder.and(trackerEvent.user.id.in(usersIds));
        }

        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));

        return trackerEventRepository.getAllTrackerEventsPaged(builder, pageable);
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
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QTrackerEvent trackerEvent = QTrackerEvent.trackerEvent;

        LocalDate localDate = date.toLocalDate();
        LocalDateTime startOfDate = localDate.atTime(LocalTime.MIN);
        LocalDateTime endOfDate = localDate.atTime(LocalTime.MAX);

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(trackerEvent.user.username.eq(userDetails.getUsername()));
        if(date != null){
            builder.and(trackerEvent.date.between(startOfDate, endOfDate));
        }

        return trackerEventRepository.getAllTrackerEvents(builder);
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
