package events.proj

import events.ProjectEvent
import events.Subscribe
import project.Project

class ProjectCreatedEvent(project: Project) : ProjectEvent(project)  {

    interface Listener {
        @Subscribe
        fun onProjectCreated(event: ProjectCreatedEvent)
    }
}