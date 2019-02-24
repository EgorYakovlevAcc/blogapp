package org.blogapp.repository;

import org.blogapp.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    Subscriber findSubscriberById(int id);
    Subscriber findSubscriberByEmail(String email);
    List<Subscriber> findSubscribersByDate (Date date);
}
