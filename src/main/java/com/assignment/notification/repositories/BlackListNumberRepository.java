package com.assignment.notification.repositories;

import com.assignment.notification.entities.BlackListNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BlackListNumberRepository extends CrudRepository <BlackListNumber,  String>  {
}
