package com.jadventure.runtime;

public class ServiceLocator {

    public static void provide(IOHandler handler) {
        _handler.set(handler);
    }

    public static IOHandler getIOHandler() {
        assert (_handler.get() != null);

        return _handler.get();
    }

    private static final ThreadLocal<IOHandler> _handler = new ThreadLocal<IOHandler>();
}
