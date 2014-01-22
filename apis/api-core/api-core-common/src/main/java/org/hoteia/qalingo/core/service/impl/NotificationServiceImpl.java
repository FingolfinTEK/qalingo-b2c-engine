/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.7.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.hoteia.qalingo.core.dao.NotificationDao;
import org.hoteia.qalingo.core.domain.Notification;
import org.hoteia.qalingo.core.service.NotificationService;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    public Notification getNotificationById(final Long notificationId) {
        return notificationDao.getNotificationById(notificationId);
    }

    public Notification getNotificationById(final String rawNotificationId) {
        long notificationId = -1;
        try {
            notificationId = Long.parseLong(rawNotificationId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        return getNotificationById(notificationId);
    }

    public List<Notification> findNotifications() {
        return notificationDao.findNotifications();
    }

    public List<Notification> findNotificationByCustomerId(final String customerId) {
        Long id = new Long(customerId);
        return notificationDao.findNotificationByCustomerId(id);
    }

    public List<Notification> findNewNotificationByCustomerId(final String customerId) {
        Long id = new Long(customerId);
        return notificationDao.findNewNotificationByCustomerId(id);
    }

    public void flagAsReadAllNewNotification(final String customerId) {
        Long id = new Long(customerId);
        notificationDao.flagAsReadAllNewNotification(id);
    }

    public void saveOrUpdateNotification(final Notification notification) {
        notificationDao.saveOrUpdateNotification(notification);
    }

    public void deleteNotification(final Notification notification) {
        notificationDao.deleteNotification(notification);
    }

}