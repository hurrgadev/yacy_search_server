// PerformaceSearch_p.java
// (C) 2004, 2005 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 16.02.2005 on http://yacy.net
//
// This is a part of YaCy, a peer-to-peer based web search engine
//
// $LastChangedDate$
// $LastChangedRevision$
// $LastChangedBy$
//
// LICENSE
// 
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import net.yacy.kelondro.util.MemoryTracker;

import de.anomic.http.server.RequestHeader;
import de.anomic.server.serverObjects;
import de.anomic.server.serverSwitch;
import de.anomic.yacy.graphics.ProfilingGraph;

public class PerformanceSearch_p {
    
    public static serverObjects respond(final RequestHeader header, final serverObjects post, final serverSwitch sb) {
        // return variable that accumulates replacements
        final serverObjects prop = new serverObjects();
        
        final ArrayList<MemoryTracker.Event> events = MemoryTracker.history("SEARCH");
        int c = 0;
        MemoryTracker.Event event;
        ProfilingGraph.searchEvent search;
        long lastt = 0;
        if (events != null) synchronized (events) {
            Iterator<MemoryTracker.Event> i = events.iterator();
            while (i.hasNext()) {
                event = i.next();
                search = (ProfilingGraph.searchEvent) event.payload;
                prop.put("table_" + c + "_query", search.queryID);
                prop.put("table_" + c + "_event", search.processName);
                prop.putNum("table_" + c + "_count", search.resultCount);
                prop.putNum("table_" + c + "_delta", event.time - lastt);
                prop.put("table_" + c + "_time", (new Date(event.time)).toString());
                prop.putNum("table_" + c + "_duration", search.duration);
                c++;
                lastt = event.time;
            }
        }
        prop.put("table", c);
        return prop;
    }
}
