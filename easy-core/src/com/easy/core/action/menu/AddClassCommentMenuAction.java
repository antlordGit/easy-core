package com.easy.core.action.menu;

import com.easy.core.service.AddClassCommentMenuService;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.impl.NotificationsManagerImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.ui.BalloonLayoutData;
import com.intellij.ui.awt.RelativePoint;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class AddClassCommentMenuAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AddClassCommentMenuService service = ServiceManager.getService(event.getProject(), AddClassCommentMenuService.class);
        service.actionPerformed(event);
//        Pair var3 = BalloonTools.INSTANCE.fetchBalloonParameters(event.getProject());
//        AlarmDebouncer<AnActionEvent> debouncer = new AlarmDebouncer<>(250);
//        event.getProject().getMessageBus().syncPublisher(new Topic<MyDynamicPluginListener>("name",MyDynamicPluginListener.class)).beforePluginLoaded();
//        debouncer.debounce(()->{});
//
//        debouncer.debounce {
//            project.messageBus
//                    .syncPublisher(EVENT_TOPIC)
//                    .onDispatch(
//                            UserEvent(
//                                    UserEvents.ON_DEMAND,
//                                    UserEventCategory.NEUTRAL,
//                                    PluginMessageBundle.message("user.event.on-demand.name"),
//                                    project
//                            )
//                    )
//        }
//        Alarm alarm = new Alarm();
//        NotificationGroup notificationGroup = NotificationGroup.balloonGroup("AMII - Rider Extension Updates", "222222");
//        Notification notification = notificationGroup.createNotification();
//        notificationGroup.createNotification();
//        notification.setTitle("11111111111");
//        notification.setContent("3333333333333333");
//        notification.setIcon(new ImageIcon("E:\\111111.png"));
//        notification.setSubtitle("222222222222");
//        notification.addAction(new AnAction("HHHHHH") {
//            @Override
//            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//                Messages.showInfoMessage("5555555", "6666666");
//            }
//        });
//        notification.setContextHelpAction(new AnAction("hello1111111") {
//            @Override
//            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
//                Messages.showInfoMessage("5555555", "6666666");
//            }
//        });
//        notification.setListener(new NotificationListener() {
//            @Override
//            public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent hyperlinkEvent) {
//                Messages.showInfoMessage("5555555", "6666666");
//            }
//        });
//        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(event.getProject());
//        Balloon var7 = NotificationsManagerImpl.createBalloon(ideFrame, notification, false, false, BalloonLayoutData.fullContent(), Disposer.newDisposable());
//        RelativePoint notificationPosition = new RelativePoint((Component) ideFrame.getComponent(), new Point(1000, 200));
//        MouseEvent mouseEvent = notificationPosition.toMouseEvent();
//        mouseEvent.getXOnScreen();
//        var7.setBounds(new Rectangle(new Point(1000, 200), new Dimension(500, 500)));
//        var7.setAnimationEnabled(true);
//        var7.setTitle("77777777777");
//        var7.show(notificationPosition, Balloon.Position.atLeft);
//        Intrinsics.checkNotNullParameter(project, "project");

//        Intrinsics.checkNotNullParameter(project, "project");
//        IdeFrame var2 = WindowManager.getInstance().getIdeFrame(project);
//        IdeFrame var10000;
//        if (var2 == null) {
//            IdeFrame[] var3 = WindowManager.getInstance().getAllProjectFrames();
//            Intrinsics.checkNotNullExpressionValue(var3, "getInstance().allProjectFrames");
//            var10000 = (IdeFrame)ArraysKt.first((Object[])var3);
//        } else {
//            var10000 = var2;
//        }
//        IdeFrame ideFrame = var10000;
//        Rectangle frameBounds = ideFrame.getComponent().getBounds();
//        RelativePoint notificationPosition = new RelativePoint((Component)ideFrame.getComponent(), new Point(frameBounds.x + frameBounds.width, 20));
//        Pair var3 = Pair(ideFrame, notificationPosition);
//
//        IdeFrame ideFrame = (IdeFrame)var3.component1();
//        RelativePoint notificationPosition = (RelativePoint)var3.component2();
//
//
//        NotificationGroup notificationGroup = new NotificationGroup("AMII - Rider Extension Updates", NotificationDisplayType.STICKY_BALLOON, false, "AMII - Rider Extension Updates", (Icon) null, 16, (DefaultConstructorMarker) null);
//        String var10001 = Intrinsics.stringPlus("AMII - Rider Extension updated to v", newVersion);
//        Notification var4 = NotificationGroup.createNotification$default(notificationGroup, var10001, UpdateNotificationKt.access$buildUpdateMessage((String)var5), (NotificationType)null, (NotificationListener)null, 12, (Object)null).setIcon(AMIIRiderIcons.PLUGIN_ICON).setListener((NotificationListener)(new UrlOpeningListener(false)));



//        Balloon var7 = NotificationsManagerImpl.createBalloon(ideFrame, updateNotification, true, false, BalloonLayoutData.fullContent(), Disposer.newDisposable());
//        Intrinsics.checkNotNullExpressionValue(var7, "createBalloon(\n        ideFrame,\n        updateNotification,\n        true,\n        false,\n        BalloonLayoutData.fullContent(),\n        Disposer.newDisposable()\n      )");
//        var7.show(notificationPosition, Position.atLeft);

