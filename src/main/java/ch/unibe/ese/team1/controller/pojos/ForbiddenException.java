package ch.unibe.ese.team1.controller.pojos;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

	/**
	 * Random serial id
	 */
	private static final long serialVersionUID = -33083670661828665L;
	
}
