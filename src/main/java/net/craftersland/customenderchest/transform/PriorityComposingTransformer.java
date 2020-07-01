package net.craftersland.customenderchest.transform;

/**
 * Transformer that tries every provided transformation in the given order and returning the result of the first one
 * that worked. Can be used to create fallback transformers.
 *
 * @param <T> the input data type
 * @param <R> the output data type
 */
public class PriorityComposingTransformer<T, R> implements DataTransformation<T, R> {

    DataTransformation<T, R>[] dataTransformers;

    @SafeVarargs
    public PriorityComposingTransformer(DataTransformation<T, R>... dataTransformerPriorityList) {
        this.dataTransformers = dataTransformerPriorityList;
    }

    @Override
    public R transform(T element) throws DataTransformationException {
        DataTransformationException parentException = new DataTransformationException("no data transformer worked");
        for (DataTransformation<T, R> dataTransformer : dataTransformers) {
            try {
                return dataTransformer.transform(element);
            } catch (DataTransformationException serializationException) {
                parentException.addSuppressed(serializationException);
            }
        }
        throw parentException;
    }

    @Override
    public T transformBack(R element) throws DataTransformationException {
        DataTransformationException parentException = new DataTransformationException("no data transformer matched");
        for (DataTransformation<T, R> dataTransformer : dataTransformers) {
            try {
                return dataTransformer.transformBack(element);
            } catch (DataTransformationException serializationException) {
                parentException.addSuppressed(serializationException);
            }
        }
        throw parentException;
    }
}
