package com.kdimitrov.edentist.api.services;

import com.kdimitrov.edentist.model.Result;
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

import static com.kdimitrov.edentist.utils.WorkableLocalDateTimeUtil.workable;

@RequiredArgsConstructor
@Service
public class VisitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public Result<VisitRequest> createVisit(VisitDTO visitDTO, Principal principal) throws NotFoundException {
        return this.createVisit(visitDTO, principal, true);
    }

    public Result<VisitRequest> createVisit(VisitDTO visitDto, Principal principal, boolean persist) throws NotFoundException {
        LOGGER.info("Creating a new visit with principal [PROTECTED].");

        if (isAlreadyRequested(visitDto)) {
            return getNextAvailable(visitDto, principal);
        }

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

        if (persist) {
            visitRepository.save(visitEntity);
        }

        VisitRequest visitRequest = new VisitRequest.Builder(visitEntity)
                .setPatientMail(principalEntity.getEmail())
                .setRemedialMail(remedialEntity.getEmail())
                .build();

        return new Result<>(visitRequest, persist);
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

    private boolean isAlreadyRequested(VisitDTO visitDto) {
        return visitRepository
                .findAlreadyRequest(LocalDateTime.parse(visitDto.getDate()), visitDto.getRemedial())
                .isPresent();
    }

    public Result<VisitRequest> getNextAvailable(VisitDTO visitDto, Principal principal) throws NotFoundException {
        LocalDateTime nextAvailableDateTime = getNextAvailableRecursive(
                workable(LocalDateTime.parse(visitDto.getDate()).plusMinutes(30)),
                visitDto.getRemedial());

        visitDto.setDate(nextAvailableDateTime.toString());
        return this.createVisit(visitDto, principal, false);
    }

    private LocalDateTime getNextAvailableRecursive(LocalDateTime date, String remedial) {
        boolean isBooked = visitRepository
                .findNextAvailable(date, remedial)
                .isPresent();
        if (isBooked) {
            getNextAvailableRecursive(date.plusMinutes(30), remedial);
        }

        return date;
    }
}
