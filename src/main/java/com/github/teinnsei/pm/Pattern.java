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

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * public String foo(final String lhs, final String rhs) {
 *     return pattern(on(lhs, rhs))
 *             .match(on(nonNull(), nonNull()), "BOTH_NON_NULL")
 *             .match(on(isNull(), nonNull()), "RHS")
 *             .match(on(nonNull(), isNull()), "LHS")
 *             .or("BOTH_IS_NULL");
 * }
 *
 * @param <T> Type returned by pattern matching
 */
public final class Pattern<T> {

    public static <T> Pattern<T> pattern(final T value) {
        return new Pattern<>(value);
    }

    public static <T1, T2> Tuple2<T1, T2> on(final T1 t1, final T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> on(final T1 t1, final T2 t2, final T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> on(final T1 t1, final T2 t2, final T3 t3, final T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> on(final T1 t1, final T2 t2, final T3 t3, final T4 t4, final T5 t5) {
        return new Tuple5<>(t1, t2, t3, t4, t5);
    }

    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> on(final T1 t1, final T2 t2, final T3 t3, final T4 t4, final T5 t5, final T6 t6) {
        return new Tuple6<>(t1, t2, t3, t4, t5, t6);
    }

    public static <T1, T2> Predicate<Tuple2<T1, T2>> on(final Predicate<T1> t1, final Predicate<T2> t2) {
        return tuple -> t1.test(tuple.t1()) && t2.test(tuple.t2());
    }

    public static <T1, T2, T3> Predicate<Tuple3<T1, T2, T3>> on(final Predicate<T1> t1, final Predicate<T2> t2, final Predicate<T3> t3) {
        return tuple -> t1.test(tuple.t1()) && t2.test(tuple.t2()) && t3.test(tuple.t3());
    }

    public static <T1, T2, T3, T4> Predicate<Tuple4<T1, T2, T3, T4>> on(final Predicate<T1> t1, final Predicate<T2> t2, final Predicate<T3> t3, final Predicate<T4> t4) {
        return tuple -> t1.test(tuple.t1()) && t2.test(tuple.t2()) && t3.test(tuple.t3()) && t4.test(tuple.t4());
    }

    public static <T1, T2, T3, T4, T5> Predicate<Tuple5<T1, T2, T3, T4, T5>> on(final Predicate<T1> t1, final Predicate<T2> t2, final Predicate<T3> t3, final Predicate<T4> t4, final Predicate<T5> t5) {
        return tuple -> t1.test(tuple.t1()) && t2.test(tuple.t2()) && t3.test(tuple.t3()) && t4.test(tuple.t4()) && t5.test(tuple.t5());
    }

    public static <T1, T2, T3, T4, T5, T6> Predicate<Tuple6<T1, T2, T3, T4, T5, T6>> on(final Predicate<T1> t1, final Predicate<T2> t2, final Predicate<T3> t3, final Predicate<T4> t4, final Predicate<T5> t5, final Predicate<T6> t6) {
        return tuple -> t1.test(tuple.t1()) && t2.test(tuple.t2()) && t3.test(tuple.t3()) && t4.test(tuple.t4()) && t5.test(tuple.t5()) && t6.test(tuple.t6());
    }

    public static Predicate<String> isStringEmpty() {
        return s -> s == null || "".equals(s);
    }

    public static Predicate<String> isStringNonEmpty() {
        return isStringEmpty().negate();
    }

    public static <T> Predicate<Optional<T>> isEmpty() {
        return Optional::isEmpty;
    }

    public static <T> Predicate<Optional<T>> isPresent() {
        return Optional::isPresent;
    }

    public static <T> Predicate<T> isNull() {
        return Objects::isNull;
    }

    public static <T> Predicate<T> nonNull() {
        return Objects::nonNull;
    }

    public static <T> Predicate<T> any() {
        return any -> true;
    }

    @SafeVarargs
    public static <T> Predicate<T> any(final T... args) {
        return any -> {
            for (T arg : args) {
                if (arg.equals(any)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static <T> Predicate<T> is(final T value) {
        return value::equals;
    }

    public static <T> Predicate<T> in(final Collection<T> collection) {
        return collection::contains;
    }

    private final T value;

    private Pattern(final T value) {
        this.value = value;
    }

    public <R> Match<T, R> match(final T match, final R ret) {
        return new Match<>(value, match, ret);
    }

    public <R> Match<T, R> match(final T match, final Supplier<R> ret) {
        return new Match<>(value, match, ret);
    }

    public <R> Match<T, R> match(final Predicate<T> caseM, final R returned) {
        return new Match<>(value, caseM, returned);
    }

    public <R> Match<T, R> match(final Predicate<T> caseM, final Supplier<R> returned) {
        return new Match<>(value, caseM, returned);
    }

    public <R> Match<T, R> match(final Predicate<T> caseM, final Function<T, R> returned) {
        return new Match<>(value, caseM, returned);
    }

    public <R, E extends RuntimeException> Match<T, R> match(final Predicate<T> caseM, final ExceptionSupplier<E> returned) {
        return new Match<>(value, caseM, returned);
    }

}
