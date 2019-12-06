package tech.blend.models;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InMemoryUserAuthenticationCsv {

    @Parsed(field = "username")
    String username;

    @Parsed(field = "password")
    String password;

    @Parsed(field = "role")
    String role;
}
