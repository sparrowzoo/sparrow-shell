package com.sparrow.mvc.result;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.ConfigUtility;

public class ResultErrorAssembler {
    public static Result assemble(BusinessException exception, String language) {
        Result result = Result.FAIL(exception);
        String error = ConfigUtility.getLanguageValue(result.getKey(), language, result.getError());
        if (!CollectionsUtility.isNullOrEmpty(exception.getParameters())) {
            error = String.format(error, exception.getParameters().toArray());
        }
        result.setError(error);
        return result;
    }
}
