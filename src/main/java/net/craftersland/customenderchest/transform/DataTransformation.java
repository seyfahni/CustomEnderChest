package net.craftersland.customenderchest.transform;

import java.util.Objects;

public interface DataTransformation<T, R> {

    R transform(T element) throws DataTransformationException;

    T transformBack(R element) throws DataTransformationException;

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
