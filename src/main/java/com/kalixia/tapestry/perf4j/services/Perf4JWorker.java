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

import org.apache.tapestry5.ioc.OperationTracker;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.TransformMethod;
import org.perf4j.StopWatch;
import org.perf4j.aop.Profiled;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;

public class Perf4JWorker implements ComponentClassTransformWorker {
    private final OperationTracker tracker;

    public Perf4JWorker(OperationTracker tracker) {
        this.tracker = tracker;
    }

    public void transform(final ClassTransformation transformation, final MutableComponentModel model) {
        // Profiled methods
        for (final TransformMethod method : transformation.matchMethodsWithAnnotation(Profiled.class)) {
            tracker.run("Profiling method " + method.getName(), new Runnable() {
                public void run() {
                    ComponentMethodAdvice profiledMethodAdvice = new ComponentMethodAdvice() {
                        public void advise(ComponentMethodInvocation invocation) {
                            Profiled annotation = transformation.getMethodAnnotation(method.getSignature(), Profiled.class);
                            String tag = annotation.tag();
                            String message = annotation.message();
                            StopWatch watch = new Slf4JStopWatch(tag, message,
                                    LoggerFactory.getLogger(annotation.logger()));
                            watch.start();
                            invocation.proceed();
                            watch.stop();
                        }
                    };
                    method.addAdvice(profiledMethodAdvice);
                }
            });
        }
    }
}
