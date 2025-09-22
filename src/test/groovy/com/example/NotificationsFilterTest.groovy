package com.example

import spock.lang.Shared
import spock.lang.Specification


class NotificationsFilterTest extends Specification {

    @Shared
    def userId = "7b677549-39be-4d0b-b66a-7232008d8414"

    // optimize userPreferenceService calls to not get the user's preferences twice
    def "should not call user preference service twice for the same userId"() {

        given: "the user's preferences allows all notification types"
            def userPreferenceService = Mock(UserPreferenceService)
            UserPreference userPreference = new UserPreference(Set.of(), Set.of(NotificationType.SMS, NotificationType.PUSH, NotificationType.EMAIL))
        and: "sent notifications"
            def notificationSentService = Mock(NotificationSentService)
            notificationSentService.getSentIds(_ as Set<String>) >> Set.of()
        and:
            def filter = new NotificationsFilterImpl(userPreferenceService, notificationSentService)
            def notificationList = generateNotifications(10)

        when:
            def filteredNotifications = filter.filter(notificationList, "Some-sender_id")

        then:
            filteredNotifications.size() == 10
            1 * userPreferenceService.getUserPreferences(userId) >> userPreference
    }

    // Сделать проверку была ли уже отправка этого уведомления или ещё нет. Если была, то убрать уведомление из списка для отправления
    def "should filter notifications by message id"() {

        given: "the user's preferences"
            def userPreferenceService = Mock(UserPreferenceService)
            UserPreference userPreference = new UserPreference(Set.of(), Set.of(NotificationType.SMS, NotificationType.PUSH, NotificationType.EMAIL))
            userPreferenceService.getUserPreferences(userId) >> userPreference
        and: "sent notifications"
            def notificationSentService = Mock(NotificationSentService)
            notificationSentService.getSentIds(_ as Set<String>) >> sentIds
        and:
            def filter = new NotificationsFilterImpl(userPreferenceService, notificationSentService)
            def notificationList = generateNotifications(10)

        when:
            def filteredNotifications = filter.filter(notificationList, "Some-sender_id")

        then:
            filteredNotifications.size() == filteredNotificationAmount

        where:
            sentIds                                                            || filteredNotificationAmount
            Set.of("notificationId-1", "notificationId-3", "notificationId-5") || 7
            Set.of("notificationId-0", "notificationId-9")                     || 8
            Set.of("notificationId-", "notificationId-19")                     || 10
    }

    // Отфильтровать уведомления по предпочтению пользователю
    def "should return notifications based on user preferences"() {

        given: "the user's preferences"
            def userPreferenceService = Mock(UserPreferenceService)
            UserPreference userPreference = new UserPreference(blockedSenders, preferedNotificationTypes)
            userPreferenceService.getUserPreferences(userId) >> userPreference
        and: "sent notifications"
            def notificationSentService = Mock(NotificationSentService)
            notificationSentService.getSentIds(_ as Set<String>) >> Set.of()
        and:
            def filter = new NotificationsFilterImpl(userPreferenceService, notificationSentService)
            def notificationList = generateNotifications(10)

        when:
            def filteredNotifications = filter.filter(notificationList, senderId)

        then:
            filteredNotifications.size() == filteredNotificationAmount

        where:
            blockedSenders               | preferedNotificationTypes                                                   | senderId      || filteredNotificationAmount
            Set.of("sender1", "sender2") | Set.of(NotificationType.SMS, NotificationType.PUSH)                         | "some-sender" || 0
            Set.of("some-sender")        | Set.of(NotificationType.SMS, NotificationType.PUSH, NotificationType.EMAIL) | "some-sender" || 0
            Set.of("sender1")            | Set.of(NotificationType.SMS, NotificationType.PUSH, NotificationType.EMAIL) | "some-sender" || 10
    }

    // Предоставить интерфейс для получения списка уведомлений + id отправителя (один отправитель для всего списка уведомлений)
    def "should return notifications list as is"() {

        given: "the user allows all notification types from all senders"
            UserPreference userPreference = new UserPreference(Set.of(), Set.of(NotificationType.SMS, NotificationType.PUSH, NotificationType.EMAIL))
            def userPreferenceService = Mock(UserPreferenceService)
            userPreferenceService.getUserPreferences(userId) >> userPreference
        and: "sent notifications"
            def notificationSentService = Mock(NotificationSentService)
            notificationSentService.getSentIds(_ as Set<String>) >> Set.of()
        and:
            def filter = new NotificationsFilterImpl(userPreferenceService, notificationSentService)
            def notificationList = generateNotifications(5)
            def senderId = "Some-sender_id"

        when:
            def filteredNotifications = filter.filter(notificationList, senderId)

        then:
            filteredNotifications == notificationList
    }

    def generateNotifications(int amount) {
        List<Notification> notifications = new ArrayList<>(amount)

        for (int i = 0; i < amount; i++) {
            notifications.add(new Notification("notificationId-" + i, NotificationType.EMAIL, userId, "message index " + i))
        }

        return notifications
    }

}