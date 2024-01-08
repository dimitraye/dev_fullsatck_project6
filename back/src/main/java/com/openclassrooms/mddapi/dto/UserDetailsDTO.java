package com.openclassrooms.mddapi.dto;



import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String email;
    private String userName;
    private Set<Theme> themes = new HashSet<>();


    public UserDetailsDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        if (!CollectionUtils.isEmpty(user.getThemes())) {
            this.themes = user.getThemes();
        }
    }
}
