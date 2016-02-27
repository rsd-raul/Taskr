package com.software.achilles.tasked.extras;

//import android.util.Log;

public class ThreadManager {

    // TODO Investigar sobre threads y como manejarlos, sigue dando "Skipped X frames!"
    public static boolean launchIfPossible(Runnable runnable){
        // Launch new thread
//        try {
//            Log.d("d", "n");
//            new Thread(runnable).start();
        // En el movil de Sara da error, pero no lo detecta el Catch curiosamente... Art vs Dalvik?
//        }catch (IllegalStateException e) {
//            Log.d("d", "e");
            runnable.run();
//        }
//        Log.d("d", "f");

        return true;
    }
}
