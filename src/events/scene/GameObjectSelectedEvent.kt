package events.scene

import events.Event
import events.Subscribe
import scene.graph.GameObject

class GameObjectSelectedEvent(val gameObject: GameObject) : Event() {

        init {
            msg = "GameObject ${gameObject.name} selected; { id: ${gameObject.id} }"
        }

        interface Listener {
            @Subscribe
            fun onGameObjectSelected(event: GameObjectSelectedEvent)
        }
}