package com.kalixia.tapestry.perf4j.services;

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
    public void transform(ClassTransformation transformation, MutableComponentModel model) {
        // Profiled methods
        for (TransformMethod method : transformation.matchMethodsWithAnnotation(Profiled.class)) {
            transformMethod(transformation, method);
        }
    }

    private void transformMethod(final ClassTransformation transformation, final TransformMethod profiledMethod) {
        ComponentMethodAdvice profiledMethodAdvice = new ComponentMethodAdvice() {
            public void advise(ComponentMethodInvocation invocation) {
                Profiled annotation = transformation.getAnnotation(Profiled.class);
                StopWatch watch = new Slf4JStopWatch(annotation.tag(), annotation.message(),
                        LoggerFactory.getLogger(annotation.logger()));
                watch.start();
                invocation.proceed();
                watch.stop();
            }
        };
        profiledMethod.addAdvice(profiledMethodAdvice);
    }
}
