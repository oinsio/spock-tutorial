package com.example;

import java.util.List;

public interface NotificationsFilter {

    List<Notification> filter(List<Notification> notifications, String senderId);

}
