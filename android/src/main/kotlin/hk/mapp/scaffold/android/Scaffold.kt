package hk.mapp.scaffold.android

import android.content.Context
import hk.mapp.scaffold.android.internal.AndroidLog
import hk.mapp.scaffold.android.internal.BusDelegateImpl
import hk.mapp.scaffold.core.ScaffoldBase
import hk.mapp.scaffold.core.base.Log
import hk.mapp.scaffold.core.base.event.Bus

class Scaffold(private val context: Context): ScaffoldBase() {

    override val log: Log = AndroidLog(context)
    override val bus = Bus(BusDelegateImpl())

}
