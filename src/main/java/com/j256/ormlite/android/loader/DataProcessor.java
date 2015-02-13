package com.j256.ormlite.android.loader;

import java.util.Collection;

public interface DataProcessor<T>  {
    public void process(Collection<T> dataSet);
}
