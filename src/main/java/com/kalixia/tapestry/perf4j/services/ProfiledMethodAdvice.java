package com.kalixia.tapestry.perf4j.services;

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.ioc.MethodAdvice;
import org.perf4j.StopWatch;
import org.perf4j.aop.Profiled;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfiledMethodAdvice implements MethodAdvice {
    private final Profiled annotation;

    ProfiledMethodAdvice(Profiled annotation) {
        this.annotation = annotation;
    }

    public void advise(Invocation invocation) {
        String tag = annotation.tag();
        String message = annotation.message();
        Logger logger = LoggerFactory.getLogger(annotation.logger());
        StopWatch watch = new Slf4JStopWatch(tag, message, logger);
        watch.start();
        invocation.proceed();
        watch.stop();
    }
}
