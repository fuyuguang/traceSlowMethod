package com.fyg.tracemethod.ui.thread;

import android.view.View;
import android.view.View.OnClickListener;

class OnclickTestListener implements OnClickListener {
    private final OnclickTest this$0;

    OnclickTestListener(final OnclickTest this$0) {
        this.this$0 = this$0;
    }

    public void onClick(View view) {
        Object tag = view.getTag(view.getId());
        if (tag != null && tag instanceof Long) {
            Long temTime = (Long)tag;
            if (System.currentTimeMillis() - temTime < 300L) {
                return;
            }
        }

        view.setTag(view.getId(), System.currentTimeMillis());

    }
}
