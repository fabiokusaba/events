package br.com.nlw.events.service;

import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepository;
import br.com.nlw.events.repository.SubscriptionRepository;
import br.com.nlw.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {
        // Instânciando uma subscription
        Subscription subs = new Subscription();

        // Recuperar evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);

        // Verificando se o evento existe
        if (event == null) {
            throw new EventNotFoundException("Evento " + eventName + " não existe.");
        }

        // Recuperando usuário do banco
        User userRecuperado = userRepository.findByEmail(user.getEmail());

        if (userRecuperado == null) {
            // Gravando usuário no banco
            userRecuperado = userRepository.save(user);
        }

        // Se o userId passado como parâmetro para esse metodo for diferente de null, ou seja, existe alguém que
        // indicou, faço a busca no repositório, se na busca ele não exister lanço a exceção, porém se ele não existir
        // ignoro tudo isso e o indicador continua sendo vazio
        User indicador = null;

        // Verificando se o usuário indicador existe
        if (userId != null) {
            indicador = userRepository.findById(userId).orElse(null);
            if (indicador == null) {
                throw new UserIndicadorNotFoundException("Usuário " + userId + " indicador não existe.");
            }
        }

        // Atribuindo valores ao subscription
        subs.setEvent(event);
        subs.setSubscriber(userRecuperado);
        subs.setIndication(indicador);

        // Verificar se tem uma inscrição com esse usuário no evento
        Subscription tmpSub = subscriptionRepository.findByEventAndSubscriber(event, userRecuperado);
        if (tmpSub != null) {
            throw new SubscriptionConflictException("Já existe inscrição para o usuário " + userRecuperado.getName() +
                    " no Evento " + event.getTitle());
        }

        // Gravando os dados
        Subscription res = subscriptionRepository.save(subs);

        return new SubscriptionResponse(res.getSubscriptionNumber(), "http://codecraft.com/subscription" +
                res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        // Verificando se o evento existe
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFoundException("Ranking do evento " + prettyName + " não existe.");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }
}
