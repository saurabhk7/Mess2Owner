package com.example.saurabh.mess2owner;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by saurabh on 4/4/17.
 */

public class RalewayReg extends TextView{
    public RalewayReg(Context context) {
        super(context);
        init(null);
    }

    public RalewayReg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RalewayReg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    public RalewayReg(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Just Change your font name
        Typeface ralewayreg=Typeface.createFromAsset(getContext().getAssets(),"Raleway-Regular.ttf");
        setTypeface(ralewayreg);
    }
}
