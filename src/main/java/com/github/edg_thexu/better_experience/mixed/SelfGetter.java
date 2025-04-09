package com.github.edg_thexu.better_experience.mixed;

@SuppressWarnings("unchecked")
public interface SelfGetter<T> {
    default T te$getSelf() {
        return (T) this;
    }
}