package com.kalixia.tapestry.perf4j.services;

import org.apache.tapestry5.ioc.MethodAdvice;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Order;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.perf4j.aop.Profiled;

import java.lang.reflect.Method;

public class Perf4JModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(ComponentRequestFilter.class, Perf4JComponentRequestFilter.class).withId("perf4JComponentRequestFilter");
    }

    /**
     * Contribute {@link Perf4JComponentRequestFilter} to the pipeline in order to create a {@link org.perf4j.StopWatch}
     * per intercepted page called.
     *
     * @param configuration
     * @param filter
     */
    public static void contributeComponentRequestHandler(OrderedConfiguration<ComponentRequestFilter> configuration,
                                                         @InjectService("perf4JComponentRequestFilter") ComponentRequestFilter filter) {
        configuration.add("Perf4JFilter", filter, "before:*");
    }

//    public static void contributeComponentClassTransformWorker(
//            OrderedConfiguration<ComponentClassTransformWorker> configuration) {
//        configuration.addInstance("Perf4J", Perf4JWorker.class, "after:*");
//    }

    /**
     * Advise all methods that are tagged with the {@link Profiled} annotation.
     * <br/>
     * <strong>This only works when annotations are placed on the interface and NOT implementation classes!</strong>
     * @param receiver
     */
    @Match("*")
    @Order("after:*")
    public static void adviseProfiledMethods(MethodAdviceReceiver receiver) {
        Class<?> serviceInterface = receiver.getInterface();
        for (Method method : serviceInterface.getMethods()) {
            Profiled annotation = method.getAnnotation(Profiled.class);
            if (annotation != null) {
                MethodAdvice advice = new ProfiledMethodAdvice(annotation);
                receiver.adviseMethod(method, advice);
            }
        }
    }

}
