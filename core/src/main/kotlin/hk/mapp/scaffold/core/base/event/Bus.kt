package hk.mapp.scaffold.core.base.event

import hk.mapp.scaffold.core.typeToken

class Bus(val delegate: BusDelegate) {

    inline fun <reified T: Any> subscribe(observer: EventObserver, id: String, noinline subscriber: (T) -> Unit) =
            delegate.subscribe(observer, typeToken<T>(), id, subscriber)
    inline fun <reified T: Any> subscribe(observer: EventObserver, noinline subscriber: (T) -> Unit) =
            delegate.subscribe(observer, typeToken<T>(), "", subscriber)

    inline fun <reified T: Any> produce(observer: EventObserver, id: String, noinline producer: () -> T) =
            delegate.produce(observer, typeToken<T>(), id, producer)
    inline fun <reified T: Any> produce(observer: EventObserver, noinline producer: () -> T) =
            delegate.produce(observer, typeToken<T>(), "", producer)

    inline fun <reified T: Any> unsubscribe(id: String, noinline subscriber: (T) -> Unit) =
            delegate.unsubscribe(typeToken<T>(), id, subscriber)
    inline fun <reified T: Any> unsubscribe(noinline subscriber: (T) -> Unit) =
            delegate.unsubscribe(typeToken<T>(), "", subscriber)

    inline fun <reified T: Any> unproduce(id: String, noinline producer: () -> T) =
            delegate.unproduce(typeToken<T>(), id, producer)
    inline fun <reified T: Any> unproduce(noinline producer: () -> T) =
            delegate.unproduce(typeToken<T>(), "", producer)

    inline fun <reified T: Any> post(id: String, event: T) =
            delegate.post(typeToken<T>(), id, event)
    inline fun <reified T: Any> post(event: T) =
            delegate.post(typeToken<T>(), "", event)

}
