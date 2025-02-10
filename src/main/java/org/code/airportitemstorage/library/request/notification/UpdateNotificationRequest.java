package org.code.airportitemstorage.library.request.notification;

import lombok.Data;

@Data
public class UpdateNotificationRequest {
    public long id;

    public String title;

    public String content;
}

