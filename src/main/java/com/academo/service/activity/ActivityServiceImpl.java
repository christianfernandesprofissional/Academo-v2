package com.academo.service.activity;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Activity;
import com.academo.model.ActivityType;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.ActivityRepository;
import com.academo.service.activityType.IActivityTypeService;
import com.academo.service.calculation.CalculationService;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activity.ActivityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements IActivityService{

    private final ActivityRepository activityRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final IActivityTypeService activityTypeService;
    private final ICalculationService calculationService;

    public ActivityServiceImpl(ActivityRepository activityRepository, IUserService userService, ISubjectService subjectService, IActivityTypeService activityTypeService, ICalculationService calculationService) {
        this.activityRepository = activityRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.activityTypeService = activityTypeService;
        this.calculationService = calculationService;
    }

    @Override
    public Page<ActivityDTO> findAll(Integer userId, Pageable pageable) {
        return activityRepository.findAllByUserId(userId, pageable).map(ActivityDTO::fromActivity);
    }

    @Override
    public ActivityDTO findById(Integer userId, Integer activityId) {
        return ActivityDTO.fromActivity(activityRepository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityNotFoundException::new));
    }

    @Override
    public ActivityDTO create(Integer userId, SaveActivityDTO activityDTO) {
        ActivityDTO dto =  ActivityDTO.fromActivity(activityRepository.save(fillActivity(userId, activityDTO)));
        calculationService.updatePeriodAverage(activityTypeService.findById(activityDTO.activityTypeId(), userId).getPeriod().getId());
        calculationService.updateSubjectAverage(activityDTO.subjectId());
        return dto;
    }

    @Override
    public ActivityDTO update(Integer userId, Integer activityId, SaveActivityDTO activityDTO) {
        Activity existingActivity = activityRepository.findByIdAndUserId(activityId, userId).orElseThrow(ActivityNotFoundException::new);
        existingActivity.setActivityDate(activityDTO.activityDate());
        existingActivity.setNotificationDate(activityDTO.notificationDate());
        existingActivity.setName(activityDTO.name());
        existingActivity.setDescription(activityDTO.description());
        existingActivity.setGrade(activityDTO.grade());
        ActivityDTO dto =  ActivityDTO.fromActivity(activityRepository.save(existingActivity));
        calculationService.updatePeriodAverage(activityTypeService.findById(activityDTO.activityTypeId(), userId).getPeriod().getId());
        calculationService.updateSubjectAverage(activityDTO.subjectId());
        return dto;
    }

    @Override
    public void delete(Integer userId,Integer activityId) {
        Activity activity = activityRepository.findByIdAndUserId(activityId,userId).orElseThrow(ActivityNotFoundException::new);
        calculationService.updatePeriodAverage(activity.getActivityType().getPeriod().getId());
        calculationService.updateSubjectAverage(activity.getSubject().getId());
        activityRepository.deleteById(activityId);
    }

    @Override
    public Boolean existsByName(String activityName) {
        return activityRepository.existsActivityByName(activityName);
    }

    @Override
    public Boolean existsById(Integer id) {
        return activityRepository.existsById(id);
    }

    @Override
    public Page<ActivityDTO> findAllBySubjectId(Integer userId, Integer subjectId, Pageable pageable) {
        // Esse dto abaixo serve apenas para tentar recuperar um Subject com este ID e UserID. Caso o subject não pertença
        // ao usuário da requisição, será lançada uma exceção
        SubjectDTO dto = subjectService.findById(subjectId, userId);
        return activityRepository.findAllBySubjectId(subjectId, pageable).map(ActivityDTO::fromActivity);
    }

    /**
     * Preenche a classe Activity buscando
     * todas as dependências nos seus respectivos
     * services.
     * @author Christian
     * @return Activity
     */
    private Activity fillActivity(Integer userId, SaveActivityDTO activityDTO){
        User user = userService.findById(userId);
        ActivityType activityType = activityTypeService.findById(activityDTO.activityTypeId(), userId);
        SubjectDTO dto = subjectService.findById(activityDTO.subjectId(), userId);
        Subject subject = SubjectDTO.toSubject(activityDTO.subjectId(), dto);
        Activity activity = new Activity();
        activity.setName(activityDTO.name());
        activity.setDescription(activityDTO.description());
        activity.setActivityDate(activityDTO.activityDate());
        activity.setNotificationDate(activityDTO.notificationDate());
        activity.setGrade(activityDTO.grade());
        activity.setActivityType(activityType);
        activity.setSubject(subject);
        activity.setUser(user);
        return activity;
    }
}