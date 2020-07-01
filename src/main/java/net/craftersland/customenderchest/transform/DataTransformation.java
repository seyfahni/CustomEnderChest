package net.craftersland.customenderchest.transform;

import java.util.Objects;

/**
 * A reversible data transformation of arbitrary data.
 *
 * @param <T> the input data type
 * @param <R> the output data type
 */
public interface DataTransformation<T, R> {

    /**
     * Transform data. This can be reversed with {@link #transformBack(R)}.
     *
     * @param element the data to transform
     * @return the transformed data
     * @throws DataTransformationException when the transformation fails
     */
    R transform(T element) throws DataTransformationException;

    /**
     * Reverse transformation of data. This reverses the results of {@link #transform(T)}.
     *
     * @param element the transformed data
     * @return the original data
     * @throws DataTransformationException when the transformation fails
     */
    T transformBack(R element) throws DataTransformationException;

    /**
     * Create a transformation that first transforms with the given transformation and then with this transformation.
     * When reversing, the transformation are applied in reverse order.
     *
     * @param before the transformation to apply before
     * @param <V> the new input data type
     * @return the combined transformation
     */
    default <V> DataTransformation<V, R> andBefore(DataTransformation<V, T> before) {
        Objects.requireNonNull(before);
        return new DataTransformation<V, R>() {
            @Override
            public R transform(V element) throws DataTransformationException {
                return DataTransformation.this.transform(before.transform(element));
            }

            @Override
            public V transformBack(R element) throws DataTransformationException {
                return before.transformBack(DataTransformation.this.transformBack(element));
            }
        };
    }

    /**
     * Create a transformation that first transforms with this transformation and then with the given transformation.
     * When reversing, the transformation are applied in reverse order.
     *
     * @param after the transformation to apply after
     * @param <V> the new output data type
     * @return the combined transformation
     */
    default <V> DataTransformation<T, V> andThen(DataTransformation<R, V> after) {
        Objects.requireNonNull(after);
        return new DataTransformation<T, V>() {
            @Override
            public V transform(T element) throws DataTransformationException {
                return after.transform(DataTransformation.this.transform(element));
            }

            @Override
            public T transformBack(V element) throws DataTransformationException {
                return DataTransformation.this.transformBack(after.transformBack(element));
            }
        };
    }

    /**
     * Inverse this transformation. Executing the inverted and base transformation in sequence equals the identity,
     * but is less efficient.
     *
     * @return the inverted transformation
     */
    default DataTransformation<R, T> inverse() {
        return new DataTransformation<R, T>() {
            @Override
            public T transform(R element) throws DataTransformationException {
                return DataTransformation.this.transformBack(element);
            }

            @Override
            public R transformBack(T element) throws DataTransformationException {
                return DataTransformation.this.transform(element);
            }
        };
    }

    /**
     * Get a identity transformation. The data will not be altered.
     *
     * @param <T> the data type
     * @return an identity transformation
     */
    static <T> DataTransformation<T, T> identity() {
        return new DataTransformation<T, T>() {
            @Override
            public T transform(T element) {
                return element;
            }

            @Override
            public T transformBack(T element) {
                return element;
            }
        };
    }
}
