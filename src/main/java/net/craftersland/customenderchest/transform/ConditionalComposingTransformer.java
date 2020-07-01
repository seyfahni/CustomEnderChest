package net.craftersland.customenderchest.transform;

import java.util.function.Predicate;

public class ConditionalComposingTransformer<T, R> implements DataTransformation<T, R> {

    private final Predicate<T> condition;
    private final DataTransformation<T, R> ifBranch;
    private final DataTransformation<T, R> elseBranch;

    public ConditionalComposingTransformer(Predicate<T> condition, DataTransformation<T, R> ifBranch, DataTransformation<T, R> elseBranch) {
        this.condition = condition;
        this.ifBranch = ifBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public R transform(T element) throws DataTransformationException {
        if (condition.test(element)) {
            return ifBranch.transform(element);
        } else {
            return elseBranch.transform(element);
        }
    }

    @Override
    public T transformBack(R element) throws DataTransformationException {
        DataTransformationException parentException = new DataTransformationException("no data transformer branch worked");
        try {
            return ifBranch.transformBack(element);
        } catch (DataTransformationException serializationException) {
            parentException.addSuppressed(serializationException);
        }
        try {
            return elseBranch.transformBack(element);
        } catch (DataTransformationException serializationException) {
            parentException.initCause(serializationException);
        }
        throw parentException;
    }
}
