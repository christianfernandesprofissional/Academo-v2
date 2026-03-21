package com.academo.service.calculation;

import com.academo.model.Activity;
import com.academo.model.ActivityType;
import com.academo.model.Period;
import com.academo.model.Subject;
import com.academo.model.enums.CalculationType;
import com.academo.repository.PeriodRepository;
import com.academo.repository.SubjectRepository;
import com.academo.util.calculo.Calc;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CalculationService implements ICalculationService{

    private final SubjectRepository subjectRepository;
    private final PeriodRepository periodRepository;

    public CalculationService(SubjectRepository subjectRepository, PeriodRepository periodRepository){
        this.subjectRepository = subjectRepository;
        this.periodRepository = periodRepository;
    }

    @Override
    public void updateSubjectAverage(Integer subjectId){
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(SubjectNotFoundException::new);

        switch (subject.getCalculationType()){
            case CalculationType.MEDIA_ARITMETICA -> averageFromSubject(subject);
            case CalculationType.MEDIA_PONDERADA -> weightedAverageFromSubject(subject);
        };

        subjectRepository.save(subject);
    }

    @Override
    public void updatePeriodAverage(Integer periodId) {
        Period period = periodRepository.findById(periodId).orElseThrow(PeriodNotFoundException::new);
        BigDecimal[][] notasEPesos = new BigDecimal[period.getActivityTypeList().size()][period.getActivityTypeList().size()];
        List<ActivityType> activityTypes = period.getActivityTypeList().stream().toList();
        for(int i = 0; i<activityTypes.size();i++){
            BigDecimal nota = Calc.mediaAritmetica(activityTypes.get(i).getActivities().stream().map(Activity::getGrade).toList());
            notasEPesos[i][0] = nota;
            notasEPesos[i][1] = activityTypes.get(i).getWeight();
        }
        period.setGrade(Calc.mediaPonderada(notasEPesos));
        periodRepository.save(period);

    }


    private void weightedAverageFromSubject(Subject subject) {

        List<Period> periods = subject.getPeriods().stream().toList();
        BigDecimal[][] notasEPesos = new BigDecimal[periods.size()][periods.size()];
        int i = 0;
        for(Period p: periods){
            notasEPesos[i][0] = p.getGrade();
            notasEPesos[i][i] = p.getWeight();
        }
        subject.setFinalGrade(Calc.mediaPonderada(notasEPesos));

    }

    private void averageFromSubject(Subject subject) {
        subject.setFinalGrade(Calc.mediaAritmetica(subject.getPeriods().stream().map(Period::getGrade).toList()));
    }

}
