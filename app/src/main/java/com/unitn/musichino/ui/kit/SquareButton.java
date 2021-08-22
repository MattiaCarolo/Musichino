package com.unitn.musichino.ui.kit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class SquareButton extends Button {
    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int WidthSpec, int HeightSpec){
        int newWidthSpec = HeightSpec ;

        super.onMeasure(newWidthSpec, HeightSpec);
    }
}
