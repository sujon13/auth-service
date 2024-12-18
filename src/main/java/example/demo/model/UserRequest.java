package example.demo.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
    @NotNull
    private String userName;
    private String name;
    //@NotNull
    private String rawPassword;
}
