package events.proj

import events.ProjectEvent
import events.Subscribe
import project.Project

class ProjectChangedEvent(project: Project) : ProjectEvent(project) {

        interface Listener {
            @Subscribe
            fun onProjectChanged(event: ProjectChangedEvent)
        }
}