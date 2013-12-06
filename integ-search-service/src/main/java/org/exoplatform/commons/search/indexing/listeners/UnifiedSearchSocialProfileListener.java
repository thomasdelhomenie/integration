package org.exoplatform.commons.search.indexing.listeners;

import org.exoplatform.commons.api.indexing.IndexingService;
import org.exoplatform.commons.api.indexing.data.SearchEntryId;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.profile.ProfileLifeCycleEvent;
import org.exoplatform.social.core.profile.ProfileListenerPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Indexing with :
 * - collection : "social"
 * - type : "profile"
 * - name : username
 */
public class UnifiedSearchSocialProfileListener extends ProfileListenerPlugin {

  private static Log log = ExoLogger.getLogger(UnifiedSearchSocialProfileListener.class);

  private final IndexingService indexingService;

  public UnifiedSearchSocialProfileListener() {
    indexingService = (IndexingService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(IndexingService.class);

    if(indexingService == null) {
      log.warn("No IndexingService found, data will not be indexed for the search");
    }
  }

  @Override
  public void avatarUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    profileUpdated(profileLifeCycleEvent);
  }

  @Override
  public void basicInfoUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    profileUpdated(profileLifeCycleEvent);
  }

  @Override
  public void contactSectionUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    profileUpdated(profileLifeCycleEvent);
  }

  @Override
  public void experienceSectionUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    profileUpdated(profileLifeCycleEvent);
  }

  @Override
  public void headerSectionUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    profileUpdated(profileLifeCycleEvent);
  }

  protected void profileUpdated(ProfileLifeCycleEvent profileLifeCycleEvent) {
    if(indexingService != null) {
      Map<String, Object> content = new HashMap<String, Object>();
      content.put("profile", profileLifeCycleEvent.getProfile());
      SearchEntryId searchEntryId = new SearchEntryId("social", "profile", profileLifeCycleEvent.getProfile().getId());
      indexingService.update(searchEntryId, content);
    }
  }
}
