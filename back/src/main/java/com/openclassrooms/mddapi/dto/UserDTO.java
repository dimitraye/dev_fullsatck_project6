package com.openclassrooms.mddapi.dto;



import com.openclassrooms.mddapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();;
    }
}
