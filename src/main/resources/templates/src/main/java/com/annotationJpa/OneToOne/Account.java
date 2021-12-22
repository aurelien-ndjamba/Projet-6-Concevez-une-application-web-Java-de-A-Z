package com.annotationJpa.OneToOne;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;


/** 
 * Classe modélisant un objet entité de type "Account"
 * 
 */
@Data
@Entity
@Table(name = "account", schema = "public")
public class Account {
	@Id
	@Column(name = "id", nullable = false)
    private Integer id;
	
//	private String email;
	 
	private String bank;
	
	@OneToOne//( fetch = FetchType.EAGER )
	@JoinColumn(name = "email")
	private AppUser appUser;
	
}