package com.unitn.musichino.uikit;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

public class SettingsTransition extends TransitionSet {
    public SettingsTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds());
        addTransition(new ChangeTransform());
    }
}