package bigappcompany.com.artistbooking.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import bigappcompany.com.artistbooking.R;


public class MyCustomBoldTextView extends TextView {
    public MyCustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),getResources().getString(R.string.bold)));
    }
}
