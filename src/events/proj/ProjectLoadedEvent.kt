package events.proj

import events.ProjectEvent
import events.Subscribe
import project.Project

class ProjectLoadedEvent(project: Project) :ProjectEvent(project) {


    interface Listener {
        @Subscribe
        fun onProjectLoaded(event: ProjectLoadedEvent)
    }
}