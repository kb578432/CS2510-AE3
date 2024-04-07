package uk.ac.nulondon;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * offers helpful methods for working with lists.
 * many of the methods are just to make stream methods more concise.
 */
public class CollectionUtilities {
    /**
     * returns the first element in `collection` which satisfies `predicate`, or null if no such element exists.
     */
    public static <T> T find(Collection<T> collection, Predicate<T> predicate) {
        if (collection == null || collection.isEmpty()) return null;

        return collection.stream()
                         .filter(predicate)
                         .findFirst()
                         .orElse(null);
    }

    /**
     * returns a list such that, for all indices `i`:
     * the i'th element of the list is the output of `initializer` applied to `i`.
     * inspired by kotlin's `List.init`
     */
    public static <T> List<T> initializeList(
            int listSize,
            Function<Integer, T> initializer
    ) {
        if (listSize < 0 || initializer == null) return List.of();

        List<T> list = new ArrayList<>();
        for (int index = 0; index < listSize; index++) {
            T element = initializer.apply(index);
            list.add(element);
        }
        return list;
    }

    /**
     * applies the `transformer` function to all elements of the argued 1d collection.
     * implemented to reduce boilerplate associated with stream.
     */
    public static <I, O> List<O> map(
            Collection<I> collection,
            Function<I, O> transformer
    ) {
        if (collection == null || collection.isEmpty()) return List.of();

        return collection.stream()
                         .map(transformer)
                         .toList();
    }

    /**
     * applies the `transformer` function to all elements of the argued 2d list.
     * implemented to reduce boilerplate associated with stream.
     */
    public static <I, O> List<List<O>> map2D(
            Collection<Collection<I>> collections,
            Function<I, O> transformer
    ) {
        List<List<I>> collectionsAsLists = map(
                collections,
                collection -> collection.stream().toList()
        );
        return map2D(collectionsAsLists, transformer);
    }

    public static <I, O> List<List<O>> map2D(
            List<List<I>> lists,
            Function<I, O> transformer
    ) {
        if (lists == null || lists.isEmpty()) return List.of();

        Function<List<I>, List<O>> listTransformer = row -> map(row, transformer);
        return map(lists, listTransformer);
    }

    /**
     * returns the sum of the argued list of integers.
     */
    public static int sum(Collection<Integer> integers) {
        int sum = 0;
        for (Integer integer : integers) {
            sum += integer;
        }
        return sum;
    }

    /**
     * returns a transposed matrix. that is:
     * the i'th column of `matrix` is the i'th row of the transposed matrix.
     * the i'th row of `matrix` is the i'th column of the transposed matrix.
     */
    public static <T> List<List<T>> transpose(List<List<T>> matrix) {
        if (matrix == null || matrix.isEmpty()) return List.of();

        List<Integer> columnCounts = map(matrix, List::size);
        int maxColumnCount = Collections.max(columnCounts);
        List<List<T>> columns = initializeList(maxColumnCount, ArrayList::new);

        for (List<T> row : matrix) {
            int columnCount = row.size();
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                T element = row.get(columnIndex);
                List<T> column = columns.get(columnIndex);
                column.add(element);
            }
        }
        return columns;
    }

    public static <T, K, V> Map<K, V> initializeMap(
            Collection<T> collection,
            Function<T, K> keyProvider,
            Function<T, V> valueProvider
    ) {
        return collection.stream().collect(toMap(
                keyProvider,
                valueProvider
        ));
    }

    public static <K, V> Map<K, V> initializeMap(
            Collection<K> keys,
            Function<K, V> valueProvider
    ) {
        return initializeMap(
                keys,
                key -> key,
                valueProvider
        );
    }

    public static <T> Set<T> getSetDifference(Set<T> setA, Set<T> setB) {
        return setA.stream()
                   .filter(element -> !setB.contains(element))
                   .collect(toSet());
    }
}
