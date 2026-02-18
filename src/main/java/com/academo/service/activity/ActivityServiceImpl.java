package com.academo.service.activity;

import com.academo.controller.dtos.activity.ActivityDTO;
import com.academo.controller.dtos.activity.SaveActivityDTO;
import com.academo.controller.dtos.notification.NotificationDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Activity;
import com.academo.model.ActivityType;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.ActivityRepository;
import com.academo.service.activityType.ActivityTypeServiceImpl;
import com.academo.service.subject.SubjectServiceImpl;
import com.academo.service.user.UserServiceImpl;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activity.ActivityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public  class ActivityServiceImpl implements IActivityService{
    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    SubjectServiceImpl subjectService;

    @Autowired
    ActivityTypeServiceImpl activityTypeService;

    @Override
    public List<ActivityDTO> findAll(Integer userId) {
        return activityRepository.findAllByUserId(userId).stream().map(ActivityDTO::fromActivity).toList();
    }

    @Override
    public ActivityDTO findById(Integer userId, Integer activityId) {
        return ActivityDTO.fromActivity(activityRepository.findById(userId,activityId).orElseThrow(ActivityNotFoundException::new));
    }

    @Override
    public ActivityDTO create(Integer userId, SaveActivityDTO activityDTO) {
        return ActivityDTO.fromActivity(activityRepository.save(fillActivity(userId, activityDTO)));
    }

    @Override
    public ActivityDTO update(Integer userId, Integer activityId, SaveActivityDTO activityDTO) {
        if(activityRepository.findById(userId,activityId).isEmpty()) throw new NotAllowedInsertionException("Inserção inválida");
        return ActivityDTO.fromActivity(activityRepository.save(fillActivity(userId, activityDTO)));
    }

    @Override
    public void delete(Integer userId,Integer activityId) {
        if(activityRepository.findById(userId,activityId).isEmpty()) throw new NotAllowedInsertionException("Deleção inválida");
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
    public List<ActivityDTO> findBySubjectId(Integer subjectId) {
        return activityRepository.findAllBySubjectId(subjectId).stream().map(ActivityDTO::fromActivity).toList();
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
        activity.setValue(activityDTO.value());
        activity.setActivityType(activityType);
        activity.setSubject(subject);
        activity.setUser(user);
        return activity;
    }
}