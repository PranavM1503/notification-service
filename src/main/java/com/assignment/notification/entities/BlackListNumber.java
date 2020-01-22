package com.assignment.notification.entities;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blacklist_numbers")
public class BlackListNumber {

    @Id
//    @Column(name="phone_number",unique=true,nullable=false)
    private String phone_number;
}
