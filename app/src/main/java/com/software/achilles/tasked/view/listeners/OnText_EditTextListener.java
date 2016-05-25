package com.software.achilles.tasked.view.listeners;

import android.text.Editable;
import android.text.TextWatcher;

public class OnText_EditTextListener implements TextWatcher{

    // --------------------------- Values ----------------------------

    // ------------------------- Attributes --------------------------

    private boolean active = false;
    private Runnable actionPositive, actionNegative;

    // ------------------------- Constructor -------------------------

    public OnText_EditTextListener(Runnable actionPositive, Runnable actionNegative) {
        this.actionPositive = actionPositive;
        this.actionNegative = actionNegative;
    }

    // -------------------------- Use Cases --------------------------

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean hasContent = !s.toString().matches("");

        // Toggles the FAB between "Positive" (when text present) and "Negative" (when not).
        if(hasContent && !active) {
            actionPositive.run();
            active = true;
        }else if (!hasContent){
            actionNegative.run();
            active = false;
        }
    }

}
