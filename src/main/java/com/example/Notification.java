package com.example;

/**
 * Уведомление:
 * id - уведомления
 * type - тип уведомления (EMAIL, SMS, PUSH)
 * userId - получатель (id пользователя)
 * message - текст сообщения
 */
public record Notification(String id, NotificationType type, String userId, String message) {
}
