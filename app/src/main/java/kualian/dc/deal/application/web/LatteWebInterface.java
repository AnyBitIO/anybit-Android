package kualian.dc.deal.application.web;

import android.webkit.JavascriptInterface;

import kualian.dc.deal.application.web.event.Event;
import kualian.dc.deal.application.web.event.EventManager;


/**
 *
 */

final class LatteWebInterface {
    private final WebDelegate DELEGATE;

    private LatteWebInterface(WebDelegate delegate) {
        this.DELEGATE = delegate;
    }

    static LatteWebInterface create(WebDelegate delegate) {
        return new LatteWebInterface(delegate);
    }

    @SuppressWarnings("unused")
    @JavascriptInterface
    public String event(String params) {
        //final String action = JSON.parseObject(params).getString("action");
        final Event event = EventManager.getInstance().createEvent(params);
        if (event != null) {
            event.setAction(params);
            event.setDelegate(DELEGATE);
            event.setContext(DELEGATE.getContext());
            event.setUrl(DELEGATE.getUrl());
            return event.execute(params);
        }
        return null;
    }
}
