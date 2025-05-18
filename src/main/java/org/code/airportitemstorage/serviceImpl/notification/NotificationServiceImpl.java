package org.code.airportitemstorage.serviceImpl.notification;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.code.airportitemstorage.library.dto.notification.NotificationDto;
import org.code.airportitemstorage.library.entity.notification.Notification;
import org.code.airportitemstorage.library.entity.user.User;
import org.code.airportitemstorage.library.request.notification.AddNotificationRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsRequest;
import org.code.airportitemstorage.library.request.notification.GetNotificationsResponse;
import org.code.airportitemstorage.library.request.notification.UpdateNotificationRequest;
import org.code.airportitemstorage.mapper.notification.NotificationMapper;
import org.code.airportitemstorage.service.notification.NotificationService;
import org.code.airportitemstorage.service.user.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    @Override
    public CompletableFuture<Integer> AddNotification(AddNotificationRequest request) {
        try {
//            var user = userService.CheckUserAuthorization();
//            if (user == null) throw new AuthenticationException();

            Notification notification = new Notification();
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());
            notification.setDefaultCommentDate();
            notification.setAuthor("USER");

            int result = notificationMapper.insert(notification);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            // 异常处理，可以自定义异常包装返回异常信息给前端等需求场景下处理更细致的逻辑。
            CompletableFuture<Integer> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public int UpdateNotification(UpdateNotificationRequest request) throws AuthenticationException {
        var user = userService.CheckUserAuthorization();

        if(user == null) throw new AuthenticationException();

        Notification notification = notificationMapper.selectById(request.id);

        if(notification == null)return 0;

        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());

        return notificationMapper.updateById(notification);
    }

    @Override
    public int DeleteNotification(long id) throws AuthenticationException {
        var user = userService.CheckUserAuthorization();

        if(user == null) throw new AuthenticationException();

        Notification notification = notificationMapper.selectById(id);

        if(notification == null)return 0;

        return notificationMapper.deleteById(id);
    }

    @Override
    public GetNotificationsResponse GetAllNotifications(GetNotificationsRequest request) throws AuthenticationException {
        var user = userService.CheckUserAuthorization();
        if (user == null) throw new AuthenticationException();

        var queryWrapper = buildQueryWrapper(request);
        var notificationList = new ArrayList<NotificationDto>();

        if (request.queryHasPublished) {
            notificationMapper.selectList(queryWrapper).forEach(notification ->
                    notificationList.add(convertToDto(notification, user))
            );
            return new GetNotificationsResponse(notificationList, notificationList.size());
        }

        // 分页查询
        var page = new Page<Notification>(request.pageIndex, request.pageSize);
        Page<Notification> notificationPage = notificationMapper.selectPage(page, queryWrapper);

        notificationPage.getRecords().forEach(notification ->
                notificationList.add(convertToDto(notification, user))
        );

        return new GetNotificationsResponse(notificationList, notificationPage.getTotal());
    }

    @Override
    public int PublishNotification(long id) throws AuthenticationException {
        var user = userService.CheckUserAuthorization();

        if(user == null) throw new AuthenticationException();

        Notification notification = notificationMapper.selectById(id);

        if(notification == null || notification.publish) throw new AuthenticationException();

        notification.setPublish(true);

        return notificationMapper.updateById(notification);
    }

    @Override
    public int UnPublishNotification(long id) throws AuthenticationException {
        var user = userService.CheckUserAuthorization();

        if(user == null) throw new AuthenticationException();

        Notification notification = notificationMapper.selectById(id);

        if(notification == null || !notification.publish) return 0;

        notification.setPublish(false);

        return notificationMapper.updateById(notification);
    }

    private QueryWrapper<Notification> buildQueryWrapper(GetNotificationsRequest request) {
        var queryWrapper = new QueryWrapper<Notification>();

        if ((request.author != null && !request.getAuthor().isEmpty()) || (request.title != null && !request.getTitle().isEmpty())) {
            queryWrapper.eq("author", request.getAuthor()).or().eq("title", request.getTitle());
        }

        if (request.queryHasPublished) {
            queryWrapper.eq("publish", true);
        }

        return queryWrapper;
    }

    private NotificationDto convertToDto(Notification notification, User user) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setAuthor(user.getAccountName());
        dto.setCreateTime(notification.getCreateTime());
        dto.setPublish(notification.isPublish());
        return dto;
    }
}
