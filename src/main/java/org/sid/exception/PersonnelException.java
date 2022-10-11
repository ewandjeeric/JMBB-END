package org.sid.exception;

public class PersonnelException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public PersonnelException(String e) {
        super(e);
    }

    public PersonnelException(long id) {
        super("Le personnel donc l'id egale " + id +" n'existe pas");
    }

}
