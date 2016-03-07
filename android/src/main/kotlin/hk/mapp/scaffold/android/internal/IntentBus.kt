package hk.mapp.scaffold.android.internal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.common.collect.MapMaker
import hk.mapp.scaffold.android.util.Apps
import hk.mapp.scaffold.core.base.event.Bus
import hk.mapp.scaffold.core.base.event.EventObserver
import hk.mapp.scaffold.core.computeIfNull
import java.util.*

object IntentBus {

    private val map = MapMaker().weakKeys().makeMap<Context, Pair<Bus, MutableSet<String>>>()

    @Synchronized
    fun subscribe(observer: EventObserver, context: Context, action: String, subscriber: (Intent) -> Unit) {
        val pair = map.computeIfNull(context) { Bus(BusDelegateImpl()) to HashSet() }
        val bus = pair.first
        bus.subscribe(observer, action, subscriber)

        if (pair.second.add(action)) {
            Apps.register(context, action, object: BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    bus.post(action, intent)
                }
            })
        }
    }

}
