package br.com.nlw.events.controller;

import br.com.nlw.events.dto.ErrorMessage;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.exception.EventNotFoundException;
import br.com.nlw.events.exception.SubscriptionConflictException;
import br.com.nlw.events.exception.UserIndicadorNotFoundException;
import br.com.nlw.events.model.User;
import br.com.nlw.events.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    @PostMapping({"/{prettyName}", "/{prettyName}/{userId}"})
    public ResponseEntity<?> createSubscription(
            @PathVariable String prettyName,
            @RequestBody User subscriber,
            @PathVariable(required = false) Integer userId) {
        try {
            SubscriptionResponse res = service.createNewSubscription(prettyName, subscriber, userId);
            if (res != null) {
                return ResponseEntity.ok(res);
            }
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
        } catch (SubscriptionConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(ex.getMessage()));
        } catch (UserIndicadorNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }
}
