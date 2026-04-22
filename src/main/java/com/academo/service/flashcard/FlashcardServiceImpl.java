package com.academo.service.flashcard;

import com.academo.controller.dtos.flashcard.CreateFlashcardDTO;
import com.academo.controller.dtos.flashcard.FlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateFlashcardDTO;
import com.academo.controller.dtos.flashcard.UpdateLevelDTO;
import com.academo.model.Flashcard;
import com.academo.model.Subject;
import com.academo.model.User;
import com.academo.model.enums.flashcard.CardLevel;
import com.academo.repository.FlashcardRepository;
import com.academo.repository.GroupRepository;
import com.academo.util.exceptions.NotAllowedInsertionException;
import com.academo.util.exceptions.flashcard.FlashcardNotFoundException;
import com.academo.util.exceptions.group.GroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FlashcardServiceImpl implements IFlashcardService{

    private final FlashcardRepository repository;

    private final GroupRepository groupRepository;

    public FlashcardServiceImpl(FlashcardRepository repository, GroupRepository groupRepository){
        this.repository = repository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<FlashcardDTO> findAllBySubjectId(Integer userId, Integer subjectId) {
        return repository.findAllByUserIdAndSubjectId(userId, subjectId).stream().map(FlashcardDTO::fromFlashcard).toList();
    }

    @Override
    public Page<FlashcardDTO> findAllByUserId(Integer userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable).map(FlashcardDTO::fromFlashcard);
    }

    @Override
    public List<FlashcardDTO> findAllByLevel(Integer userId, Integer subjectId, String level) {
        CardLevel cardLevel;
        try{
            cardLevel = CardLevel.valueOf(level.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new FlashcardNotFoundException("Nivel inválido!");
        }


        return findAllBySubjectId(userId, subjectId)
                .stream()
                .filter(p -> cardLevel.equals(CardLevel.valueOf(p.level())))
                .toList();
    }

    @Override
    public List<FlashcardDTO> findAllByGroupId(Integer userId, Integer groupId, String level) {
        groupRepository.findByIdAndUserId(groupId, userId).orElseThrow(GroupNotFoundException::new);

        CardLevel cardLevel = null;
        if (level != null) {
            try {
                cardLevel = CardLevel.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new FlashcardNotFoundException("Nivel inválido!");
            }
        }

        return repository.findAllByUserIdAndGroupIdAndLevel(userId, groupId, cardLevel)
                .stream()
                .map(FlashcardDTO::fromFlashcard)
                .toList();
    }

    @Override
    public FlashcardDTO findById(Integer userId, Integer flashcardId){
        return FlashcardDTO.fromFlashcard(repository.findByIdAndUserId(flashcardId, userId).orElseThrow(FlashcardNotFoundException::new));
    }

    @Override
    public FlashcardDTO create(Integer userId, CreateFlashcardDTO dto) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFrontPart(dto.frontPart());
        flashcard.setBackPart(dto.backPart());
        flashcard.setLevel(CardLevel.SEM_NIVEL);
        Subject subject = new Subject();
        subject.setId(dto.subjectId());
        flashcard.setSubject(subject);
        User user = new User();
        user.setId(userId);
        flashcard.setUser(user);
        flashcard = repository.save(flashcard);
        return FlashcardDTO.fromFlashcard(flashcard);
    }

    @Override
    public FlashcardDTO update(Integer userId, Integer id, UpdateFlashcardDTO dto) {
        Flashcard flashcard = repository.findByIdAndUserId(id, userId).orElseThrow(FlashcardNotFoundException::new);

        CardLevel cardLevel;

        try{
            cardLevel = CardLevel.valueOf(dto.level());
        }catch (IllegalArgumentException e){
            throw new FlashcardNotFoundException("Nivel inválido!");
        }

        flashcard.setLevel(cardLevel);
        flashcard.setFrontPart(dto.frontPart());
        flashcard.setBackPart(dto.backPart());
        repository.save(flashcard);
        return FlashcardDTO.fromFlashcard(flashcard);
    }

    @Override
    public FlashcardDTO updateLevel(Integer userId, Integer id, UpdateLevelDTO dto) {
        Flashcard flashcard = repository.findByIdAndUserId(id, userId).orElseThrow(FlashcardNotFoundException::new);
        CardLevel cardLevel;
        try{
            cardLevel = CardLevel.valueOf(dto.level());
        }catch (IllegalArgumentException e){
            throw new FlashcardNotFoundException("Nivel inválido!");
        }
        flashcard.setLevel(cardLevel);
        repository.save(flashcard);
        return FlashcardDTO.fromFlashcard(flashcard);
    }

    @Override
    public void delete(Integer userId, Integer flashcardId) {
        Flashcard flashcard = repository.findByIdAndUserId(flashcardId, userId).orElseThrow(FlashcardNotFoundException::new);
        if(!Objects.equals(flashcard.getUser().getId(), userId)) throw new NotAllowedInsertionException("Deleção inválida");
        repository.deleteById(flashcardId);
    }

}
