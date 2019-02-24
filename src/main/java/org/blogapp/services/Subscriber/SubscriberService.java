package org.blogapp.services.Subscriber;

import lombok.NoArgsConstructor;
import org.blogapp.model.Subscriber;
import org.blogapp.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@NoArgsConstructor
public class SubscriberService implements SubscriberInterface {
    @Autowired
    private SubscriberRepository repository;
    @Override
    public Subscriber findSubscriberById(int id) {
        return repository.findSubscriberById(id);
    }

    @Override
    public Subscriber findSubscriberByEmail(String email) {
        return repository.findSubscriberByEmail(email);
    }

    @Override
    public List<Subscriber> findSubscribersByDate(Date date) {
        return repository.findSubscribersByDate(date);
    }

    @Override
    public void save(Subscriber subscriber) {
        repository.save(subscriber);
    }
}
