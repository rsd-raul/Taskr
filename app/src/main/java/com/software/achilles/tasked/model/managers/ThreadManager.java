package com.software.achilles.tasked.model.managers;

public class ThreadManager {

    private static int THREADS_AVAILABLE = Runtime.getRuntime().availableProcessors();
    private static int THREADS_IN_USE = 1;

    // TODO Investigar sobre threads y como manejarlos, sigue dando "Skipped X frames!"
    // En el movil de Sara da error, pero no lo detecta el Catch curiosamente... Art vs Dalvik?
    public static boolean launchIfPossible(Runnable runnable){
        // Launch new thread
//        try {
//            if(THREADS_AVAILABLE > THREADS_IN_USE)
//                new Thread(runnable).start();
//            else
//                throw new IllegalStateException();
//
//            THREADS_IN_USE ++;
//            return true;
//        }catch (IllegalStateException e) {
//            Log.d("ThreadManager", "Thread not initialized");
            runnable.run();
            return false;
//        }
    }

    // If the main acivity is paused, pause the other

    private static boolean main_paused;

    public static void mainThreadPaused(boolean status){
        main_paused = status;
    }
}
