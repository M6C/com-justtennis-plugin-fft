package com.justtennis.plugin.yout.rxjava;

import android.support.annotation.IntDef;

import com.justtennis.plugin.common.rxjava.RxBus;

import java.lang.annotation.Retention;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Used for subscribing to and publishing to subjects. Allowing you to send data between activities, fragments, etc.
 */

public final class RxFindVideo {

    public static final int SUBJECT_REFRESH = 0;
    public static final int SUBJECT_UPDATE_DOWNLOAD_STATUS_VIDEO = 1;

    @Retention(SOURCE)
    @IntDef({SUBJECT_REFRESH, SUBJECT_UPDATE_DOWNLOAD_STATUS_VIDEO})
    @interface Subject {
    }

    private static RxBus bus = new RxBus();

    private RxFindVideo() {}

    /**
     * Subscribe to the specified subject and listen for updates on that subject. Pass in an object to associate
     * your registration with, so that you can unsubscribe later.
     * <br/><br/>
     * <b>Note:</b> Make sure to call {@link RxFindVideo#unregister(Object)} to avoid memory leaks.
     */
    public static void subscribe(@Subject int subject, @NonNull Object lifecycle, @NonNull Consumer<Object> action) {
        bus.subscribe(subject, lifecycle, action);
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    public static void publish(@Subject int subject) {
        bus.publish(subject, Observable.empty());
    }

    /**
     * Publish an object to the specified subject for all subscribers of that subject.
     */
    public static void publish(@Subject int subject, @NonNull Object message) {
        bus.publish(subject, message);
    }

    /**
     * Unregisters this object from the bus, removing all subscriptions.
     * This should be called when the object is going to go out of memory.
     */
    public static void unregister(@NonNull Object lifecycle) {
        bus.unregister(lifecycle);
    }
}