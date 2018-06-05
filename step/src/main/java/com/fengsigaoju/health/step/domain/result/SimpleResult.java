package com.fengsigaoju.health.step.domain.result;

/**
 * 返回单个领域模型
 * @author yutong song
 * @date 2018/4/2
 */
public class SimpleResult<T> extends BaseResult {

    /**
     * 具体返回的对象
     */
    private T result;

    public SimpleResult(T result) {
        super();
        this.result = result;
    }

    public SimpleResult() {

    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}