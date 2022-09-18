package ru.practicum.diplom.priv.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.diplom.priv.request.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class RequestDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @NotNull
    private Long event;
    @NotNull
    private Long requester;
    private RequestStatus status;
}
