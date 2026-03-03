package com.academo.service.subject;

import com.academo.controller.dtos.subject.CreateSubjectDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.controller.dtos.subject.UpdateSubjectDTO;
import com.academo.model.Group;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.GroupRepository;
import com.academo.repository.SubjectRepository;
import com.academo.repository.UserRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.group.GroupNotFoundException;
import com.academo.util.exceptions.subject.SubjectNotFoundException;
import com.academo.util.exceptions.user.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements ISubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final IUserService userService;

    public SubjectServiceImpl(SubjectRepository subjectRepository, UserRepository userRepository, GroupRepository groupRepository, IUserService userService) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userService = userService;
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
    public SubjectDTO create(Integer userId, CreateSubjectDTO createSubjectDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Subject subject = new Subject();
        subject.setName(createSubjectDTO.name());
        subject.setDescription(createSubjectDTO.description());
        subject.setUser(user);
        subject = subjectRepository.save(subject);
        return SubjectDTO.fromSubject(subject);
    }

    @Override
    public SubjectDTO update(Integer userId, Integer subjectId, UpdateSubjectDTO subjectDTO) {
        Subject inDb = subjectRepository.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        if(!inDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();
        User user = userService.findById(userId);
        Subject updated = new Subject();
        updated.setId(subjectId);
        updated.setName(subjectDTO.name());
        updated.setDescription(subjectDTO.description());
        updated.setIsActive(subjectDTO.isActive());
        updated.setUser(user);
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