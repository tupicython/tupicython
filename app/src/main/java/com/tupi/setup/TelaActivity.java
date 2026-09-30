package com.tupi.setup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TelaActivity extends Activity {

    public static Activity instancia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instancia = this;

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instancia = null;
    }
}
