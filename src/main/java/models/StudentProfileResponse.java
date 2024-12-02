package models;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class StudentProfileResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private List<String> enrolledCourses;
    private String  profileCreationDate;
    private String  lastLogin;
    private String accountStatus;
    private Preferences preferences;
    private PersonalDetails personalDetails;

    @Getter
    @Setter
    public static class Preferences {
        private String language;
        private String timezone;
        private boolean emailNotifications;
    }

    @Getter
    @Setter
    public static class PersonalDetails {
        private int age;
        private String gender;
        private Address address;

        @Getter
        @Setter
        public static class Address {
            private String street;
            private String city;
            private String province;
            private String postalCode;
            private String country;
        }
    }
}
