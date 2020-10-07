package com.kdimitrov.rabbitmq.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kdimitrov.edentist.model.VisitEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = VisitRequest.Builder.class)
@JsonSerialize
public class VisitRequest implements Serializable {

    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "patientMail")
    private String patientMail;

    @JsonProperty(value = "remedialMail")
    private String remedialMail;

    public VisitRequest() {
        super();
    }

    @JsonPOJOBuilder(withPrefix = "set")
    @Setter
    @AllArgsConstructor
    public static class Builder {
        private long id;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime date;
        private String code;
        private String patientMail;
        private String remedialMail;

        public Builder() {
            super();
        }

        public Builder(VisitEntity visit) {
            this.id = visit.getId();
            this.date = visit.getDate();
            this.code = visit.getCode();
        }

        public VisitRequest.Builder setPatientMail(String patientMail) {
            this.patientMail = patientMail;
            return this;
        }

        public VisitRequest.Builder setRemedialMail(String remedialMail) {
            this.remedialMail = remedialMail;
            return this;
        }

        public VisitRequest build() {
            return new VisitRequest(id, date, code, patientMail, remedialMail);
        }
    }
}
