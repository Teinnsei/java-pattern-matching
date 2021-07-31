package com.github.teinnsei.pm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Partial<T> {

    private final T value;

    Partial(final T value) {
        this.value = value;
    }

    public <B> Partial<B> andThen(final Function<T, B> f) {
        return new Partial<>(f.apply(value));
    }

    public T value() {
        return value;
    }

    public Optional<T> toOption() {
        return Optional.ofNullable(value);
    }

    public CompletionStage<T> toCompletionStage() {
        return CompletableFuture.completedFuture(value);
    }

    public Flow.Publisher<T> toPublisher() {
        return subscriber -> {
            subscriber.onNext(value);
            subscriber.onComplete();
        };
    }

    public void handle(final Consumer<T> f) {
        f.accept(value);
    }

}
