package models;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateResponse {
    private String id;
    private String updateStatus;
    private String message;
}
