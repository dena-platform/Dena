package com.dena.platform.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage other ThreadLocals with use of a ThreadLocal.
 * This class contain threads that managing other threads.
 * <p>
 * <b>Note:</b> Adapted from Broadleaf platform: https://github.com/BroadleafCommerce/BroadleafCommerce
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public class ThreadLocalManager {
    private final static Logger log = LoggerFactory.getLogger(ThreadLocalManager.class);

    private List<ThreadLocal> threadLocals = new ArrayList<>();

    /**
     * Create an instance of ThreadLocalManager in thread local. this manage all thread local created with manager.
     */
    private final static ThreadLocal<ThreadLocalManager> THREAD_LOCAL_MANAGER = ThreadLocal.withInitial(ThreadLocalManager::new);

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type) {
        return createThreadLocal(type, true);
    }

    public static <T> ThreadLocal<T> createThreadLocal(final Class<T> type, final boolean createInitialValue) {

        ThreadLocal<T> response = new ThreadLocal<T>() {
            @Override
            protected T initialValue() { // This method call for first time the thread local get method call
                addThreadLocal(this);  // Add this thread local for example ThreadLocal<DenaRequestContext> to ThreadLocalManager
                if (!createInitialValue) {
                    return null;
                }

                try {
                    return type.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        return response;
    }

    /**
     * Remove all thread local that is created by current thread
     */
    public static void remove() {
        THREAD_LOCAL_MANAGER.get().threadLocals.forEach(threadLocal -> {
            log.debug("Removing ThreadLocal #{} from request thread.", threadLocal);
            threadLocal.remove();
        });

        THREAD_LOCAL_MANAGER.get().threadLocals.clear();
        THREAD_LOCAL_MANAGER.remove();
    }


    private static void addThreadLocal(final ThreadLocal threadLocal) {
        THREAD_LOCAL_MANAGER.get().threadLocals.add(threadLocal);
    }


}
