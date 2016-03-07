package hk.mapp.scaffold.core.base.event

/**
 * A representation of event observer which is responsible to indicate the lifecycle of event
 * subscription / production. No more handling will occur if either the EventObserver is garbage
 * collected or explicitly returning false in its isActive method.
 */
interface EventObserver {

    fun isActive(): Boolean = true

}
