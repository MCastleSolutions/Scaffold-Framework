package hk.mapp.scaffold.core.base

interface Log {

    fun catching(t: Throwable)

    fun debug(msg: () -> Any)

    fun debug(msg: String)

}
