package com.gutfriendly.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gutfriendly.app.user.model.UserDetails;

@Repository
public interface UserRepo extends JpaRepository<UserDetails, Integer> {

    UserDetails findByPhoneNo(String PhoneNo);

    // NOTE: derived existsBy.../countBy... queries cannot be used on this
    // entity. Its id field is named user_id, which Spring Data reads as the
    // nested path user.id and then fails with
    // "No property 'user' found for type 'UserDetails'". Plain finders select
    // the whole row and are unaffected.
    UserDetails findByEmail(String email);

    
    
}
