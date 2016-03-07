package hk.mapp.scaffold.android.internal

import com.google.common.collect.MapMaker
import com.google.common.reflect.TypeToken
import hk.mapp.scaffold.android.util.Apps
import hk.mapp.scaffold.core.base.event.BusDelegate
import hk.mapp.scaffold.core.base.event.EventObserver
import hk.mapp.scaffold.core.computeIfNull
import java.util.*

class BusDelegateImpl: BusDelegate {

    private val subscribers: LazyTable<Function1<*, Unit>>
    private val producers: LazyTable<Function0<*>>

    init {
        val m = MapMaker().weakKeys()
        subscribers = LazyTable(m)
        producers = LazyTable(m)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: Any> subscribe(observer: EventObserver, type: TypeToken<T>, id: String, subscriber: (T) -> Unit) {
        check(Apps.isMainThread())
        subscribers.put(type, observer, id, subscriber)

        producers.asSequence()
                .filter { it.key.isSubtypeOf(type) }
                .flatMap { it.value.asSequence() }
                .filter { it.key.isActive() }
                .flatMap { it.value[id]?.asSequence() ?: emptySequence() }
                .toList()
                .forEach { subscriber(it() as T) }
    }

    override fun <T: Any> produce(observer: EventObserver, type: TypeToken<T>, id: String, producer: () -> T) {
        check(Apps.isMainThread())
        producers.put(type, observer, id, producer)

        dispatch(type, id, producer)
    }

    override fun <T: Any> unsubscribe(type: TypeToken<T>, id: String, subscriber: (T) -> Unit) = run {
        check(Apps.isMainThread())
        subscribers.remove(type, id, subscriber)
    }

    override fun <T: Any> unproduce(type: TypeToken<T>, id: String, producer: () -> T) = run {
        check(Apps.isMainThread())
        producers.remove(type, id, producer)
    }

    override fun <T: Any> post(type: TypeToken<T>, id: String, event: T) {
        check(Apps.isMainThread())

        dispatch(type, id) { event }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <T: Any> dispatch(type: TypeToken<T>, id: String, producer: () -> T) {
        subscribers.asSequence()
                .filter { type.isSubtypeOf(it.key) }
                .flatMap { it.value.asSequence() }
                .filter { it.key.isActive() }
                .flatMap { it.value[id]?.asSequence() ?: emptySequence() }
                .toList()
                .forEach { (it as Function1<T, Unit>)(producer()) };
    }

    private companion object {
        class LazyTable<V>(val m: MapMaker):
                HashMap<TypeToken<*>, MutableMap<EventObserver, MutableMap<String, MutableSet<V>>>>() {

            fun put(type: TypeToken<*>, observer: EventObserver, id: String, v: V) = run {
                computeIfNull(type) { m.makeMap() }
                        .computeIfNull(observer) { HashMap() }
                        .computeIfNull(id) { HashSet() }
                        .add(v)
            }

            fun remove(type: TypeToken<*>, id: String, v: V): Boolean {
                get(type)?.values?.forEach {
                    if (it[id]?.remove(v) ?: false) {
                        return true
                    }
                }
                return false
            }

        }
    }

}
