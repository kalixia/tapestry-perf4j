package com.kalixia.tapestry.perf4j.services;

import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

import java.io.IOException;

public class Perf4JComponentRequestFilter implements ComponentRequestFilter {
    public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
        StopWatch watch = profilePage(parameters.getActivePageName());
        handler.handleComponentEvent(parameters);
        watch.stop();
    }

    public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException {
        StopWatch watch = profilePage(parameters.getLogicalPageName());
        handler.handlePageRender(parameters);
        watch.stop();
    }

    private StopWatch profilePage(String pageName) {
        String tag = "page." + pageName;
        StopWatch watch = new Slf4JStopWatch(tag);
        watch.start();
        return watch;
    }
}
