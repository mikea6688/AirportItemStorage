package org.code.airportitemstorage.service.notification;

import org.code.airportitemstorage.library.request.notification.AddNotificationRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsResponse;
import org.code.airportitemstorage.library.request.notification.UpdateNotificationRequest;

import javax.naming.AuthenticationException;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    CompletableFuture<Integer> AddNotification(AddNotificationRequest request) throws AuthenticationException;

    int UpdateNotification(UpdateNotificationRequest request) throws AuthenticationException;

    int DeleteNotification(long id) throws AuthenticationException;

    GetNotificationsResponse GetAllNotifications(GetNotificationsRequest request) throws AuthenticationException;

    int PublishNotification(long id) throws AuthenticationException;

    int UnPublishNotification(long id) throws AuthenticationException;
}
