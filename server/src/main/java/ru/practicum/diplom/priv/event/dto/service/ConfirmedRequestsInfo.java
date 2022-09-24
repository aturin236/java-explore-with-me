package ru.practicum.diplom.priv.event.dto.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConfirmedRequestsInfo {
    private Long eventId;
    private int countRequests;
}
