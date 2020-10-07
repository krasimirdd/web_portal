package com.kdimitrov.edentist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kdimitrov.edentist.model.annotation.DateInFuture;
import com.kdimitrov.edentist.model.annotation.TimeIsWorkable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@DateInFuture(date = "date")
@TimeIsWorkable(date = "date")
public class VisitDTO {

    @DateTimeFormat
    private String date;

    @NotBlank
    private String remedial;

    @Override
    public String toString() {
        return "VisitDTO{" +
                "date=" + date +
                ", remedial='" + remedial + '\'' +
                '}';
    }
}
