package com.adrianbcodes.timemanager.trackerEvent;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public interface SqlTrackerEventRepository extends TrackerEventRepository, JpaRepository<TrackerEvent, Long>, QuerydslPredicateExecutor {

    Page<TrackerEvent> findAll(Predicate predicate, Pageable pageable);
    List<TrackerEvent> findAll(Predicate predicate);
    List<TrackerEvent> findAll(Predicate predicate, Sort sort);

    @Override
    default Page<TrackerEvent> getAllTrackerEventsPagedAndFiltered(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, Pageable pageable){
        QTrackerEvent trackerEvent = QTrackerEvent.trackerEvent;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(trackerEvent.description.containsIgnoreCase(description));

        if(!projectsIds.isEmpty()){
            builder.and(trackerEvent.project.id.in(projectsIds));
        }
        if(!clientsIds.isEmpty()){
            builder.and(trackerEvent.project.client.id.in(clientsIds));
        }
        if(!tasksIds.isEmpty()){
            builder.and(trackerEvent.task.id.in(tasksIds));
        }
        if(duration != null){
            builder.and(trackerEvent.duration.eq(duration));
        }
        if(date != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            builder.and(trackerEvent.date.eq(localDateTime));
        }
        if(!usersIds.isEmpty()){
            builder.and(trackerEvent.user.id.in(usersIds));
        }
        return this.findAll(builder, pageable);
    }
    @Override
    default List<TrackerEvent> getAllTrackerEventsFilteredByUsernameAndDate(String username, LocalDateTime date) {
        QTrackerEvent trackerEvent = QTrackerEvent.trackerEvent;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(trackerEvent.user.username.eq(username));
        if(date != null){
            LocalDate localDate = date.toLocalDate();
            LocalDateTime startOfDate = localDate.atTime(LocalTime.MIN);
            builder.and(trackerEvent.date.eq(startOfDate));
        }
        return this.findAll(builder);
    }

    default List<TrackerEvent> getAllTrackerEventsFilteredAndSorted(String description, List<Long> projectsIds, List<Long> clientsIds, List<Long> tasksIds, Long duration, String date, List<Long> usersIds, Sort sort) {
        QTrackerEvent trackerEvent = QTrackerEvent.trackerEvent;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(trackerEvent.description.containsIgnoreCase(description));

        if(!projectsIds.isEmpty()){
            builder.and(trackerEvent.project.id.in(projectsIds));
        }
        if(!clientsIds.isEmpty()){
            builder.and(trackerEvent.project.client.id.in(clientsIds));
        }
        if(!tasksIds.isEmpty()){
            builder.and(trackerEvent.task.id.in(tasksIds));
        }
        if(duration != null){
            builder.and(trackerEvent.duration.eq(duration));
        }
        if(date != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            builder.and(trackerEvent.date.eq(localDateTime));
        }
        if(!usersIds.isEmpty()){
            builder.and(trackerEvent.user.id.in(usersIds));
        }
        return this.findAll(builder, sort);
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
