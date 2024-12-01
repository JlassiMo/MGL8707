package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameUpdateResponse {
    private String id;
    private String oldUsername;
    private String newUsername;
    private String updateStatus;
    private String message;
}
