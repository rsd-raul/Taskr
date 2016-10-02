package com.software.achilles.tasked.util;
import android.support.annotation.NonNull;
import android.util.Log;

public abstract class L {

    // ------------------------- CONSTANTS ---------------------------

    private static final int VERBOSE = 1, DEBUG = 2, INFO = 3, WARN = 4, ERROR = 5, WTF = 6;

    private static final String DEFAULT = "Taskr";

    // -------------------------- METHODS ----------------------------

    /**
     * Calls the appropriate Log method based on level.
     *
     * @param level     Type of log wanted
     * @param msg       Message to log
     * @param throwable Throwable to log
     */
    private static void log(final int level, final String msg, final Throwable throwable) {

        final String TAG = level > WARN ? getExactDetails() : DEFAULT;

        switch (level) {
            case VERBOSE:
                Log.v(TAG, msg, throwable);
                break ;
            case DEBUG:
                Log.d(TAG, msg, throwable);
                break ;
            case INFO:
                Log.i(TAG, msg, throwable);
                break ;
            case WARN:
                Log.w(TAG, msg, throwable);
                break ;
            case ERROR:
                Log.e(TAG, msg, throwable);
                break ;
            case WTF:
                Log.wtf(TAG, msg, throwable);
                break ;
        }
    }

    /**
     * When logging errors, provide a more useful TAG for the log.
     *
     * @return the exact position where the call was made
     */
    private static String getExactDetails(){
        final StackTraceElement element;
        try {
            element = new Throwable().getStackTrace()[3];
        }catch (Exception e){ return DEFAULT; }

        String callerClassName = element.getClassName();
        callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
        if (callerClassName.indexOf("$") > 0)
            callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"));

        String callerMethodName = element.getMethodName();
        callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1);
        if (callerMethodName.equals("<init>"))
            callerMethodName = callerClassName;

        String callerLineNumber = String.valueOf(element.getLineNumber());

        return "[" + callerClassName + "." + callerMethodName + "():" + callerLineNumber + "]";
    }

    // --------------------------- CALLS -----------------------------

    public static void d(@NonNull final String msg) {
        log(DEBUG, msg, null);
    }
    public static void d(final String msg, @NonNull final Throwable throwable) {
        log(DEBUG, msg, throwable);
    }

    public static void v(@NonNull final String msg) {
        log(VERBOSE, msg, null);
    }
    public static void v(final String msg, @NonNull final Throwable throwable) {
        log(VERBOSE, msg, throwable);
    }

    public static void i(@NonNull final String msg) {
        log(INFO, msg, null);
    }
    public static void i(final String msg, @NonNull final Throwable throwable) {
        log(INFO, msg, throwable);
    }

    public static void w(@NonNull final String msg) {
        log(WARN, msg, null);
    }
    public static void w(final String msg, @NonNull final Throwable throwable) {
        log(WARN, msg, throwable);
    }

    public static void e(@NonNull final String msg) {
        log(ERROR, msg, null);
    }
    public static void e(final String msg, @NonNull final Throwable throwable) {
        log(ERROR, msg, throwable);
    }

    public static void wtf(@NonNull final String msg) {
        log(WTF, msg, null);
    }
    public static void wtf(final String msg, @NonNull final Throwable throwable) {
        log(WTF, msg, throwable);
    }
}