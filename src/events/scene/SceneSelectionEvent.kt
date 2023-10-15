package events.scene

import events.Subscribe
import scene.Scene

class SceneSelectionEvent(val scene: Scene) :SceneEvent() {

    init {
        this.desc = "${scene.name} selected"
    }

    interface Listener {
        @Subscribe
        fun onSceneSelection(event: SceneSelectionEvent)
    }
}