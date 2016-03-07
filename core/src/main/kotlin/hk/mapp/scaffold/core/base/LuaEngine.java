package hk.mapp.scaffold.core.base;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.Map;

public class LuaEngine {

    private final LuaFunction NONE_FUNCTION = new ZeroArgFunction() {
        @Override public LuaValue call() {
            return LuaValue.NONE;
        }
    };

    private final Log log;
    private final Globals globals = JsePlatform.standardGlobals(); // create globals

    public LuaEngine(Log log) {
        this.log = log;
    }

    public Compiled load(String script) {
        return new Compiled(this, script, loadFunction(script));
    }

    LuaValue eval(LuaFunction function, Bindings bindings) {
        Globals g = globals;

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (g) {
            g.setmetatable(bindings);

            try {
                return function.call();
            }
            catch (LuaError e) {
                log.catching(e);
                return LuaValue.NONE;
            }
            finally {
                g.setmetatable(null);
            }
        }
    }

    private LuaFunction loadFunction(String script) {
        try {
            return globals.load(script).checkfunction();
        }
        catch (LuaError e) {
            log.catching(e);
            return NONE_FUNCTION;
        }
    }


    public static class Compiled {
        private final LuaEngine engine;
        private final String string;
        private final LuaFunction function;

        Compiled(LuaEngine engine, String string, LuaFunction function) {
            this.engine = engine;
            this.string = string;
            this.function = function;
        }

        public LuaValue eval(Bindings bindings) {
            return engine.eval(function, bindings);
        }

        @Override public String toString() {
            return string;
        }
    }

    public static class Bindings extends LuaTable {
        private final Map<String, Object> map;

        public static Bindings create() {
            return new Bindings(new HashMap<String, Object>());
        }

        private Bindings(final Map<String, Object> map) {
            this.map = map;
            rawset(LuaValue.INDEX, new TwoArgFunction() {
                public LuaValue call(LuaValue table, LuaValue key) {
                    return !key.isstring() ? rawget(key) : toLua(map.get(key.tojstring()));
                }
            });
            rawset(LuaValue.NEWINDEX, new ThreeArgFunction() {
                public LuaValue call(LuaValue table, LuaValue key, LuaValue value) {
                    if (key.isstring()) {
                        String k = key.tojstring();
                        Object v = toJava(value);

                        if (v == null) {
                            map.remove(k);
                        }
                        else {
                            map.put(k, v);
                        }
                    }
                    else {
                        rawset(key, value);
                    }
                    return LuaValue.NONE;
                }
            });
        }

        public Object put(String k, Object v) {
            return map.put(k, v);
        }

        public void putAll(Map<? extends String, ?> map) {
            this.map.putAll(map);
        }

        public void clear() {
            map.clear();
        }
    }

    public static LuaValue toLua(Object o) {
        return o instanceof LuaValue? (LuaValue) o: CoerceJavaToLua.coerce(o);
    }

    public static Object toJava(LuaValue v) {
        switch (v.type()) {
            case LuaValue.TNIL: return null;
            case LuaValue.TSTRING: return v.tojstring();
            case LuaValue.TUSERDATA: return v.checkuserdata(Object.class);
            case LuaValue.TNUMBER: return v.isinttype() ? v.toint(): v.todouble();
            default: return v;
        }
    }

}
