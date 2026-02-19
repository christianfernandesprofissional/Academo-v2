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
import com.academo.service.subject.ISubjectService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.activity.ActivityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements IActivityService{

    private final ActivityRepository activityRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;
    private final IActivityTypeService activityTypeService;

    public ActivityServiceImpl(ActivityRepository activityRepository, IUserService userService, ISubjectService subjectService, IActivityTypeService activityTypeService) {
        this.activityRepository = activityRepository;
        this.userService = userService;
        this.subjectService = subjectService;
        this.activityTypeService = activityTypeService;
    }

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
    public List<ActivityDTO> findBySubjectId(Integer userId, Integer subjectId) {
        // Esse dto abaixo serve apenas para tentar recuperar um Subject com este ID e UserID. Caso o subject não pertença
        // ao usuário da requisição, será lançada uma exceção
        SubjectDTO dto = subjectService.findById(subjectId, userId);
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