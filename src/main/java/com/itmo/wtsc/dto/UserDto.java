package com.itmo.wtsc.dto;

import com.itmo.wtsc.dto.cases.NewUserCase;
import com.itmo.wtsc.dto.cases.UpdateUserCase;
import com.itmo.wtsc.utils.enums.UserRole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.itmo.wtsc.utils.ErrorMessages.FIELD_BLANK_ERROR;
import static com.itmo.wtsc.utils.ErrorMessages.FIELD_NULL_ERROR;

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
}
