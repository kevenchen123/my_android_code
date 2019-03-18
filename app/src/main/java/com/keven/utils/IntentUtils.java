package com.keven.utils;

import android.content.Context;
import android.content.Intent;
import java.net.URISyntaxException;

public class IntentUtils {
    private final static String HOME_INTENT = "intent:#Intent;package=com.android.launcher3;action=android.intent.action.MAIN;category=android.intent.category.HOME;launchFlags=0x14000000;end";

    private void startActivity(Context context, String mIntent) {
        try {
            if (mIntent != null) {
                context.startActivity(Intent.parseUri(mIntent, Intent.URI_INTENT_SCHEME));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to attach intent", e);
        }
    }
}
