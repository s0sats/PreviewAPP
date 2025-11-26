package com.namoadigital.prj001.core.util.processor;

import java.util.List;


public interface FileProcessorCallback<T> {
    void onProcess(List<T> dataList);
}
