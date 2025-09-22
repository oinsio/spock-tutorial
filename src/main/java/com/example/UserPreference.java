package com.example;

import java.util.Set;

/**
 * разрешенные каналы уведомлений (список типов) - @see com.example.NotificationType
 * заблокированные отправители (список id отправителей)
 */
public record UserPreference(Set<String> blockedSenderIds, Set<NotificationType> preferredNotificationTypes) {

}
