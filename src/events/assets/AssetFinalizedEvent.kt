package events.assets

import assets.Asset
import events.AssetEvent
import events.Subscribe

class AssetFinalizedEvent (asset: Asset<*>): AssetEvent(asset) {

    init {
        this.msg = "${this::class.simpleName} at ${asset.meta.properties.path} finalized"
    }

    interface Listener {
        @Subscribe
        fun onAssetFinalized(event: AssetFinalizedEvent)
    }

}