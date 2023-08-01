package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequest{" +
                "requestIds=" + requestIds.size() +
                ", status=" + status +
                '}';
    }
}
