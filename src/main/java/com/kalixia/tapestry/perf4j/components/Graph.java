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
package com.kalixia.tapestry.perf4j.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.perf4j.chart.StatisticsChartGenerator;
import org.perf4j.log4j.GraphingStatisticsAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component displaying a specified Perf4J graph.
 * Most of the time you'll prefer to use the {@link Graphs} component instead as it allow to dynamically update
 * the graph displayed on the web page.
 *
 * @author jeje
 */
public class Graph {
    /** Name of the graph to generate. */
    @Parameter
    @Property
    private String name;

    private static final Logger logger = LoggerFactory.getLogger(Graph.class);

    public String getChartUrl() {
        GraphingStatisticsAppender appender = GraphingStatisticsAppender.getAppenderByName(name);
        StatisticsChartGenerator chartGenerator = appender.getChartGenerator();
        return chartGenerator.getChartUrl();
    }

    public boolean isNameGiven() {
        return name != null && !"".equals(name.trim());
    }
}
