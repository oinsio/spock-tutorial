package com.example;

import java.util.Set;

public interface NotificationSentService {

    Set<String> getSentIds(Set<String> notificationIds);
}
