/*
   Copyright 2011 Kalixia, SARL.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
