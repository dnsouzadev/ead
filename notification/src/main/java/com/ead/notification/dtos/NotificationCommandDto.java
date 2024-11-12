package com.ead.notification.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
public class NotificationCommandDto {

    private String title;
    private String message;
    private UUID userId;

}
