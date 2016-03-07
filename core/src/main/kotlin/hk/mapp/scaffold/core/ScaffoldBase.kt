package hk.mapp.scaffold.core

import hk.mapp.scaffold.core.base.Log
import hk.mapp.scaffold.core.base.LuaEngine
import hk.mapp.scaffold.core.base.event.Bus
import hk.mapp.scaffold.core.base.event.EventObserver

abstract class ScaffoldBase: EventObserver {

    abstract val log: Log
    abstract val bus: Bus
    val lua = LuaEngine(log)

}
