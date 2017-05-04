package com.example.saurabh.mess2owner;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by saurabh on 4/4/17.
 */

public class RalewaySemiBold extends TextView{
    public RalewaySemiBold(Context context) {
        super(context);
        init(null);
    }

    public RalewaySemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RalewaySemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public RalewaySemiBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Just Change your font name
        Typeface ralewaysemb=Typeface.createFromAsset(getContext().getAssets(),"Raleway-SemiBold.ttf");
        setTypeface(ralewaysemb);
    }
}
