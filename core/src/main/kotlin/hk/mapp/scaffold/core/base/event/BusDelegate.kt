package hk.mapp.scaffold.core.base.event

import com.google.common.reflect.TypeToken

interface BusDelegate {

    fun <T: Any> subscribe(observer: EventObserver, type: TypeToken<T>, id: String, subscriber: (T) -> Unit)
    fun <T: Any> produce(observer: EventObserver, type: TypeToken<T>, id: String, producer: () -> T)
    fun <T: Any> unsubscribe(type: TypeToken<T>, id: String, subscriber: (T) -> Unit): Boolean
    fun <T: Any> unproduce(type: TypeToken<T>, id: String, producer: () -> T): Boolean
    fun <T: Any> post(type: TypeToken<T>, id: String, event: T)

}
