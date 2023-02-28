package kovalenko.vika.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime createDate;
}
