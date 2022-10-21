package com.bootcamp.dscatalog.services.validation;

import com.bootcamp.dscatalog.dto.UserInsertDTO;
import com.bootcamp.dscatalog.entities.User;
import com.bootcamp.dscatalog.repository.UserRepository;
import com.bootcamp.dscatalog.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserInsertDTO> {

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Autowired
    public UserRepository userRepository;

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
        User user = userRepository.findByEmail(dto.getEmail());
        if(user != null){
            list.add(new FieldMessage("email", "Email ja existe"));
        }


        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
