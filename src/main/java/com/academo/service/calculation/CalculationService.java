package com.academo.service.calculation;

import com.academo.model.Activity;
import com.academo.model.ActivityType;
import com.academo.model.Period;
import com.academo.model.Subject;
import com.academo.model.enums.period.CalculationType;
import com.academo.model.enums.period.PeriodName;
import com.academo.repository.PeriodRepository;
import com.academo.repository.SubjectRepository;
import com.academo.util.calculo.Calc;
import com.academo.util.exceptions.period.PeriodNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
        boolean subjectHasExam = subject.getPeriods().stream().anyMatch(p -> PeriodName.valueOf(p.getName()).equals(PeriodName.EXAME));

        switch (subject.getCalculationType()){
            case CalculationType.MEDIA_ARITMETICA -> averageFromSubject(subject);
            case CalculationType.MEDIA_PONDERADA -> weightedAverageFromSubject(subject);
        };

        subjectRepository.save(subject);
    }

    @Override
    public void updatePeriodAverage(Integer periodId) {
        Period period = periodRepository.findById(periodId).orElseThrow(PeriodNotFoundException::new);
       if(!PeriodName.valueOf(period.getName()).equals(PeriodName.EXAME)){
           BigDecimal[][] notasEPesos = new BigDecimal[period.getActivityTypeList().size()][2];
           List<ActivityType> activityTypes = period.getActivityTypeList().stream().toList();
           for(int i = 0; i<activityTypes.size();i++){
               BigDecimal nota = Calc.mediaAritmetica(activityTypes.get(i).getActivities().stream().map(Activity::getGrade).toList());
               notasEPesos[i][0] = nota;
               notasEPesos[i][1] = activityTypes.get(i).getWeight();
           }
           period.setGrade(Calc.mediaPonderada(notasEPesos));
           periodRepository.save(period);
       }
    }


    private void weightedAverageFromSubject(Subject subject) {
        boolean subjectHasExam = subject.getPeriods().stream().anyMatch(p -> PeriodName.valueOf(p.getName()).equals(PeriodName.EXAME));
        List<Period> periods = new ArrayList<>(subject.getPeriods());

        Optional<Period> exam = periods.stream()
                .filter(p -> PeriodName.valueOf(p.getName()).equals(PeriodName.EXAME))
                .findFirst();

        exam.ifPresent(periods::remove);

        BigDecimal[][] notasEPesos = new BigDecimal[periods.size()][2];

        int i = 0;
        for(Period p: periods){
               notasEPesos[i][0] = p.getGrade();
               notasEPesos[i][1] = p.getWeight();
               i++;
        }
        BigDecimal mediaFinal = Calc.mediaPonderada(notasEPesos);
        if(subjectHasExam && exam.isPresent()){
               mediaFinal = mediaFinal.add(exam.get().getGrade()).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        }

        subject.setFinalGrade(mediaFinal);

    }

    private void averageFromSubject(Subject subject) {
        Set<Period> periods = new HashSet<>(subject.getPeriods());
        boolean subjectHasExam = subject.getPeriods().stream().anyMatch(p -> PeriodName.valueOf(p.getName()).equals(PeriodName.EXAME));
        if(subjectHasExam){
            periods.stream().min(Comparator.comparing(Period::getGrade)).ifPresent(periods::remove);
        }

        subject.setFinalGrade(Calc.mediaAritmetica(periods.stream().map(Period::getGrade).toList()));
    }

    @Override
    public BigDecimal sumWeights(Collection<BigDecimal> pesos) {
        return pesos.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
