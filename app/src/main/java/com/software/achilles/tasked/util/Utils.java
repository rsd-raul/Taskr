package com.software.achilles.tasked.util;

import com.software.achilles.tasked.R;
import com.software.achilles.tasked.model.domain.Label;

import java.util.List;

public abstract class Utils {

    public static boolean notEmpty( String s ) {
        return s != null && !s.trim().isEmpty();
    }
    public static boolean notEmpty( List array ) {
        return array != null && array.size() > 0;
    }

    public static String filterAndFormatLabels(List<Label> labels, Integer[] which, boolean all){
        String labelsStr =  "";

        if(all)
            for(Label aux : labels)
                labelsStr += "#" + aux.getTitle() + " ";
        else
            for(int aux : which)
                labelsStr += "#" + labels.get(aux).getTitle() + " ";

        return labelsStr;
    }
}
