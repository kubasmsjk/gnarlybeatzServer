package pl.gnarlybeatz.gnarlybeatzServer.validator;

import jakarta.validation.*;
import org.springframework.stereotype.Component;
import pl.gnarlybeatz.gnarlybeatzServer.auth.AuthenticateRequest;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.ObjectNotValidException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator<T extends AuthenticateRequest> {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    public void validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        if (!violations.isEmpty()) {
            var errorMessages = violations
                    .stream()
                    .collect(Collectors.toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage,
                            (existing, replacement) -> existing));
            throw new ObjectNotValidException(errorMessages);
        }
    }
}
