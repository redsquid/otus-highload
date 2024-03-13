package org.example.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Advertisement {

    UUID accountId;

    String text;
}
