package org.isj.ing.annuarium.webapp.service;


import java.util.Arrays;
import java.util.HashSet;


import org.isj.ing.annuarium.webapp.model.entities.Role;
import org.isj.ing.annuarium.webapp.model.entities.User;
import org.isj.ing.annuarium.webapp.repository.RoleRepository;
import org.isj.ing.annuarium.webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;




@Service("userService")
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;//objet a utiliser pour encrypter le mdp

	@Autowired //injection des dependances soit en attribut soit en declaration des constructeurs
	public UserService(UserRepository userRepository,
			RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User saveUser(User user) {//pour sauvegarder un user
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);

		final Role userRole = roleRepository.findByRole("ADMIN");
//icic nous sommes entrain d'utiliser les entites methodes pas tres recommander il afut d'abord convertir en dto pour manipuler les objets
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));


		return userRepository.save(user);
	}

}