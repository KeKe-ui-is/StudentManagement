package raisetech.student.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationErrorResponse {
    private Instant timestamp;
    private int status;
    private List<ViolationErrorDetail> errorDetailList;
    private String url;

}
