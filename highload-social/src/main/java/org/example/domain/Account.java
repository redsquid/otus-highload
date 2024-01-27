package org.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @JsonIgnoreProperties
    UUID id;

    String firstName;

    String lastName;

    LocalDate birthDate;

    Sex sex;

    String city;

    String interests;

    Integer rank;
}
