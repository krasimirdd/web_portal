package com.kdimitrov.edentist.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VisitApprovalDto {

    private String code;
    private String remedial;
    private boolean status;
}
