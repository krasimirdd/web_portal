package com.kdimitrov.edentist.api.services;

import com.kdimitrov.edentist.model.UserEntity;
import com.kdimitrov.edentist.model.VisitEntity;
import com.kdimitrov.edentist.model.dto.VisitApprovalDto;
import com.kdimitrov.edentist.model.dto.VisitDTO;
import com.kdimitrov.edentist.repository.UserRepository;
import com.kdimitrov.edentist.repository.VisitRepository;
import com.kdimitrov.edentist.utils.PasswordHelper;
import com.kdimitrov.rabbitmq.model.VisitRequest;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public VisitRequest createVisit(VisitDTO visitDto, Principal principal) throws NotFoundException {
        LOGGER.info("Creating a new visit with principal [PROTECTED].");

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setDate(LocalDateTime.parse(visitDto.getDate()));
        UserEntity principalEntity = userRepository.findOneByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("No such principal in the DB"));
        UserEntity remedialEntity = userRepository.findOneByEmail(visitDto.getRemedial())
                .orElseThrow(() -> new NotFoundException("No such remedial in the DB"));
        visitEntity.setPatient(principalEntity);
        visitEntity.setRemedial(remedialEntity);
        visitEntity.setCode(PasswordHelper.generatePassword(5));
        visitEntity.setApproved(false);
        visitEntity.setDeclined(false);

        visitRepository.save(visitEntity);
        return new VisitRequest.Builder(visitEntity)
                .setPatientMail(principalEntity.getEmail())
                .setRemedialMail(remedialEntity.getEmail())
                .build();
    }

    public List<VisitEntity> getNotProcessed(long userId) {
        return visitRepository.findByRemedialIdAndApprovedIsFalseAndDeclinedIsFalse(userId);
    }

    public List<VisitEntity> getAllForPatient(long userId) {
        return visitRepository.findAllByPatientId(userId);
    }

    public void changeStatus(VisitApprovalDto visitApprovalDto) throws NotFoundException {
        VisitEntity entity = visitRepository.findByCode(visitApprovalDto.getCode())
                .orElseThrow(() -> new NotFoundException("No entity found!"));

        if (visitApprovalDto.isStatus()) {
            entity.setApproved(true);
        } else {
            entity.setDeclined(true);
        }
        visitRepository.saveAndFlush(entity);
    }
}
