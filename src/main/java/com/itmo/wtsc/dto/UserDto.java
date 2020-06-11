package com.itmo.wtsc.dto;

import com.itmo.wtsc.dto.cases.NewUserCase;
import com.itmo.wtsc.dto.cases.UpdateUserCase;
import com.itmo.wtsc.utils.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.itmo.wtsc.utils.ErrorMessages.*;

@Data
public class UserDto {

    private Integer id;

    @NotBlank(groups = {NewUserCase.class}, message = FIELD_BLANK_ERROR)
    private String login;

    @NotBlank(groups = {NewUserCase.class}, message = FIELD_BLANK_ERROR)
    private String password;

    @NotNull(groups = {NewUserCase.class}, message = FIELD_NULL_ERROR)
    private UserRole role;

    @NotNull(groups = {UpdateUserCase.class}, message = FIELD_NULL_ERROR)
    private boolean active;

    @NotBlank(groups = {NewUserCase.class}, message = FIELD_BLANK_ERROR)
    @Email(groups = {NewUserCase.class}, message = INVALID_FORMAT_ERROR)
    private String email;
}
