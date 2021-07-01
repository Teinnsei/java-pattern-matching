# Java Pattern Matching


```java
public String foo(final String lhs, final String rhs) {
    return pattern(on(lhs, rhs))
            .match(on(nonNull(), nonNull()), "BOTH_NON_NULL")
            .match(on(isNull(), nonNull()), "RHS")
            .match(on(nonNull(), isNull()), "LHS")
            .or("BOTH_IS_NULL");
}

foo(null, "some")   -> "RHS"
foo("some", null)   -> "LHS"
foo("some", "some") -> "BOTH_NON_NULL"
foo(null, null)     -> "BOTH_IS_NULL"
```