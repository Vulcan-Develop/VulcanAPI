package net.vulcandev.vulcanapi.event;

import net.vulcandev.vulcanapi.VulcanAPI;
import net.xantharddev.vulcanlib.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class VulcanEventManager {

    private final Map<Class<? extends VulcanEvent>, List<RegisteredListener>> listeners = new HashMap<>();

    private VulcanEventManager() {}

    private static final class InstanceHolder {
        static final VulcanEventManager instance = new VulcanEventManager();
    }

    public static VulcanEventManager getInstance() {
        return InstanceHolder.instance;
    }

    private void log(String message) {
        Logger.log(VulcanAPI.getInstance(), message);
    }

    public void registerListener(VulcanListener listener) {
        Class<?> clazz = listener.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) continue;

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !VulcanEvent.class.isAssignableFrom(params[0])) {
                log("Invalid event handler: " + method.getName() + " in " + clazz.getSimpleName());
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends VulcanEvent> eventType = (Class<? extends VulcanEvent>) params[0];

            RegisteredListener regListener = new RegisteredListener(
                    listener, method, annotation.priority(), annotation.ignoreCancelled()
            );

            listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(regListener);

            listeners.get(eventType).sort((a, b) ->
                    Integer.compare(b.priority.getPriority(), a.priority.getPriority()));
        }

        log("Registered listener: " + clazz.getSimpleName());
    }

    public void unregisterListener(VulcanListener listener) {
        listeners.values().forEach(list ->
                list.removeIf(registered -> registered.listener == listener));

        log("Unregistered listener: " + listener.getClass().getSimpleName());
    }

    public boolean callEvent(VulcanEvent event) {
        List<RegisteredListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) return false;

        for (RegisteredListener listener : eventListeners) {
            if (event instanceof Cancellable && ((Cancellable) event).isCancelled()
                    && listener.ignoreCancelled) {
                continue;
            }

            try {
                listener.method.setAccessible(true);
                listener.method.invoke(listener.listener, event);
            } catch (Exception e) {
                log("Error executing event handler: " + e.getMessage());
                e.printStackTrace();
            }

            if (event instanceof Cancellable && ((Cancellable) event).isCancelled()
                    && listener.priority != EventPriority.MONITOR) {
                break;
            }
        }

        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }

    public void shutdown() {
        listeners.clear();
        log("Event manager shutdown - all listeners cleared");
    }

    public int getListenerCount(Class<? extends VulcanEvent> eventClass) {
        List<RegisteredListener> list = listeners.get(eventClass);
        return list == null ? 0 : list.size();
    }

    public int getTotalListenerCount() {
        return listeners.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    private static class RegisteredListener {
        final VulcanListener listener;
        final Method method;
        final EventPriority priority;
        final boolean ignoreCancelled;

        RegisteredListener(VulcanListener listener, Method method, EventPriority priority, boolean ignoreCancelled) {
            this.listener = listener;
            this.method = method;
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
        }
    }
}
