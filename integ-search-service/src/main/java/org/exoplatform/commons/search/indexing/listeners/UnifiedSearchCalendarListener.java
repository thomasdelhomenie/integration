package org.exoplatform.commons.search.indexing.listeners;

import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.impl.CalendarEventListener;
import org.exoplatform.commons.api.indexing.IndexingService;
import org.exoplatform.commons.api.indexing.data.SearchEntry;
import org.exoplatform.commons.api.indexing.data.SearchEntryId;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Indexing with :
 * - collection : "calendar"
 * - type : (event|task)
 * - name : object id
 *
 * FIXME Listeners only for public events. What about others events ?
 */
public class UnifiedSearchCalendarListener extends CalendarEventListener {

  private static Log log = ExoLogger.getLogger(UnifiedSearchCalendarListener.class);

  private final IndexingService indexingService;

  public UnifiedSearchCalendarListener() {
    indexingService = (IndexingService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(IndexingService.class);

    if(indexingService == null) {
      log.warn("No IndexingService found, data will not be indexed for the search");
    }
  }

  @Override
  public void savePublicEvent(CalendarEvent event, String calendarId) {
    // FIXME No need of the calendarId argument as it is already contained in the event object

    if(indexingService != null) {
      Map<String, Object> content = new HashMap<String, Object>();
      content.put("event", event);
      SearchEntry searchEntry = new SearchEntry("calendar", event.getEventType().toLowerCase(), event.getId(), content);
      indexingService.add(searchEntry);
    }
  }

  @Override
  public void updatePublicEvent(CalendarEvent event, String calendarId) {
    // FIXME No need of the calendarId argument as it is already contained in the event object

    if(indexingService != null) {
      Map<String, Object> content = new HashMap<String, Object>();
      content.put("event", event);
      SearchEntryId searchEntryId = new SearchEntryId("calendar", event.getEventType().toLowerCase(), event.getId());
      indexingService.update(searchEntryId, content);
    }
  }

  @Override
  public void deletePublicEvent(CalendarEvent event, String calendarId) {
    if(indexingService != null) {
      SearchEntryId searchEntryId = new SearchEntryId("calendar", event.getEventType().toLowerCase(), event.getId());
      indexingService.delete(searchEntryId);
    }
  }

  @Override
  public void updatePublicEvent(CalendarEvent oldEvent, CalendarEvent event, String calendarId) {
    // FIXME Why 2 methods for an event update ?

    if(indexingService != null) {
      Map<String, Object> content = new HashMap<String, Object>();
      content.put("event", event);
      SearchEntryId searchEntryId = new SearchEntryId("calendar", event.getEventType().toLowerCase(), event.getId());
      indexingService.update(searchEntryId, content);
    }
  }
}
