package br.com.nlw.events.service;

import br.com.nlw.events.model.Event;
import br.com.nlw.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    // Injeção de dependência: busca uma implementação, cria uma instância dessa implementação e disponibiliza
    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event) {
        // Gerando o pretty name
        event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));

        // Gravando os dados
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        // Conversão simples
        return (List<Event>) eventRepository.findAll();
    }

    public Event getByPrettyName(String prettyName) {
        return eventRepository.findByPrettyName(prettyName);
    }
}
