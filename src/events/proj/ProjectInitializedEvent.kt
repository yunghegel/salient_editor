package events.proj

import events.ProjectEvent
import events.Subscribe
import project.Project

class ProjectInitializedEvent(project: Project) : ProjectEvent(project) {

    interface Listener {
        @Subscribe
        fun onInitialized(event: ProjectInitializedEvent)
    }

}