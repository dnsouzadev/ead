package com.ead.notification.dtos;

import com.ead.notification.enums.NotificationStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NotificationDto {

    @NotNull
    private NotificationStatus notificationStatus;
}
