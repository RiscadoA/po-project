package ggc.notifications;

/**
 * Interface for notification methods.
 */
public interface NotificationMethod {
  /**
   * Sends a notification to an observer.
   * 
   * @param observer The observer to notify.
   * @param notification The notification to send.
   */
  public abstract void send(Observer observer, Notification notification);
}
