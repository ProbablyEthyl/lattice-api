package net.ethyl.lattice_api.core.utils.function;

import java.util.Objects;

@FunctionalInterface
public interface SeptConsumer<A, B, C, D, E, F, G> {
    void accept(A a, B b, C c, D d, E e, F f, G g);

    default SeptConsumer<A, B, C, D, E, F, G> andThen(SeptConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d, e, f, g) -> {
            this.accept(a, b, c, d, e, f, g);
            after.accept(a, b, c, d, e, f, g);
        };
    }
}
