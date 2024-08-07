package com.insyncwithfoo.pyrightls

import com.insyncwithfoo.pyrightls.icons.Icon
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction.createSimpleExpiring
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project


private const val ID = "Pyright LS notifications"
private val ICON = Icon.COLORED_SMALL


internal fun Notification.prettify() = apply {
    isImportant = false
    icon = ICON
}


internal fun Notification.addSimpleExpiringAction(text: String, action: () -> Unit) =
    addAction(createSimpleExpiring(text, action))


internal fun Notification.runThenNotify(project: Project, action: Notification.() -> Unit) {
    run(action)
    notify(project)
}


internal fun pyrightNotificationGroup(): NotificationGroup {
    val groupManager = NotificationGroupManager.getInstance()
    return groupManager.getNotificationGroup(ID)
}
