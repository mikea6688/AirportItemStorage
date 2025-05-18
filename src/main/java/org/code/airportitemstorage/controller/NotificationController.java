package org.code.airportitemstorage.controller;

import org.code.airportitemstorage.library.request.notification.AddNotificationRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsResponse;
import org.code.airportitemstorage.library.request.notification.UpdateNotificationRequest;
import org.code.airportitemstorage.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Async
    @PostMapping("add")
    public CompletableFuture<Integer> addNotification(@RequestBody AddNotificationRequest request) throws AuthenticationException {
        return notificationService.AddNotification(request);
    }

    @PostMapping("update")
    public int updateNotification(@RequestBody UpdateNotificationRequest request) throws AuthenticationException {
        return notificationService.UpdateNotification(request);
    }

    @PostMapping("delete")
    public int deleteNotification(@RequestBody long id) throws AuthenticationException {
        return notificationService.DeleteNotification(id);
    }

    @GetMapping("list")
    public GetNotificationsResponse getAllNotifications(GetNotificationsRequest request) throws AuthenticationException {
        return notificationService.GetAllNotifications(request);
    }

    @PostMapping("publish")
    public int publishNotification(@RequestBody long id) throws AuthenticationException {
        return notificationService.PublishNotification(id);
    }

    @PostMapping("unpublish")
    public int unpublishNotification(@RequestBody long id) throws AuthenticationException {
        return notificationService.UnPublishNotification(id);
    }
}
