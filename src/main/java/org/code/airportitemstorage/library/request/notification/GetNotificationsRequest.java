package org.code.airportitemstorage.library.request.notification;

import lombok.Data;

@Data
public class GetNotificationsRequest {
    public String title;

    public String author;

    public boolean queryHasPublished;

    public long pageIndex = 1;

    public long pageSize = 15;
}
