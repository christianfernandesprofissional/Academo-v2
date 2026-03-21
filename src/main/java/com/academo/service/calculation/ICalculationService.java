package com.academo.service.calculation;

import com.academo.model.Period;
import com.academo.model.Subject;

import java.math.BigDecimal;

public interface ICalculationService{

    void updateSubjectAverage(Integer subjectId);
    void updatePeriodAverage(Integer periodId);

}
