package events.scene

import events.Subscribe
import scene.Scene

class SceneLoadedEvent(var scene: Scene) : SceneEvent() {

    interface Listener {
        @Subscribe
        fun onSceneLoaded(event: SceneLoadedEvent)
    }
}