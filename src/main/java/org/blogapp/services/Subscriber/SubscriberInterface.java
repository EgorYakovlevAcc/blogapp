package org.blogapp.services.Subscriber;

import org.blogapp.model.Subscriber;

import java.util.Date;
import java.util.List;

public interface SubscriberInterface {
    Subscriber findSubscriberById(int id);
    Subscriber findSubscriberByEmail(String email);
    List<Subscriber> findSubscribersByDate (Date date);

    void save(Subscriber subscriber);
}
