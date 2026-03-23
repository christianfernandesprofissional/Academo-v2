package com.academo.service.subject;

import com.academo.controller.dtos.period.SavePeriodDTO;
import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;
import com.academo.model.Group;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.CalculationType;
import com.academo.model.enums.PeriodName;
import com.academo.repository.GroupRepository;
import com.academo.repository.SubjectRepository;
import com.academo.repository.UserRepository;
import com.academo.service.calculation.ICalculationService;
import com.academo.service.period.IPeriodService;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.group.GroupNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import com.academo.util.exceptions.user.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SubjectServiceImpl implements ISubjectService {

    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final IUserService userService;
    private final IPeriodService periodService;
    private final ICalculationService calculationService;

    public SubjectServiceImpl(SubjectRepository subjectRepository, GroupRepository groupRepository, IUserService userService, IPeriodService periodService, ICalculationService calculationService) {
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.periodService = periodService;
        this.calculationService = calculationService;
    }

    @Override
    public List<SubjectDTO> findAll(Integer userId) {
        return subjectRepository.findByUserId(userId).stream().map(SubjectDTO::fromSubject).toList();
    }

    @Override
    public SubjectDTO findById(Integer subjectId, Integer userId) {
        return SubjectDTO.fromSubject(subjectRepository.findByIdAndUserId(subjectId, userId).orElseThrow(SubjectNotFoundException::new));
    }

    @Override
    public List<SubjectDTO> findByGroup(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return group.getSubjects().stream().map(SubjectDTO::fromSubject).toList();
    }

    @Override
    @Transactional
    public SubjectDTO create(Integer userId, CreateSubjectDTO createSubjectDTO) {
        User user = userService.findById(userId);
        Subject subject = new Subject();
        subject.setName(createSubjectDTO.name());
        subject.setDescription(createSubjectDTO.description());
        subject.setUser(user);
        subject = subjectRepository.save(subject);

        SavePeriodDTO p1 = new SavePeriodDTO(subject.getId(), PeriodName.P1.name(), "0", "1");
        SavePeriodDTO p2 = new SavePeriodDTO(subject.getId(), PeriodName.P2.name(), "0", "1");
        periodService.create(userId, p1);
        periodService.create(userId, p2);

        return SubjectDTO.fromSubject(subject);
    }

    @Override
    public SubjectDTO update(Integer userId, Integer subjectId, UpdateSubjectDTO subjectDTO) {
        Subject inDb = subjectRepository.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        if(!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();

        Subject updated = inDb;
        updated.setName(subjectDTO.name());
        updated.setDescription(subjectDTO.description());
        updated.setPassingGrade(new BigDecimal(subjectDTO.passingGrade()));
        updated.setCalculationType(CalculationType.valueOf(subjectDTO.calculationType()));
        updated.setIsActive(subjectDTO.isActive());
        updated = subjectRepository.save(updated);
        return SubjectDTO.fromSubject(updated);
    }

    @Override
    @Transactional
    public void delete(Integer userId, Integer subjectId){
        Subject inDb = subjectRepository.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        if(!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");

        // Verificar esta lógica
        for(Group g : inDb.getGroups()) {
            g.getSubjects().remove(inDb);
            groupRepository.save(g);
        }
        subjectRepository.deleteById(subjectId);
    }
}