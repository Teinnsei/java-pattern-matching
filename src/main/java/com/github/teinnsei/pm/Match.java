/*
 * Copyright (c) 2021, Alexander Galagutskiy
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.teinnsei.pm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Match<T, R> {

    private final T value;

    private final R returned;

    Match(final T value, final R returned) {
        this.value = value;
        this.returned = returned;
    }

    Match(final T value, final T caseM, final R returned) {
        this.returned = caseM.equals(value) ? returned : null;
        this.value = value;
    }

    Match(final T value, final T caseM, final Supplier<R> returned) {
        this.returned = caseM.equals(value) ? returned.get() : null;
        this.value = value;
    }

    Match(final T value, final Predicate<T> caseM, final R returned) {
        this.returned = caseM.test(value) ? returned : null;
        this.value = value;
    }

    Match(final T value, final Predicate<T> caseM, final Supplier<R> returned) {
        this.returned = caseM.test(value) ? returned.get() : null;
        this.value = value;
    }

    Match(final T value, final Predicate<T> caseM, final Function<T, R> returned) {
        this.returned = caseM.test(value) ? returned.apply(value) : null;
        this.value = value;
    }

    <E extends RuntimeException> Match(final T value, final Predicate<T> caseM, final ExceptionSupplier<E> returned) {
        if (caseM.test(value)) {
            throw returned.get();
        }
        this.returned = null;
        this.value = value;
    }

    public Match<T, R> match(final T caseM, final R returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public Match<T, R> match(final T caseM, final Supplier<R> returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public Match<T, R> match(final Predicate<T> caseM, final R returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public Match<T, R> match(final Predicate<T> caseM, final Supplier<R> returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public Match<T, R> match(final Predicate<T> caseM, final Function<T, R> returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public <E extends RuntimeException> Match<T, R> match(final Predicate<T> caseM, final ExceptionSupplier<E> returned) {
        if (this.returned != null) return this;
        else return new Match<>(value, caseM, returned);
    }

    public R orNull() {
        return returned;
    }

    public R or(final R defaultValue) {
        if (returned == null) {
            return defaultValue;
        }
        return returned;
    }

    public R or(final Supplier<R> defaultValue) {
        if (returned == null) {
            return defaultValue.get();
        }
        return returned;
    }

    public R or(final Function<T, R> defaultValue) {
        if (returned == null) {
            return defaultValue.apply(value);
        }
        return returned;
    }

    public <E extends RuntimeException> R exceptionally(final ExceptionSupplier<E> defaultValue) {
        final var r = returned;
        if (r == null) {
            throw defaultValue.get();
        }
        return r;
    }

    public Partial<R> partial() {
        return new Partial<>(returned);
    }

    public Partial<R> partial(final R defaultValue) {
        return new Partial<>(defaultValue);
    }

    public Partial<R> partial(final Supplier<R> defaultValue) {
        return new Partial<>(or(defaultValue));
    }

    public Partial<R> partial(final Function<T, R> defaultValue) {
        return new Partial<>(or(defaultValue));
    }

    public <E extends RuntimeException> Partial<R> partial(final ExceptionSupplier<E> defaultValue) {
        return new Partial<>(exceptionally(defaultValue));
    }

    public <T2> Match<T2, R> pattern(final T2 value) {
        return new Match<>(value, returned);
    }

    public Optional<R> toOption() {
        return Optional.ofNullable(returned);
    }

    public CompletionStage<R> toCompletionStage() {
        return CompletableFuture.completedFuture(returned);
    }

    public Flow.Publisher<R> toPublisher() {
        return subscriber -> {
            subscriber.onNext(returned);
            subscriber.onComplete();
        };
    }

}
