package hk.mapp.scaffold.core

import com.google.common.reflect.TypeToken
import rx.Single
import rx.SingleSubscriber
import rx.Subscriber
import rx.subscriptions.Subscriptions
import java.util.*
import kotlin.reflect.KClass

// Collections:

fun Sequence<String>.filterNotEmpty() = this.filterNot { it.isEmpty() }

fun <K, V> Map<K, V>.inverse() = map { it.value to it.key }.toMap()

inline fun <K: Any, V> MutableMap<K, V>.computeIfNull(key: K, defaultValue: () -> V): V =
        get(key) ?: defaultValue().apply { put(key, this) }

fun <C> Map<*, Map<C, *>>.columnKeys() = values.asSequence().flatMap { it.keys.asSequence() }

inline fun <T, K> Array<out T>.toDistinctMapBy(selector: (T) -> K) = toDistinctMap(selector) { it }

inline fun <T, K, V> Array<out T>.toDistinctMap(selector: (T) -> K, transform: (T) -> V): Map<K, V> {
    val capacity = 1 + size / .75f
    val result = LinkedHashMap<K, V>(Math.max(16, capacity.toInt()))
    for (element in this) {
        val key = selector(element)
        require(result.put(key, transform(element)) == null) { "Duplicated key: $key" }
    }
    return result
}

fun <K, V> Sequence<Pair<K, V>>.toDistinctMap() = LinkedHashMap<K, V>().apply {
    for ((k, v) in this@toDistinctMap) {
        require(put(k, v) == null) { "Duplicated key: $k" }
    }
}

fun <T> T.within(c: Collection<T>) = this in c

fun <T, R> Sequence<T>.flapMapIndexed(transform: (Int, T) -> Sequence<R>) =
        mapIndexed(transform).flatten()

fun <T> Sequence<T>?.orEmpty() = this ?: emptySequence<T>()


// Reflections:

inline fun <reified T> typeToken() = object: TypeToken<T>() { }

fun <T: Any> T.simpleClassName() = javaClass.simpleName

infix fun KClass<*>.isSubTypeOf(type: KClass<*>) = type.java.isAssignableFrom(java) // TODO: wait for API


// Check argument:

inline fun <reified T: Any> Any.cast(lazyMessage: () -> Any): T {
    return try { this as T } catch (e: ClassCastException) {
        throw IllegalArgumentException(lazyMessage().toString(), e)
    }
}

inline fun <reified T: Any> Any.cast() = this as T

inline fun <T: Any> T?.notNull(lazyMessage: () -> Any) = requireNotNull(this, lazyMessage)


// Rx:

fun <T> single(body : (SingleSubscriber<in T>) -> Unit): Single<T> = Single.create(body)

fun <T: Any> T.toSingle(): Single<T> = Single.just(this)

fun SingleSubscriber<*>.onUnsubscribe(f: () -> Unit) = add(Subscriptions.create(f))

fun Subscriber<*>.onUnsubscribe(f: () -> Unit) = add(Subscriptions.create(f))
