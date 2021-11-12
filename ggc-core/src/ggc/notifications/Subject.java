package ggc.notifications;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Observer design pattern pattern Subject implementation.
 */
public abstract class Subject implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111111057L;

  /** Observer set. */
  private HashSet<Observer> _observers;

  /** Notification method set. */
  private HashSet<NotificationMethod> _notificationMethods;

  /**
   * Default constructor.
   */
  public Subject() {
    this._observers = new HashSet<Observer>();
    this._notificationMethods = new HashSet<NotificationMethod>();
  }

  /**
   * Checks if the subject has an observer attached.
   * 
   * @param observer Observer to check.
   * @return true if the subject has the observer attached, false otherwise.
   */
  public boolean hasObserverAttached(Observer observer) {
    return this._observers.contains(observer);
  }

  /**
   * Attaches an observer to this subject.
   * 
   * @param observer The observer to attach.
   */
  public void attachObserver(Observer observer) {
    this._observers.add(observer);
  }

  /**
   * Detaches an observer from this subject.
   * 
   * @param observer The observer to detach.
   */
  public void detachObserver(Observer observer) {
    this._observers.remove(observer);
  }

  /**
   * Registers a notification method.
   * 
   * @param method The method to register.
   */
  public void registerNotificationMethod(NotificationMethod method) {
    this._notificationMethods.add(method);
  }

  /**
   * Unregisters a notification method.
   * 
   * @param method
   */
  public void unregisterNotificationMethod(NotificationMethod method) {
    this._notificationMethods.remove(method);
  }

  /**
   * Notifies all attached observers with a specific notification method.
   * 
   * @param method Notification method to use.
   * @param notification Notification to send.
   */
  public void notifyObservers(NotificationMethod method, Notification notification) {
    for (Observer observer : this._observers)
      method.send(observer, notification);
  }

  /**
   * Notifies all attached observers with all default notification methods.
   * 
   * @param notification Notification to send.
   */
  public void notifyObservers(Notification notification) {
    for (NotificationMethod method : this._notificationMethods)
      notifyObservers(method, notification);
  }
}