//        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("=========");
//            }
//        });
//        ((UserEventListener)event.getProject().getMessageBus().syncPublisher(UserEventsKt.getEVENT_TOPIC())).onDispatch(new UserEvent(UserEvents.ON_DEMAND, UserEventCategory.NEUTRAL, PluginMessageBundle.message("user.event.on-demand.name", new Object[0]), event.getProject()));
//        event.getProject().getMessageBus().syncPublisher(new Topic(UserEventListener.class))
//        System.out.println("===========");

//        myUpdateMerger = new MergingUpdateQueue("EditorNotifications update merger", 100, true, null, project);
//        MessageBusConnection connection = event.getProject().getMessageBus().connect(event.getProject());
//        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileEditorManagerAdapter() {
//            @Override
//            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
////                updateNotifications(file);
//                System.out.println("--===");
//            }
//        });
//        connection.subscribe(DumbService.DUMB_MODE, new DumbService.DumbModeListener() {
//            @Override
//            public void enteredDumbMode() {
////                updateAllNotifications();
//                System.out.println("d--d-d--");
//            }
//
//            @Override
//            public void exitDumbMode() {
//                System.out.println("d--d-d--");
//            }
//        });

// event.getProject().getMessageBus().syncPublisher(UserEventsKt.getEVENT_TOPIC())).onDispatch(new UserEvent(UserEvents.ON_DEMAND, UserEventCategory.NEUTRAL, PluginMessageBundle.message("user.event.on-demand.name", new Object[0]), project));
//
//
//        Alarm coolDownAlarm = new Alarm();
//        coolDownAlarm.addRequest(() -> {
//            System.out.println("============");
//        }, 1);

//        BrowserUtil.browse(h.getURL());

//        MyDialogWrapper wrapper = new MyDialogWrapper(event.getProject(), true);
//        wrapper.setSize(500, 500);
//        wrapper.setLocation(500, 500);
//        wrapper.setTitle("99999999999");
//        wrapper.show();

//        Intrinsics.checkNotNullParameter(event.getProject(), "project");
////        Intrinsics.checkNotNullParameter(newVersion, "newVersion");
////        NotificationGroup var10000 = new NotificationGroup("AMII Updates", NotificationDisplayType.STICKY_BALLOON, false, "AMII Updates", (Icon)null, "ddd",null);
////        String var10001 = "AMII updated to v";
////        Object var10002 = VisualAssetDefinitionService.INSTANCE.getRandomAssetByCategory(MemeAssetCategory.HAPPY).map(null).orElseGet(null);
////        Intrinsics.checkNotNullExpressionValue(var10002, "VisualAssetDefinitionSer…elebration.gif\"\n        }");
////        Notification var4 = NotificationGroup.createNotification$default(var10000, var10001, UpdateNotificationKt.access$buildUpdateMessage((String)var10002), (NotificationType)null, (NotificationListener)null, 12, (Object)null).setIcon(AMIIIcons.PLUGIN_ICON).setListener((NotificationListener)(new UrlOpeningListener(false)));
//
//        NotificationGroup notificationGroup = NotificationGroup.balloonGroup("AMII - Rider Extension Updates", "222222");
////        NotificationGroup notificationGroup = NotificationGroup.balloonGroup("AMII Updates", NotificationDisplayType.STICKY_BALLOON, false, "AMII Updates", (Icon)null, 16, (DefaultConstructorMarker)null);
//        Notification var4 = notificationGroup.createNotification();
//
//        Intrinsics.checkNotNullExpressionValue(var4, "notificationGroup.create…rlOpeningListener(false))");
//        Notification updateNotification = var4;
//        showNotification(event.getProject(), updateNotification);

//        NotificationGroup.balloonGroup()
//        Pair var5 = BalloonTools.INSTANCE.fetchBalloonParameters(event.getProject());
//        Pair var5 = BalloonTools.INSTANCE.fetchBalloonParameters(event.getProject());
//        IdeFrame ideFrame = (IdeFrame) var5.component1();
//        NotificationGroup notificationGroup = NotificationGroup.balloonGroup("AMII - Rider Extension Updates", PluginId.findId("com.your.antlord.easy-core.plugin.id"));
////        Notification var4 = notificationGroup.createNotification();
//        Notification var4 = notificationGroup.createNotification();
////        var4.setIcon(new ImageIcon("E:\\1.png"));
//        ImageIcon imageIcon = new ImageIcon("E:\\5.png");
//        var4.setIcon(new ImageIcon("E:\\5.png"));
//        var4.setTitle("333");
//        var4.setContent("444");
//        Balloon balloon = var4.getBalloon();
//        int iconWidth = imageIcon.getIconWidth();
//        int iconHeight = imageIcon.getIconHeight();
//        var4.setContent("<html>\n" +
//                "<img src=\"https://img1.baidu.com/it/u=1390953780,3279318409&fm=15&fmt=auto\" width=\"" + iconWidth + "\" height=\"" + iconHeight + "\"/>" +
//                "</body>\n" +
//                "</html>");
//        var4.setImportant(false);
//
//        NotificationGroup balloonNotifications = new NotificationGroup("Notification group", NotificationDisplayType.BALLOON, true);
//
//        Notification success = balloonNotifications.createNotification(
//
//                "HighlightBracketPair is updated to ",
//
//        "<br/>If this plugin helps you, please give me a star on " +
////                "<img src=\"E:\\\\1.png\"/>\n" +
//                "<img src=\".\\/1.png\"/>\n" +
//                "<b><a href=\"https://github.com/qeesung/HighlightBracketPair\">Github</a>, ^_^.</b>",
//
//                NotificationType.INFORMATION,
//
//                new NotificationListener.UrlOpeningListener(true));
//
//        JLabel jLabel = new JLabel();
//        jLabel.setText("777777777777777777");
//        success.setBalloon();
//        Notifications.Bus.notify(success, event.getProject());


