package events.scene

import events.Subscribe
import scene.Scene

class SceneCreatedEvent(val scene: Scene) : SceneEvent() {

    init {
        this.desc = "${scene.name} created"
    }

    interface Listener {
        @Subscribe
        fun onSceneCreated(event: SceneCreatedEvent)
    }
}