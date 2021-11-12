package ggc.notifications;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Serializable;

/**
 * A notification method which registers the notifications on the application.
 */
public class NotificationRegister implements NotificationMethod, Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111101133L;

  /** Notifications registered, by observer. */
  private final HashMap<Observer, ArrayList<Notification>> _notifications;

  /**
   * Default constructor.
   */
  public NotificationRegister() {
    this._notifications = new HashMap<Observer, ArrayList<Notification>>();
  }

  /**
   * Gets the notifications registered for a specific observer, and cleans them.
   * 
   * @param observer The observer.
   * @return The notifications registered for the observer.
   */
  public Collection<Notification> popNotifications(Observer observer) {
    ArrayList<Notification> notifs = this.getNotifications(observer);
    this._notifications.remove(observer);
    return notifs;
  }

  /**
   * Gets the notifications registered for a specific observer, and cleans them.
   * 
   * @param observer The observer.
   * @return The notifications registered for the observer.
   */
  private ArrayList<Notification> getNotifications(Observer observer) {
    ArrayList<Notification> notifs = this._notifications.get(observer);

    if (notifs == null) {
      notifs = new ArrayList<Notification>();
      this._notifications.put(observer, notifs);
    }

    return notifs;
  }

  @Override
  public void send(Observer observer, Notification notification) {
    this.getNotifications(observer).add(notification);
  }
}
