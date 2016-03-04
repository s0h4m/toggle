package cc.soham.toggle.enums;

import cc.soham.toggle.CheckRequest;

/**
 * An enum that shows the type of source in the config
 * This is needed so that we can do things like {@link CheckRequest.Builder#getLatest()} when the source type is URL
 */
public enum SourceType {
    STRING,
    JSONOBJECT,
    CONFIG,
    URL,
    NONE
}
