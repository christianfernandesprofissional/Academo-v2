package com.academo.service.group;

import com.academo.controller.dtos.group.CreateGroupDTO;
import com.academo.controller.dtos.group.GroupDTO;
import com.academo.controller.dtos.group.UpdateGroupDTO;
import com.academo.controller.dtos.subject.SubjectDTO;
import com.academo.model.Group;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.repository.GroupRepository;
import com.academo.service.subject.ISubjectService;
import com.academo.service.subject.SubjectServiceImpl;
import com.academo.service.user.IUserService;
import com.academo.service.user.UserServiceImpl;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.group.GroupNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;
    private final IUserService userService;
    private final ISubjectService subjectService;

    public GroupServiceImpl(GroupRepository groupRepository, IUserService userService, ISubjectService subjectService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    @Override
    public List<GroupDTO> findAll(Integer userId){
        return groupRepository.findAllByUserId(userId).stream().map(GroupDTO::fromGroup).toList();
    }

    @Override
    public GroupDTO findById(Integer userId, Integer groupId){
        return GroupDTO.fromGroup(groupRepository.findById(groupId, userId).orElseThrow(GroupNotFoundException::new));
    }

    @Override
    public GroupDTO create(Integer userId, CreateGroupDTO createGroupDTO){
        User user =  userService.findById(userId);
        Group g = new Group();
        g.setName(createGroupDTO.name());
        g.setDescription(createGroupDTO.description());
        g.setUser(user);
        return GroupDTO.fromGroup(g);
    }

    @Override
    public GroupDTO update(Integer userId, Integer groupId, UpdateGroupDTO updateGroupDTO) {
        Group groupDb = groupRepository.findByIdAndUserId(groupId, userId).orElseThrow(GroupNotFoundException::new);
        if (!groupDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();
        groupDb.setName(updateGroupDTO.name());
        groupDb.setDescription(updateGroupDTO.description());
        groupDb.setIsActive(updateGroupDTO.isActive());
        groupDb.setSubjects(groupRepository.updateGroupDTO.subjectsId());
        return groupRepository.save(groupDb);
    }

    @Override
    public void remove(Integer userId, Integer groupId) {
        Group groupDb = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        if (!groupDb.getUser().getId().equals(userId)) throw new NotAllowedInsertionException("Deleção inválida!");
        groupRepository.deleteById(groupId);
    }

    @Override
    @Transactional
    public Group addSubjectToGroup(Integer userId, Integer groupId, Integer subjectId) {
        Group group = verifyGroup(userId, groupId);
        Subject subject = verifySubject(userId, subjectId);

        group.getSubjects().add(subject);
        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public Group deleteSubjectFromGroup(Integer userId, Integer groupId, Integer subjectId) {
        Group group = verifyGroup(userId, groupId);
        Subject subject = verifySubject(userId, subjectId);

        group.getSubjects().remove(subject);
        return groupRepository.save(group);
    }

    @Override
    public Group associateSubjects(Integer userId, Integer groupId, List<Integer> subjectsIds) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        List<Subject> subjects = group.getSubjects();
        for(Integer id : subjectsIds) {
            Subject subject = subjectService.findById(id);
            if(subject != null) {
                subjects.add(subject);
            }
        }
        group.setSubjects(subjects);
        return group;
    }

    /**
     * Verifica se o grupo pertence ao mesmo usuário,
     *
     * @param userId id do usuário
     * @param groupId id do grupo
     * @return Group
     */
    private Group verifyGroup(Integer userId, Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        if(!group.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();
        return group;
    }

    /**
     * Verifica se o Subject pertence ao mesmo usuário,
     *
     * @param userId id do usuário
     * @param subjectId id do subject
     * @return Subject
     */
    private Subject verifySubject(Integer userId, Integer subjectId){
        Subject subject = subjectService.getSubjectByIdAndUserId(subjectId, userId);
        if(!subject.getUser().getId().equals(userId)) throw new NotAllowedInsertionException();
        return subject;
    }

}
