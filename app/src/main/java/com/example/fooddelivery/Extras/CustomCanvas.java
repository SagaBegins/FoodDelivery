package com.example.fooddelivery.Extras;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class CustomCanvas extends View {

    public CustomCanvas(Context context) {
        super(context);
    }

    public CustomCanvas(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
