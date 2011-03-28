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

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.perf4j.log4j.GraphingStatisticsAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Component displaying the list of graphs that are available (in a combo box) and dynamically updating the generated
 * graph based on the selected graph name.
 * <br/>
 * <img src="../../../../../../images/graph-selection.png"/>
 * <br/>
 * Individual graph can be generated with the {@link Graph} component.
 *
 * @author jeje
 */
public class Graphs {
    @Property
    @Persist
    private String graph;

    @Property
    @Persist
    private List<String> graphNames;

    @InjectComponent("graphZone")
    private Zone zone;

    private static final Logger logger = LoggerFactory.getLogger(Graphs.class);

    @SetupRender
    void setupGraphNames() {
        graphNames = new ArrayList<String>();
        for (GraphingStatisticsAppender appender : GraphingStatisticsAppender.getAllGraphingStatisticsAppenders()) {
            graphNames.add(appender.getName());
        }
    }

    @OnEvent(component = "graphChosen", value = EventConstants.VALUE_CHANGED)
    public Object onGraphChosen(String graph) {
        return zone.getBody();
    }
}
