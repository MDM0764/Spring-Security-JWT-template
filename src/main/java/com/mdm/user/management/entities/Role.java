package com.mdm.user.management.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

@Table(name = "role")
@Entity
@NoArgsConstructor
public class Role  implements GrantedAuthority {

    public Role(int Id) {
        this.id = Id;
    }

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "`Desc`", length = 45)
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.desc;
    }

}