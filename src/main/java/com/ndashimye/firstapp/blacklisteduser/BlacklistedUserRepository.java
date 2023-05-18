package com.ndashimye.firstapp.blacklisteduser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedUserRepository extends JpaRepository<BlacklistedUser, Long> {

}
