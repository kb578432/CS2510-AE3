package uk.ac.nulondon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TestCollectionUtilities {
    @ParameterizedTest
    @MethodSource("findArguments")
    <T> void find(List<T> list, Predicate<T> predicate, T expectedElement) {
        T actualElement = CollectionUtilities.find(list, predicate);
        assertThat(actualElement).isEqualTo(expectedElement);
    }

    public static Stream<Arguments> findArguments() {
        return Stream.of(
                Arguments.of(
                        List.of(),
                        (Predicate<Object>) x -> true,
                        null
                ),
                Arguments.of(
                        List.of(1),
                        (Predicate<Integer>) x -> x == 1,
                        1
                ),
                Arguments.of(
                        List.of(1, 2, 3),
                        (Predicate<Integer>) x -> x == 1,
                        1
                )
        );
    }

    @ParameterizedTest
    @MethodSource("initializeListArguments")
    <T> void initializeList(
            int listSize,
            Function<Integer, T> initializer,
            List<T> expectedList
    ) {
        List<T> actualList = CollectionUtilities.initializeList(listSize, initializer);
        assertThat(actualList).isEqualTo(expectedList);
    }

    static Stream<Arguments> initializeListArguments() {
        return Stream.of(
                Arguments.of(
                        0,
                        (Function<Integer, Object>) x -> x,
                        List.of()
                ),
                Arguments.of(
                        5,
                        (Function<Integer, Integer>) x -> x,
                        List.of(0, 1, 2, 3, 4)
                ),
                Arguments.of(
                        5,
                        (Function<Integer, Integer>) x -> x * x,
                        List.of(0, 1, 4, 9, 16)
                ),
                Arguments.of(
                        5,
                        (Function<Integer, String>) String::valueOf,
                        List.of("0", "1", "2", "3", "4")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("mapArguments")
    <I, O> void map(
            Function<I, O> transformer,
            List<I> list,
            List<O> expectedTransformedList
    ) {
        List<O> actualTransformedList = CollectionUtilities.map(list, transformer);
        assertThat(actualTransformedList).isEqualTo(expectedTransformedList);
    }

    static Stream<Arguments> mapArguments() {
        return Stream.of(
                Arguments.of(
                        (Function) x -> x,
                        List.of(),
                        List.of()
                ),
                Arguments.of(
                        (Function<Integer, Integer>) x -> x + 1,
                        List.of(0, 1, 2),
                        List.of(1, 2, 3)
                ),
                Arguments.of(
                        (Function<String, String>) String::toUpperCase,
                        List.of("a", "b", "c"),
                        List.of("A", "B", "C")
                ),
                Arguments.of(
                        (Function<String, Integer>) String::length,
                        List.of("the", "quick", "brown", "fox"),
                        List.of(3, 5, 5, 3)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("map2DArguments")
    <I, O> void map2D(
            Function<I, O> transformer,
            List<List<I>> list,
            List<List<O>> expectedTransformedList
    ) {
        List<List<O>> actualTransformedList =
                CollectionUtilities.map2D(list, transformer);

        assertThat(actualTransformedList).isEqualTo(expectedTransformedList);
    }

    static Stream<Arguments> map2DArguments() {
        return Stream.of(
                Arguments.of(
                        (Function<Object, Object>) x -> x,
                        List.of(),
                        List.of()
                ),
                Arguments.of(
                        (Function<Integer, Integer>) x -> x + 1,
                        List.of(
                                List.of(0, 1, 2),
                                List.of(3, 4, 5),
                                List.of(6, 7, 8)
                        ),
                        List.of(
                                List.of(1, 2, 3),
                                List.of(4, 5, 6),
                                List.of(7, 8, 9)
                        )
                ),
                Arguments.of(
                        (Function<String, String>) String::toUpperCase,
                        List.of(
                                List.of("a", "b", "c"),
                                List.of("d", "e", "f"),
                                List.of("g", "h", "i")
                        ),
                        List.of(
                                List.of("A", "B", "C"),
                                List.of("D", "E", "F"),
                                List.of("G", "H", "I")
                        )
                ),
                Arguments.of(
                        (Function<String, Integer>) String::length,
                        List.of(
                                List.of("the", "quick", "brown", "fox"),
                                List.of("jumps", "over"),
                                List.of("the", "lazy", "dog")
                        ),
                        List.of(
                                List.of(3, 5, 5, 3),
                                List.of(5, 4),
                                List.of(3, 4, 3)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("sumArguments")
    void sum(List<Integer> list, int expectedSum) {
        int actualSum = CollectionUtilities.sum(list);
        assertThat(actualSum).isEqualTo(expectedSum);
    }

    static Stream<Arguments> sumArguments() {
        return Stream.of(
                Arguments.of(List.of(), 0),
                Arguments.of(List.of(1), 1),
                Arguments.of(List.of(1, 2, 3), 6)
        );
    }

    @ParameterizedTest
    @MethodSource("transposeArguments")
    <T> void transpose(List<List<T>> list, List<List<T>> expectedTranposedList) {
        List<List<T>> actualTransposedList = CollectionUtilities.transpose(list);
        assertThat(actualTransposedList).isEqualTo(expectedTranposedList);

    }

    public static Stream<Arguments> transposeArguments() {
        return Stream.of(
                Arguments.of(List.of(), List.of()),
                Arguments.of(
                        List.of(
                                List.of(1, 2),
                                List.of(3, 4),
                                List.of(5, 6)
                        ),
                        List.of(
                                List.of(1, 3, 5),
                                List.of(2, 4, 6)
                        )
                ),
                Arguments.of(
                        List.of(
                                List.of(1, 2, 3),
                                List.of(4, 5, 6),
                                List.of(7, 8, 9)
                        ),
                        List.of(
                                List.of(1, 4, 7),
                                List.of(2, 5, 8),
                                List.of(3, 6, 9)
                        )
                ),
                Arguments.of(
                        List.of(
                                List.of(1, 2, 3),
                                List.of(4, 5),
                                List.of(6)
                        ),
                        List.of(
                                List.of(1, 4, 6),
                                List.of(2, 5),
                                List.of(3)
                        )
                ),
                Arguments.of(
                        List.of(
                                List.of(),
                                List.of(),
                                List.of()
                        ),
                        List.of()
                )
        );
    }
}