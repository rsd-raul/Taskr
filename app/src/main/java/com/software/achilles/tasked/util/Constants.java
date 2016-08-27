package com.software.achilles.tasked.util;

import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions.Picker;
import com.software.achilles.tasked.view.fragments.DashboardFragment;

public abstract class Constants {

    // --------------------------- Values ----------------------------

    public static final long WEEK_IN_MILLISECOND =      604800000L; // UNUSED
    public static final long DAY_IN_MILLISECOND =       86400000L;  // UNUSED
    public static final long MINUTE_IN_MILLISECOND =    60000L;     // UNUSED

    public static final int TASK_LIST =                 1;
    public static final int LOCATION =                  9;
    public static final int LABEL =                     10;

    // ---------------------- Navigation Drawer ----------------------

    public static final int DASHBOARD =                 2;
    public static final int SNOOZED =                   3;
    public static final int COMPLETED =                 4;
    public static final int GLANCE =                    5;
    public static final int PLANNER =                   6;

    public static final int SETTINGS =                  7;
    public static final int CONTACT =                   8;

    public static final int SETTINGS_ACCOUNTS =         9;          // UNUSED
    public static final int ADD_ACCOUNT =               10;         // UNUSED

    public static final int ADD_TASK_LIST =             11;

    // ------------------------ Filter Drawer ------------------------

    public static final int STARRED =                   13;
    public static final int DUE_TODAY =                 14;
    public static final int DUE_THIS_WEEK =             15;

    // If the number is too short it collapses with the id of the actual elements and misbehaves
    public static final int COLLAPSIBLE_TASK_LIST =     2147483601;
    public static final int COLLAPSIBLE_LABEL_LIST =    2147483602;
    public static final int COLLAPSIBLE_LOCATION_LIST = 2147483603;

    public static final int COLLAPSIBLE_ORDER_LIST =    17;
    public static final int ALPHABETICAL =              18;
    public static final int DUE_DATE =                  19;
    public static final int CUSTOM_ORDER =              20;
    public static final int NONE =                      -1;

    public static final int LIST_SEPARATOR =            23;
    public static final int CLEAR_FILTER =              24;

    // ------------------------- FAB and FAM -------------------------

    public static final int ADD_TASK =                  25;
    public static final int ADD_LABEL =                 26;

    // -------------------------- Dashboard --------------------------

    public static final int DASH_TASK =                 21;
    public static final int DASH_DONE =                 22;
    public static final int DASH_FAVE =                 12;
    public static final int DASH_DATE =                 16;

    // ------------------------ Task Creation ------------------------

    public static final int DETAIL_DESCRIPTION =        27;
    public static final int DETAIL_LOCATION =           28;
    public static final int DETAIL_ALARM =              29;
    public static final int DETAIL_LABELS =             30;

    public static final String LIST_INDEX =             "listIndex";
    public static final String TASK_ID =                "taskId";

    public static final int ADD_LOCATION =              33;

    // ----------------------- Sublime Picker ------------------------

    public static final Picker TIME_PICKER =            Picker.TIME_PICKER;
    public static final Picker DATE_PICKER =            Picker.DATE_PICKER;
    public static final Picker REPEAT_PICKER =          Picker.REPEAT_OPTION_PICKER;
    public static final String SUBLIME_OPTIONS =        "SUBLIME_OPTIONS";
    public static final String SUBLIME_PICKER =         "SUBLIME_PICKER";

    public static final int FIRST =                     31;
    public static final int LAST =                      32;

    // ------------------------ Place Picker -------------------------

    public static final int PLACE_PICKER_REQUEST =      1;

}
