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

import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Order;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.LibraryMapping;
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

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("perf4j", "com.kalixia.tapestry.perf4j"));
    }

//    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration) {
//        configuration.add("perf4j", "com/kalixia/tapestry/perf4j");
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
