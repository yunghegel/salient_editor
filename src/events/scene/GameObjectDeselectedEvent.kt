package events.scene

import events.GameObjectEvent
import events.Subscribe
import scene.graph.GameObject

class GameObjectDeselectedEvent(gameObject: GameObject) : GameObjectEvent(gameObject) {

        init {
            msg = "GameObject deselected: id ${gameObject.id} }"
        }

        interface Listener {
            @Subscribe
            fun onGameObjectDeselected(event: GameObjectDeselectedEvent)
        }
}