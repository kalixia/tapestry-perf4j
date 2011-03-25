package com.kalixia.tapestry.perf4j.services;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.services.ComponentClassTransformWorker;

public class Perf4JModule {

    public static void contributeComponentClassTransformWorker(
            OrderedConfiguration<ComponentClassTransformWorker> configuration) {
        configuration.add("Perf4J", new Perf4JWorker());
    }

}
