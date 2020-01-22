package com.assignment.notification.repositories;

import com.assignment.notification.entities.SmsRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface smsRequestRepository extends CrudRepository <SmsRequest, Integer>{

}
