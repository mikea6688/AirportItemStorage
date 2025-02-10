package org.code.airportitemstorage.library.request.notification;

import org.code.airportitemstorage.library.dto.notification.NotificationDto;

import java.util.List;

public class GetNotificationsResponse {
    public GetNotificationsResponse(List<NotificationDto> notifications, long total) {
        this.notifications = notifications;
        this.total = total;
    }

    public List<NotificationDto> notifications;

    public long total;
}
