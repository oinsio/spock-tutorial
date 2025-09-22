package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NotificationsFilterImpl implements NotificationsFilter {

    UserPreferenceService userPreferenceService;
    NotificationSentService notificationSentService;

    public NotificationsFilterImpl(UserPreferenceService userPreferenceService,
                                   NotificationSentService notificationSentService) {
        this.userPreferenceService = userPreferenceService;
        this.notificationSentService = notificationSentService;
    }

    @Override
    public List<Notification> filter(List<Notification> notifications, String senderId) {

        List<Notification> filteredNotifications = new ArrayList<>();
        HashMap<String, UserPreference> userPreferencesCache = new HashMap<>();
        UserPreference userPreference;

        Set<String> notificationIds = notifications.stream().map(Notification::id).collect(Collectors.toSet());
        Set<String> sentIds = notificationSentService.getSentIds(notificationIds);

        for (Notification notification : notifications) {

            // optimize userPreferenceService calls to not get the user's preferences twice
            String userId = notification.userId();
            if (userPreferencesCache.containsKey(userId)) {
                userPreference = userPreferencesCache.get(userId);
            } else {
                userPreference = userPreferenceService.getUserPreferences(userId);
                userPreferencesCache.put(userId, userPreference);
            }

            if (!userPreference.blockedSenderIds().contains(senderId)
                    && !sentIds.contains(notification.id())
                    && userPreference.preferredNotificationTypes().contains(notification.type())) {
                filteredNotifications.add(notification);
            }
        }

        return filteredNotifications;

    }
}
