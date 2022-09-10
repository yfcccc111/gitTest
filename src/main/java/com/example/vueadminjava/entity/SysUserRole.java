package com.example.vueadminjava.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yufu
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode()
public class SysUserRole {
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;

}
