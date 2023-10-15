package events.scene

import events.Subscribe
import scene.Scene

class SceneClosedEvent(val scene: Scene) : SceneEvent() {

        interface Listener {
            @Subscribe
            fun onSceneClosed(event: SceneClosedEvent)
        }
}