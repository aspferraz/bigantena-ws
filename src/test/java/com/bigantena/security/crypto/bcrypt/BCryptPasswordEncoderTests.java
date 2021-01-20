/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bigantena.security.crypto.bcrypt;

/**
 *
 * @author aspferraz
 */
import org.junit.Test;
import java.security.SecureRandom;
import org.apache.log4j.Logger;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Dave Syer
 *
 */
public class BCryptPasswordEncoderTests {

	@Test
	public void matches() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		String result = encoder.encode("trestles@8");
//                
//                Logger logger = Logger.getLogger(BCryptPasswordEncoderTests.class);
//                logger.info(result);
		
//                assertThat(result.equals("trestles@8")).isFalse();
		assertThat(encoder.matches("trestles@8", "$2a$10$/IbMGgaJ1DqSvr3zHY.KxOYeqNJw5BmMOaTwP2kTOX4SFhewWm6xy")).isTrue();
	}

	@Test
	public void unicode() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String result = encoder.encode("passw\u9292rd");
		assertThat(encoder.matches("pass\u9292\u9292rd", result)).isFalse();
		assertThat(encoder.matches("passw\u9292rd", result)).isTrue();
	}

	@Test
	public void notMatches() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String result = encoder.encode("password");
		assertThat(encoder.matches("bogus", result)).isFalse();
	}

	@Test
	public void customStrength() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
		String result = encoder.encode("password");
		assertThat(encoder.matches("password", result)).isTrue();
	}

	@Test(expected = IllegalArgumentException.class)
	public void badLowCustomStrength() {
		new BCryptPasswordEncoder(3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void badHighCustomStrength() {
		new BCryptPasswordEncoder(32);
	}

	@Test
	public void customRandom() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8, new SecureRandom());
		String result = encoder.encode("password");
		assertThat(encoder.matches("password", result)).isTrue();
	}

	@Test
	public void doesntMatchNullEncodedValue() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		assertThat(encoder.matches("password", null)).isFalse();
	}

	@Test
	public void doesntMatchEmptyEncodedValue() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		assertThat(encoder.matches("password", "")).isFalse();
	}

	@Test
	public void doesntMatchBogusEncodedValue() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		assertThat(encoder.matches("password", "012345678901234567890123456789")).isFalse();
	}

}