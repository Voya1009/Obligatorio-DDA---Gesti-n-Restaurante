package observer;

import java.util.ArrayList;

public class ObserverManager {

    private final ArrayList<Observer> observers;

    public ObserverManager() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}