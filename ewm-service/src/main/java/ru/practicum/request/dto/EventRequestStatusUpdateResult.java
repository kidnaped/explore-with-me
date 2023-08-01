package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateResult{" +
                "confirmedRequests=" + confirmedRequests.size() +
                ", rejectedRequests=" + rejectedRequests.size() +
                '}';
    }
}
