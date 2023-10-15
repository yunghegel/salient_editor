package events.assets

import events.Event
import events.Subscribe

class AssetLoadingInitalizedEvent() : Event() {

    interface Listener {
        @Subscribe
        fun onAssetLoadingInitalized(event: AssetLoadingInitalizedEvent)
    }

}