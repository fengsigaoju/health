package com.fengsigaoju.health.user.domain.result;

import java.util.List;

/**
 * 批量返回领域模型
 * @author yutong song
 * @date 2018/4/2
 */
public class ListResult<T> extends BaseResult {
    private List<T> results;

    public ListResult(List<T> results) {
        super();
        this.results = results;
    }

    public ListResult() {

    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
