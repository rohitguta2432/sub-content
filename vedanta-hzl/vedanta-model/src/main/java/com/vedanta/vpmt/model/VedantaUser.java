package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Class to handle user authentication to be handled by spring authentication
 */
@Getter
@Setter
@NoArgsConstructor
public class VedantaUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String password;
	private String loginId;
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean accountNonExpired = true;
	private Boolean accountNonLocked = true;
	private Boolean credentialsNonExpired = true;
	private Boolean enabled;
	private String employeeId;
    private String emailId;
    private Long businessUnitId;
    private String plantCode;
    private Long plantId = 0L;

    private int isWithoutADAllowed;

	public VedantaUser(Long id, String username, String password, String loginId,

			Collection<? extends GrantedAuthority> authorities, String employeeId,String emailId, boolean isActive, int isWithoutADAllowed, Long businessUnitId,String plantCode, Long plantId) {

		this.setId(id);
		this.setUsername(username);
		this.setPassword(password);
		this.setLoginId(loginId);
		this.setAuthorities(authorities);

		this.setEmailId(emailId);		
		this.setEmployeeId(employeeId);
        this.setEnabled(isActive);
        this.setIsWithoutADAllowed(isWithoutADAllowed);
        this.setBusinessUnitId(businessUnitId);
        this.setPlantCode(plantCode);
        this.setPlantId(plantId);
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@JsonIgnore
	public Boolean getAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.getAccountNonExpired();
	}

	@JsonIgnore
	public Boolean getAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.getAccountNonLocked();
	}

	@JsonIgnore
	public Boolean getCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.getCredentialsNonExpired();
	}

	@JsonIgnore
	public Boolean getEnabled() {
		return this.enabled;
	}

	@Override
	public boolean isEnabled() {
		return this.getEnabled();
	}

}
