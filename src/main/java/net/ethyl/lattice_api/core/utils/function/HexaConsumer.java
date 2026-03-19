package net.ethyl.lattice_api.core.utils.function;

import java.util.Objects;

@FunctionalInterface
public interface HexaConsumer<A, B, C, D, E, F> {
    void accept(A a, B b, C c, D d, E e, F f);

    default HexaConsumer<A, B, C, D, E, F> andThen(HexaConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d, e, f) -> {
            this.accept(a, b, c, d, e, f);
            after.accept(a, b, c, d, e, f);
        };
    }
}
