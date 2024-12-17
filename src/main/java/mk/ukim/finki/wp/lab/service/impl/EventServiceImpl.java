package mk.ukim.finki.wp.lab.service.impl;

import mk.ukim.finki.wp.lab.model.Event;
import mk.ukim.finki.wp.lab.model.Location;
import mk.ukim.finki.wp.lab.repository.EventRepository;
import mk.ukim.finki.wp.lab.repository.LocationRepository;
import mk.ukim.finki.wp.lab.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> searchEvents(String text) {
        return eventRepository.findByNameContaining(text);
    }

    @Override
    public List<Event> searchEventsWithRating(String text, String textDesc, double rating) {
        return eventRepository.findByNameContainingOrDescriptionContainingOrPopularityScoreGreaterThanEqual(text,textDesc, rating);
    }

    @Override
    public List<Event> filterByRating(double rating) {
        return eventRepository.findByPopularityScoreGreaterThanEqual(rating);
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public Event findById(Long id) throws Exception {
        return eventRepository.findById(id).orElseThrow(Exception::new);
    }

//    @Override
//    public void editEvents(Long eventId, Event event) {
//        Event existingEvent = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
//        existingEvent.setName(event.getName());
//        existingEvent.setDescription(event.getDescription());
//        existingEvent.setPopularityScore(event.getPopularityScore());
//        eventRepository.save(existingEvent);
//    }

    @Override
    public void addEvent(String name, String description, double popularityScore, Long locationId) throws Exception {

        Location location = locationRepository.findById(locationId).orElseThrow(() -> new Exception("Location not found"));


        Event event = new Event(name, description, popularityScore, location);


        eventRepository.save(event);
    }

    @Override
    public void editChanges(Long id, String name, String description, Double popularityScore, Long locationId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        event.setName(name);
        event.setDescription(description);
        event.setPopularityScore(popularityScore);
        event.setLocation(location);

        eventRepository.save(event);
    }

    @Override
    public void editEvent(Long eventId, Event event) {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            Event updatedEvent = existingEvent.get();
            updatedEvent.setName(event.getName());
            updatedEvent.setDescription(event.getDescription());
            updatedEvent.setPopularityScore(event.getPopularityScore());
            updatedEvent.setLocation(event.getLocation());
            eventRepository.save(updatedEvent);
        }
    }

}