//        RelativePoint notificationPosition = (RelativePoint)var5.component2();
//        Balloon var10000 = NotificationsManagerImpl.createBalloon(ideFrame, var4, true, true, BalloonLayoutData.fullContent(), Disposer.newDisposable());
//        var4.getBalloon().show(notificationPosition, Balloon.Position.atLeft);
//        Balloon var10000 = NotificationsManagerImpl.createBalloon(ideFrame, var4, true, true, BalloonLayoutData.fullContent(), Disposer.newDisposable("hello"));
//        BalloonImpl balloon = (BalloonImpl) var10000;
//        var10000.setTitle("99999999999999");
//        var10000.setAnimationEnabled(false);
//        var10000.setBounds(new Rectangle(new Dimension(500, 500)));
//        var10000.setBounds(new Rectangle(new Point(500, 300), new Dimension(iconWidth, iconHeight)));

//        JLabel jLabel = new JLabel();
//        jLabel.setText("<html>\n" +
//                "<body>\n" +
//                "<img src=\"./1.png\"/>\n" +
//                "</body>\n" +
//                "</html>");
//        var10000.showInCenterOf(jLabel);
//        ((BalloonImpl) var10000).setShowPointer(false);
//        ((BalloonImpl) var10000).setTitle("888");
//        var10000.show(notificationPosition, Balloon.Position.atLeft);
//        JLayeredPane jLayeredPane = new JLayeredPane();
//        jLayeredPane.add(new JLabel("888888888"));
//        var10000.show(jLayeredPane);
//        notificationPosition.notify();
//        balloon.show(notificationPosition, Balloon.Position.above);
//        JPanel content = new NonOpaquePanel(new BorderLayout());
//        JLabel jLabel = new JLabel();
//        jLabel.setText("6666666666666");
//        ImageIcon imageIcon1 = new ImageIcon("E:\\5.png");
//        jLabel.setIcon(new ImageIcon("E:\\5.png"));
//        jLabel.setHorizontalAlignment(0);
//        jLabel.setVerticalAlignment(0);
//        jLabel.setBounds(0, 0, imageIcon1.getIconWidth() + 100, imageIcon1.getIconHeight() + 100);
//        jLabel.setBackground(new Color(0, 255, 0));
//        content.add(jLabel);
//        BalloonBuilder builder = JBPopupFactory.getInstance().createBalloonBuilder(jLabel);
////        builder = builder.setFadeoutTime(10);
//        builder = builder.setPositionChangeXShift(0);
//        builder.setShadow(false);
//        builder.setFillColor(new Color(255, 0, 0));
//        BalloonImpl balloon2 = (BalloonImpl)builder.createBalloon();
//        balloon2.setBounds(new Rectangle(new Point(100, 100), new Dimension(imageIcon1.getIconWidth(), imageIcon1.getIconHeight())));
//
//        balloon2.show(notificationPosition, Balloon.Position.atLeft);


    }

//    private final void showNotification(Project project, Notification updateNotification) {
//        try {
//            Pair var5 = BalloonTools.INSTANCE.fetchBalloonParameters(project);
//            IdeFrame ideFrame = (IdeFrame)var5.component1();
//            RelativePoint notificationPosition = (RelativePoint)var5.component2();
//            Balloon var10000 = NotificationsManagerImpl.createBalloon(ideFrame, updateNotification, false, true, BalloonLayoutData.fullContent(), Disposer.newDisposable());
//            Intrinsics.checkNotNullExpressionValue(var10000, "NotificationsManagerImpl…r.newDisposable()\n      )");
//            Balloon balloon = var10000;
//            balloon.setBounds(new Rectangle(new Dimension(300,300)));
//            balloon.setAnimationEnabled(true);
//            JPanel jPanel = new JPanel();
//            JLabel jLabel = new JLabel();
//            jLabel.setText("8888888888888");
////            balloon.showInCenterOf(jLabel);
//            balloon.show(notificationPosition, Balloon.Position.atRight);
//
//        } catch (Throwable var6) {
//            updateNotification.notify(project);
//        }
//
//    }

    public static void main(String[] args) {
        System.out.println("[[[[");
    }
}
