package stroom.proxy.repo;

import stroom.meta.shared.AttributeMap;

import java.io.IOException;

public interface StroomHeaderStreamHandler {
    void handleHeader(AttributeMap attributeMap) throws IOException;

}
