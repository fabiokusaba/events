package br.com.nlw.events.repository;

import br.com.nlw.events.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Integer> {
    // Definições de metodos e esses nomes de metodos são manipulados pela JPA e de acordo com o que a gente tiver ele
    // vai fazer um SQL correspondente
    Event findByPrettyName(String prettyName);
}
